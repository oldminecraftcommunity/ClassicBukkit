package com.mojang.minecraft.server;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.User;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.BlockData;
import com.mojang.minecraft.net.MoveData;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.net.PosData;
import com.mojang.minecraft.net.packets.ChatMessagePacket;
import com.mojang.minecraft.net.packets.KickPlayerPacket;
import com.mojang.minecraft.net.packets.LevelDataChunkPacket;
import com.mojang.minecraft.net.packets.LevelFinalizePacket;
import com.mojang.minecraft.net.packets.LevelInitializePacket;
import com.mojang.minecraft.net.packets.LoginPacket;
import com.mojang.minecraft.net.packets.PlaceOrRemoveTilePacket;
import com.mojang.minecraft.net.packets.PlayerJoinPacket;
import com.mojang.minecraft.net.packets.PlayerMoveAndRotatePacket;
import com.mojang.minecraft.net.packets.PlayerMovePacket;
import com.mojang.minecraft.net.packets.PlayerRotatePacket;
import com.mojang.minecraft.net.packets.PlayerTeleportPacket;
import com.mojang.minecraft.net.packets.TimedOutPacket;

import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.plugins.PluginManager;
import met.realfreehij.classicbukkit.plugins.events.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public final class PlayerInstance {
	private static Logger logger = MinecraftServer.logger;
	public final SocketConnection connection;
	private final MinecraftServer minecraft;
	private boolean onlyIP = false;
	private boolean sendingPackets = false;
	public String name = "";
	public final int playerID;
	private ArrayList<Packet> packets = new ArrayList<>();
	private long currentTime;
	private List<PosData> positionData = new ArrayList<>();
	private int chatCounter = 0;
	public int x;
	public int y;
	public int z;
	public int pitch;
	public int yaw;
	private boolean ignorePackets = false;
	private int packetHandlingCounter = 0;
	private int ticks = 0;
	private volatile byte[] blocks = null;
	public boolean placeUnbreakable = false;

	public PlayerInstance(MinecraftServer var1, SocketConnection var2, int var3) {
		this.minecraft = var1;
		this.connection = var2;
		this.playerID = var3;
		this.currentTime = System.currentTimeMillis();
		var2.player = this;
		Level var4 = var1.level;
		this.x = (var4.xSpawn << 5) + 16;
		this.y = (var4.ySpawn << 5) + 16;
		this.z = (var4.zSpawn << 5) + 16;
		this.yaw = (int)(var4.rotSpawn * 256.0F / 360.0F);
		this.pitch = 0;
	}

	public final String toString() {
		if(!this.onlyIP) return this.connection.ip;
		else return this.name + " (" + this.connection.ip + ")";
	}
	public static final byte PROTOCOL_VERSION = (byte)6;
	public final void handlePacket(Packet packet) {
		if(this.ignorePackets) return;
		
		if(packet instanceof LoginPacket) {
			LoginPacket pk = (LoginPacket) packet;
			byte protocol = pk.protocol;
			String username = pk.username.trim();
			String var8 = pk.something;
			char[] userarr = username.toCharArray();

			for(int var5 = 0; var5 < userarr.length; ++var5) {
				if(userarr[var5] < 32 || userarr[var5] > 127) {
					this.kickCheat("Bad name!");
					return;
				}
			}

			if(this.minecraft.verifyNames && !var8.equals(this.minecraft.mpPassCalculator.calcMPass(username))) {
				this.kick("Illegal name.");
			} else {
				PlayerInstance var11 = this.minecraft.getPlayerByName(username);
				if(var11 != null) {
					this.kick("Player with same nick already online.");
				}
				logger.info(this + " logged in as " + username);
				if(protocol != PROTOCOL_VERSION) {
					this.kick("Wrong protocol version.");
				} else if(this.minecraft.banned.containsPlayer(username)) {
					this.kick("You\'re banned!");
				} else {
					this.onlyIP = true;
					this.name = username;
					this.connection.sendPacket(new LoginPacket(PROTOCOL_VERSION, this.minecraft.serverName, this.minecraft.motd, this.minecraft.admins.containsPlayer(username) ? 100 : 0));
					byte[] levelData = this.minecraft.level.copyBlocks();
					(new MonitorBlocksThread(this, levelData)).start();
					this.minecraft.players.addPlayer(username);
				}
			}
		}
			
		if(!(packet instanceof TimedOutPacket) && this.onlyIP && this.sendingPackets) {
			if(packet instanceof PlaceOrRemoveTilePacket) {
				PlaceOrRemoveTilePacket pk = (PlaceOrRemoveTilePacket) packet;
				int x = pk.x;
				int y = pk.y;
				int z = pk.z;
				byte id = pk.id;
				EventSetTilePlayer event = new EventSetTilePlayer((short)x, (short)y, (short)z, id, this);
				ClassicBukkit.pluginManager.fireSetTile(event);
				
				if(event.isCancelled()) {
					ClassicBukkit.getServer().setTile(x, y, z);
					return;
				}

				x = event.getX();
				y = event.getY();
				z = event.getZ();
				id = event.getId();

				if(this.positionData.size() > 1200) {
					this.kickCheat("Too much lag");
				} else {
					this.positionData.add(new BlockData((short)x, (short)y, (short)z, id, pk.action));
				}
			} else if(packet instanceof ChatMessagePacket) {
				ChatMessagePacket pk = (ChatMessagePacket) packet;
				String msg = pk.message.trim();
				if(msg.length() > 0) this.chatMessage(msg);
			} else if(packet instanceof PlayerTeleportPacket) {
				if(this.positionData.size() > 1200) {
					this.kickCheat("Too much lag");
					return;
				}
				PlayerTeleportPacket pk = (PlayerTeleportPacket) packet;
				this.positionData.add(new MoveData(pk.x, pk.y, pk.z, pk.yaw, pk.pitch));
			}
		}
	}

	private void chatMessage(String msg) {
		msg = msg.trim();

		EventChatMessage event = new EventChatMessage(msg, this);
		ClassicBukkit.pluginManager.fireChatMessage(event);

		if(event.isCancelled()) {
			return;
		}

		this.chatCounter += msg.length() + 15 << 2;
		if(this.chatCounter > 600) {
			this.chatCounter = 760;
			this.sendPacket(new ChatMessagePacket("Too much chatter! Muted for eight seconds."));
			logger.info("Muting " + this.name + " for chatting too much");
		} else {
			char[] var2 = msg.toCharArray();

			for(int var3 = 0; var3 < var2.length; ++var3) {
				if(var2[var3] < 32 || var2[var3] > 127) {
					this.kickCheat("Bad chat message!");
					return;
				}
			}

			if(msg.startsWith("/")) {
				String[] parts = msg.split(" ");
				ClassicBukkit.commandManager.executeCommand(parts[0].substring(1), parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[] {}, this);
			} else {
				logger.info(this.name + " says: " + msg);
				this.minecraft.sendPacket(new ChatMessagePacket(this.playerID, this.name + ": " + msg));
			}
		}
	}

	public final void kick(String msg) {
		this.connection.sendPacket(new KickPlayerPacket(msg));
		logger.info("Kicking " + this + ": " + msg);
		this.minecraft.addTimer(this);
		this.ignorePackets = true;
	}

	private void kickCheat(String var1) {
		this.kick("Cheat detected: " + var1);
	}

	public final void sendChatMessage(String var1) {
		this.sendPacket(new ChatMessagePacket(0, var1));
	}

	public final void setBlocks(byte[] blocks) {
		this.blocks = blocks;
	}

	public final void handlePackets() {
		if(this.packetHandlingCounter >= 2) {
			this.packetHandlingCounter -= 2;
		}

		if(this.chatCounter > 0) {
			--this.chatCounter;
			if(this.chatCounter == 600) {
				this.sendPacket(new ChatMessagePacket("You can now talk again."));
				this.chatCounter = 300;
			}
		}

		PosData var2;
		boolean var26 = true; //XXX AAA
		if(this.positionData.size() > 0) {
			for(boolean var1 = true; this.positionData.size() > 0 && var1; var1 = var26) {
				var2 = this.positionData.remove(0);
				short var3;
				short var4;
				byte var5;
				byte var6;
				short var13;
				short var10001;
				short var10002;
				short var10003;
				byte var10004;
				if(var2 instanceof BlockData) { //posdata
					var10001 = var2.x;
					var10002 = var2.y;
					var10003 = var2.z;
					var10004 = ((BlockData) var2).action; //action
					var6 = ((BlockData) var2).id;
					var5 = var10004;
					var4 = var10003;
					var3 = var10002;
					var13 = var10001;
					++this.packetHandlingCounter;
					if(this.packetHandlingCounter == 100) {
						this.kickCheat("Too much clicking!");
					} else {
						Level var21 = this.minecraft.level;
						float var22 = (float)var13 - (float)this.x / 32.0F;
						float var24 = (float)var3 - ((float)this.y / 32.0F - 1.62F);
						float var25 = (float)var4 - (float)this.z / 32.0F;
						var22 = var22 * var22 + var24 * var24 + var25 * var25;
						var24 = 8.0F;
						if(var22 >= var24 * var24) {
							System.out.println("Distance: " + Math.sqrt((double)var22));
							this.kickCheat("Distance");
						} else {
							boolean var23 = User.creativeTiles.contains(Tile.tiles[var6]);
							if(!var23) {
								this.kickCheat("Tile type");
							} else if(var13 >= 0 && var3 >= 0 && var4 >= 0 && var13 < var21.width && var3 < var21.depth && var4 < var21.height) {
								if(var5 == 0) {
									if(var21.getTile(var13, var3, var4) != Tile.unbreakable.id || this.minecraft.admins.containsPlayer(this.name)) {
										var21.setTile(var13, var3, var4, 0);
									}
								} else {
									Tile var18 = Tile.tiles[var21.getTile(var13, var3, var4)];
									if(var18 == null || var18 == Tile.water || var18 == Tile.calmWater || var18 == Tile.lava || var18 == Tile.calmLava) {
										if(this.placeUnbreakable && var6 == Tile.rock.id) {
											var21.setTile(var13, var3, var4, Tile.unbreakable.id);
										} else {
											var21.setTile(var13, var3, var4, var6);
										}

										Tile.tiles[var6].onBlockAdded(var21, var13, var3, var4);
									}
								}
							}
						}
					}

					var26 = true;
				} else if(var2 instanceof MoveData) { //movedata
					var10001 = var2.x;
					var10002 = var2.y;
					var10003 = var2.z;
					var10004 = ((MoveData) var2).yaw;
					var6 = ((MoveData) var2).pitch;
					var5 = var10004;
					var4 = var10003;
					var3 = var10002;
					var13 = var10001;

					EventPlayerMovement event = new EventPlayerMovement(this);
					ClassicBukkit.pluginManager.firePlayerMovement(event);

					if(event.isCancelled()) {
						this.teleport(this.x/32, this.y/32, this.z/32, this.yaw, this.pitch);
						return;
					}

					if(var13 == this.x && var3 == this.y && var4 == this.z && var5 == this.yaw && var6 == this.pitch) {
						var26 = true;
					} else {
						boolean var7 = var13 == this.x && var3 == this.y && var4 == this.z;
						if(this.ticks++ % 2 == 0) {
							int var8 = var13 - this.x;
							int var9 = var3 - this.y;
							int var10 = var4 - this.z;
							if(var8 >= 128 || var8 < -128 || var9 >= 128 || var9 < -128 || var10 >= 128 || var10 < -128 || this.ticks % 20 <= 1) {
								this.x = var13;
								this.y = var3;
								this.z = var4;
								this.yaw = var5;
								this.pitch = var6;
								
								this.minecraft.sendPlayerPacket(this, new PlayerTeleportPacket((byte)this.playerID, var13, var3, var4, var5, var6));
								var26 = false;
								continue;
							}

							if(var13 == this.x && var3 == this.y && var4 == this.z) {
								this.yaw = var5;
								this.pitch = var6;
								this.minecraft.sendPlayerPacket(this, new PlayerRotatePacket((byte)this.playerID, var5, var6));
							} else if(var5 == this.yaw && var6 == this.pitch) {
								this.x = var13;
								this.y = var3;
								this.z = var4;
								this.minecraft.sendPlayerPacket(this, new PlayerMovePacket((byte)this.playerID, (byte)var8, (byte)var9, (byte)var10));
							} else {
								this.x = var13;
								this.y = var3;
								this.z = var4;
								this.yaw = var5;
								this.pitch = var6;
								this.minecraft.sendPlayerPacket(this, new PlayerMoveAndRotatePacket(
										(byte)this.playerID,
										(byte)var8, (byte)var9, (byte)var10,
										var5, var6
								));
								//{Integer.valueOf(this.playerID), Integer.valueOf(var8), Integer.valueOf(var9), Integer.valueOf(var10), Byte.valueOf(var5), Byte.valueOf(var6)});
							}
						}

						var26 = var7;
					}
				}
			}
		}

		if(!this.onlyIP && System.currentTimeMillis() - this.currentTime > 5000L) {
			this.kick("You need to log in!");
		} else if(this.blocks != null) {
			Level var11 = this.minecraft.level;
			byte[] dest = new byte[1024];
			int lastIndex = 0;
			int var17 = this.blocks.length;
			this.connection.sendPacket(new LevelInitializePacket());

			int length;
			while(var17 > 0) {
				length = var17;
				if(var17 > dest.length) {
					length = dest.length;
				}

				System.arraycopy(this.blocks, lastIndex, dest, 0, length);
				this.connection.sendPacket(new LevelDataChunkPacket((short)length, dest, (byte)((lastIndex + length) * 100 / this.blocks.length)));
				var17 -= length;
				lastIndex += length;
			}

			this.connection.sendPacket(new LevelFinalizePacket(var11));
			this.connection.sendPacket(new PlayerJoinPacket((byte)-1, this.name, (short)this.x, (short)this.y, (short)this.z, (byte)this.yaw, (byte)this.pitch));
			this.minecraft.sendPlayerPacket(this, 
				new PlayerJoinPacket((byte)this.playerID, this.name, 
					(short)((var11.xSpawn << 5) + 16), (short)((var11.ySpawn << 5) + 16), (short)((var11.zSpawn << 5) + 16), 
					((byte)(var11.rotSpawn * 256.0F / 360.0F)), (byte)0
				)
			);
			this.minecraft.sendPacket(new ChatMessagePacket(this.name + " joined the game"));
			ClassicBukkit.pluginManager.firePlayerJoin(new EventPlayerJoin(this));
			Iterator var20 = this.minecraft.getPlayerList().iterator();

			while(var20.hasNext()) {
				PlayerInstance var12 = (PlayerInstance)var20.next();
				if(var12 != null && var12 != this && var12.onlyIP) {
					this.connection.sendPacket(new PlayerJoinPacket(var12));
				}
			}

			this.sendingPackets = true;
			length = 0;

			while(length < this.packets.size()) {
				Packet var14 = (Packet)this.packets.get(length++);
				this.sendPacket(var14);
			}

			this.packets = null;
			this.blocks = null;
		}
	}

	public final void sendPacket(Packet var1) {
		if(!this.sendingPackets) {
			this.packets.add(var1);
		} else {
			this.connection.sendPacket(var1);
		}
	}

	public final void handleException(Exception var1) {
		if(var1 instanceof IOException) {
			ClassicBukkit.pluginManager.firePlayerQuit(new EventPlayerQuit(this));
			logger.info(this + " lost connection suddenly. (" + var1 + ")");
			var1.printStackTrace();
		} else {
			logger.warning(this + ":" + var1);
			logger.log(java.util.logging.Level.WARNING, "Exception handling " + this + "!", var1);
			var1.printStackTrace();
		}

		this.minecraft.sendPlayerPacket(this, new ChatMessagePacket(this.name + " left the game"));
		MinecraftServer.shutdown(this);
	}

	public void teleport(int x, int y, int z) {
		this.sendPacket(new PlayerTeleportPacket((byte)-1, (short)(x*32), (short)(y*32), (short)(z*32), (byte)0, (byte)0));
	}

	public void teleport(int x, int y, int z, int yaw, int pitch) {
		this.sendPacket(new PlayerTeleportPacket((byte)-1, (short)(x*32), (short)(y*32), (short)(z*32), (byte)yaw, (byte)pitch));
	}
}
