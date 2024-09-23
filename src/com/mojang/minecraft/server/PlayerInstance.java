package com.mojang.minecraft.server;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.User;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.Packet;
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
	private ArrayList packets = new ArrayList();
	private long currentTime;
	private List placedBlocks = new ArrayList();
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
		SocketConnection var1;
		if(!this.onlyIP) {
			var1 = this.connection;
			return var1.ip;
		} else {
			StringBuilder var10000 = (new StringBuilder()).append(this.name).append(" (");
			var1 = this.connection;
			return var10000.append(var1.ip).append(")").toString();
		}
	}

	public final void handlePackets(Packet var1, Object[] var2) {
		if(!this.ignorePackets) {
			if(var1 != Packet.LOGIN) {
				if(var1 != Packet.TIMED_OUT) {
					if(this.onlyIP && this.sendingPackets) {
						if(var1 == Packet.PLACE_OR_REMOVE_TILE) {
							EventSetTilePlayer event = new EventSetTilePlayer((short)var2[0], (short)var2[1], (short)var2[2], (byte)var2[4], this);
							ClassicBukkit.pluginManager.fireSetTile(event);

							if(event.isCancelled()) {
								ClassicBukkit.getServer().setTile(((Short)var2[0]).intValue(), ((Short)var2[1]).intValue(), ((Short)var2[2]).intValue());
								return;
							}

							var2[0] = event.getX();
							var2[1] = event.getY();
							var2[2] = event.getZ();
							var2[4] = event.getId();

							if(this.placedBlocks.size() > 1200) {
								this.kickCheat("Too much lag");
							} else {
								this.placedBlocks.add(var2);
							}
						} else if(var1 == Packet.CHAT_MESSAGE) {
							String var7 = var2[1].toString().trim();
							if(var7.length() > 0) {
								this.chatMessage(var7);
							}

						} else {
							if(var1 == Packet.PLAYER_TELEPORT) {
								if(this.placedBlocks.size() > 1200) {
									this.kickCheat("Too much lag");
									return;
								}

								this.placedBlocks.add(var2);
							}

						}
					}
				}
			} else {
				byte var6 = ((Byte)var2[0]).byteValue();
				String var3 = ((String)var2[1]).trim();
				String var8 = (String)var2[2];
				char[] var4 = var3.toCharArray();

				for(int var5 = 0; var5 < var4.length; ++var5) {
					if(var4[var5] < 32 || var4[var5] > 127) {
						this.kickCheat("Bad name!");
						return;
					}
				}

				if(this.minecraft.verifyNames && !var8.equals(this.minecraft.mpPassCalculator.calcMPass(var3))) {
					this.kick("Illegal name.");
				} else {
					PlayerInstance var11 = this.minecraft.getPlayerByName(var3);
					if(var11 != null) {
						this.kick("Player with same nick already online.");
					}

					logger.info(this + " logged in as " + var3);
					if(var6 != 6) {
						this.kick("Wrong protocol version.");
					} else if(this.minecraft.banned.containsPlayer(var3)) {
						this.kick("You\'re banned!");
					} else {
						this.onlyIP = true;
						this.name = var3;
						this.connection.sendPacket(Packet.LOGIN, new Object[]{Byte.valueOf((byte)6), this.minecraft.serverName, this.minecraft.motd, Integer.valueOf(this.minecraft.admins.containsPlayer(var3) ? 100 : 0)});
						Level var9 = this.minecraft.level;
						byte[] var10 = var9.copyBlocks();
						(new MonitorBlocksThread(this, var10)).start();
						this.minecraft.players.addPlayer(var3);
					}
				}
			}
		}
	}

	private void chatMessage(String var1) {
		var1 = var1.trim();

		EventChatMessage event = new EventChatMessage(var1, this);
		ClassicBukkit.pluginManager.fireChatMessage(event);

		if(event.isCancelled()) {
			return;
		}

		this.chatCounter += var1.length() + 15 << 2;
		if(this.chatCounter > 600) {
			this.chatCounter = 760;
			this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), "Too much chatter! Muted for eight seconds."});
			logger.info("Muting " + this.name + " for chatting too much");
		} else {
			char[] var2 = var1.toCharArray();

			for(int var3 = 0; var3 < var2.length; ++var3) {
				if(var2[var3] < 32 || var2[var3] > 127) {
					this.kickCheat("Bad chat message!");
					return;
				}
			}

			if(var1.startsWith("/")) {
				String[] parts = var1.split(" ");
				ClassicBukkit.commandManager.executeCommand(parts[0].substring(1), parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[] {}, this);
			} else {
				logger.info(this.name + " says: " + var1);
				this.minecraft.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(this.playerID), this.name + ": " + var1});
			}
		}
	}

	public final void kick(String var1) {
		this.connection.sendPacket(Packet.KICK_PLAYER, new Object[]{var1});
		logger.info("Kicking " + this + ": " + var1);
		this.minecraft.addTimer(this);
		this.ignorePackets = true;
	}

	private void kickCheat(String var1) {
		this.kick("Cheat detected: " + var1);
	}

	public final void sendChatMessage(String var1) {
		this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{0, var1});
	}

	public final void setBlocks(byte[] var1) {
		this.blocks = var1;
	}

	public final void handlePackets() {
		if(this.packetHandlingCounter >= 2) {
			this.packetHandlingCounter -= 2;
		}

		if(this.chatCounter > 0) {
			--this.chatCounter;
			if(this.chatCounter == 600) {
				this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), "You can now talk again."});
				this.chatCounter = 300;
			}
		}

		Object[] var2;
		boolean var26;
		if(this.placedBlocks.size() > 0) {
			for(boolean var1 = true; this.placedBlocks.size() > 0 && var1; var1 = var26) {
				var2 = (Object[])this.placedBlocks.remove(0);
				short var3;
				short var4;
				byte var5;
				byte var6;
				short var13;
				short var10001;
				short var10002;
				short var10003;
				byte var10004;
				if(var2[0] instanceof Short) {
					var10001 = ((Short)var2[0]).shortValue();
					var10002 = ((Short)var2[1]).shortValue();
					var10003 = ((Short)var2[2]).shortValue();
					var10004 = ((Byte)var2[3]).byteValue();
					var6 = ((Byte)var2[4]).byteValue();
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
				} else {
					((Byte)var2[0]).byteValue();
					var10001 = ((Short)var2[1]).shortValue();
					var10002 = ((Short)var2[2]).shortValue();
					var10003 = ((Short)var2[3]).shortValue();
					var10004 = ((Byte)var2[4]).byteValue();
					var6 = ((Byte)var2[5]).byteValue();
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
								this.minecraft.sendPlayerPacket(this, Packet.PLAYER_TELEPORT, new Object[]{Integer.valueOf(this.playerID), Short.valueOf(var13), Short.valueOf(var3), Short.valueOf(var4), Byte.valueOf(var5), Byte.valueOf(var6)});
								var26 = false;
								continue;
							}

							if(var13 == this.x && var3 == this.y && var4 == this.z) {
								this.yaw = var5;
								this.pitch = var6;
								this.minecraft.sendPlayerPacket(this, Packet.PLAYER_ROTATE, new Object[]{Integer.valueOf(this.playerID), Byte.valueOf(var5), Byte.valueOf(var6)});
							} else if(var5 == this.yaw && var6 == this.pitch) {
								this.x = var13;
								this.y = var3;
								this.z = var4;
								this.minecraft.sendPlayerPacket(this, Packet.PLAYER_MOVE, new Object[]{Integer.valueOf(this.playerID), Integer.valueOf(var8), Integer.valueOf(var9), Integer.valueOf(var10)});
							} else {
								this.x = var13;
								this.y = var3;
								this.z = var4;
								this.yaw = var5;
								this.pitch = var6;
								this.minecraft.sendPlayerPacket(this, Packet.PLAYER_MOVE_AND_ROTATE, new Object[]{Integer.valueOf(this.playerID), Integer.valueOf(var8), Integer.valueOf(var9), Integer.valueOf(var10), Byte.valueOf(var5), Byte.valueOf(var6)});
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
			byte[] var15 = new byte[1024];
			int var16 = 0;
			int var17 = this.blocks.length;
			this.connection.sendPacket(Packet.LEVEL_INITIALIZE, new Object[0]);

			int var19;
			while(var17 > 0) {
				var19 = var17;
				if(var17 > var15.length) {
					var19 = var15.length;
				}

				System.arraycopy(this.blocks, var16, var15, 0, var19);
				this.connection.sendPacket(Packet.LEVEL_DATA_CHUNK, new Object[]{Integer.valueOf(var19), var15, Integer.valueOf((var16 + var19) * 100 / this.blocks.length)});
				var17 -= var19;
				var16 += var19;
			}

			this.connection.sendPacket(Packet.LEVEL_FINALIZE, new Object[]{Integer.valueOf(var11.width), Integer.valueOf(var11.depth), Integer.valueOf(var11.height)});
			this.connection.sendPacket(Packet.PLAYER_JOIN, new Object[]{Integer.valueOf(-1), this.name, Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), Integer.valueOf(this.yaw), Integer.valueOf(this.pitch)});
			this.minecraft.sendPlayerPacket(this, Packet.PLAYER_JOIN, new Object[]{Integer.valueOf(this.playerID), this.name, Integer.valueOf((var11.xSpawn << 5) + 16), Integer.valueOf((var11.ySpawn << 5) + 16), Integer.valueOf((var11.zSpawn << 5) + 16), Integer.valueOf((int)(var11.rotSpawn * 256.0F / 360.0F)), Integer.valueOf(0)});
			this.minecraft.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), this.name + " joined the game"});
			ClassicBukkit.pluginManager.firePlayerJoin(new EventPlayerJoin(this));
			Iterator var20 = this.minecraft.getPlayerList().iterator();

			while(var20.hasNext()) {
				PlayerInstance var12 = (PlayerInstance)var20.next();
				if(var12 != null && var12 != this && var12.onlyIP) {
					this.connection.sendPacket(Packet.PLAYER_JOIN, new Object[]{Integer.valueOf(var12.playerID), var12.name, Integer.valueOf(var12.x), Integer.valueOf(var12.y), Integer.valueOf(var12.z), Integer.valueOf(var12.yaw), Integer.valueOf(var12.pitch)});
				}
			}

			this.sendingPackets = true;
			var19 = 0;

			while(var19 < this.packets.size()) {
				Packet var14 = (Packet)this.packets.get(var19++);
				var2 = (Object[])((Object[])this.packets.get(var19++));
				this.sendPacket(var14, var2);
			}

			this.packets = null;
			this.blocks = null;
		}
	}

	public final void sendPacket(Packet var1, Object... var2) {
		if(!this.sendingPackets) {
			this.packets.add(var1);
			this.packets.add(var2);
		} else {
			this.connection.sendPacket(var1, var2);
		}
	}

	public final void handleException(Exception var1) {
		if(var1 instanceof IOException) {
			ClassicBukkit.pluginManager.firePlayerQuit(new EventPlayerQuit(this));
			logger.info(this + " lost connection suddenly. (" + var1 + ")");
		} else {
			logger.warning(this + ":" + var1);
			logger.log(java.util.logging.Level.WARNING, "Exception handling " + this + "!", var1);
			var1.printStackTrace();
		}

		this.minecraft.sendPlayerPacket(this, Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), this.name + " left the game"});
		MinecraftServer.shutdown(this);
	}

	public void teleport(int x, int y, int z) {
		this.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{-1, x*32, y*32, z*32, 0, 0});
	}

	public void teleport(int x, int y, int z, int yaw, int pitch) {
		this.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{-1, x*32, y*32, z*32, yaw, pitch});
	}
}
