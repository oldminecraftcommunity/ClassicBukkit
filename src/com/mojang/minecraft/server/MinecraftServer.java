package com.mojang.minecraft.server;

import com.mojang.comm.ConnectionList;
import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.net.packets.ChatMessagePacket;
import com.mojang.minecraft.net.packets.KickPlayerPacket;
import com.mojang.minecraft.net.packets.PlayerDisconnectPacket;
import com.mojang.minecraft.net.packets.SetTilePacket;
import com.mojang.minecraft.net.packets.TimedOutPacket;

import met.realfreehij.classicbukkit.ClassicBukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MinecraftServer implements Runnable {
	public static Logger logger = Logger.getLogger("MinecraftServer"); //public
	static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private ConnectionList connectionList;
	private Map<SocketConnection, PlayerInstance> playerInstancesMap = new HashMap<>();
	private List<PlayerInstance> playerList = new ArrayList<>();
	private List<SendTimer> sendTimers = new ArrayList<>();
	private int maxPlayers;
	private Properties properties = new Properties();
	public Level level;
	private boolean isPublic = false;
	public String serverName;
	public String motd;
	private int port;
	private PlayerInstance[] playerInstances;
	public PlayerList admins = new PlayerList("Admins", new File("admins.txt"));
	public PlayerList banned = new PlayerList("Banned", new File("banned.txt"));
	private PlayerList bannedIP = new PlayerList("Banned (IP)", new File("banned-ip.txt"));
	private List playerList2 = new ArrayList();
	private String salt = "" + (new Random()).nextLong();
	private String serverURL = "";
	public MPPassCalculator mpPassCalculator = new MPPassCalculator(this.salt);
	public boolean verifyNames = false;
	private int maxConnectCount;

	public MinecraftServer() throws IOException {
		try {
			this.properties.load(new FileReader("server.properties"));
		} catch (Exception var3) {
			logger.warning("Failed to load server.properties!");
		}

		try {
			this.serverName = this.properties.getProperty("server-name", "Minecraft Server");
			this.motd = this.properties.getProperty("motd", "Welcome to my Minecraft Server!");
			this.port = Integer.parseInt(this.properties.getProperty("port", "25565"));
			this.maxPlayers = Integer.parseInt(this.properties.getProperty("max-players", "16"));
			this.isPublic = Boolean.parseBoolean(this.properties.getProperty("public", "true"));
			this.verifyNames = Boolean.parseBoolean(this.properties.getProperty("verify-names", "true"));
			if(this.maxPlayers < 1) {
				this.maxPlayers = 1;
			}

			if(this.maxPlayers > 127) {
				this.maxPlayers = 127;
			}

			this.maxConnectCount = Integer.parseInt(this.properties.getProperty("max-connections", "3"));
			this.properties.setProperty("server-name", this.serverName);
			this.properties.setProperty("motd", this.motd);
			this.properties.setProperty("max-players", "" + this.maxPlayers);
			this.properties.setProperty("port", "" + this.port);
			this.properties.setProperty("public", "" + this.isPublic);
			this.properties.setProperty("verify-names", "" + this.verifyNames);
			this.properties.setProperty("max-connections", "3");
		} catch (Exception var2) {
			logger.warning("server.properties is broken! Delete it or fix it!");
			System.exit(0);
		}

		try {
			this.properties.store(new FileWriter("server.properties"), "Minecraft server properties");
		} catch (Exception var1) {
			logger.warning("Failed to save server.properties!");
		}

		this.playerInstances = new PlayerInstance[this.maxPlayers];
		this.connectionList = new ConnectionList(this.port, this);
		(new ConsoleInput(this)).start();
	}

	public final void disconnect(SocketConnection var1) {
		PlayerInstance player = this.playerInstancesMap.get(var1);
		if(player != null) {
			logger.info(player + " disconnected");
			this.playerInstancesMap.remove(player.connection);
			this.playerList.remove(player);
			if(player.playerID >= 0) {
				this.playerInstances[player.playerID] = null;
			}

			this.sendPacket(new PlayerDisconnectPacket((byte)player.playerID));
		}

	}

	private void addTimer(SocketConnection var1) {
		this.sendTimers.add(new SendTimer(var1, 100));
	}

	public final void addTimer(PlayerInstance var1) {
		this.sendTimers.add(new SendTimer(var1.connection, 100));
	}

	public static void shutdown(PlayerInstance var0) {
		var0.connection.disconnect();
	}

	public final void sendPacket(Packet pk) {
		for(int i = 0; i < this.playerList.size(); ++i) {
			try {
				this.playerList.get(i).sendPacket(pk);
			} catch (Exception e) {
				this.playerList.get(i).handleException(e);
			}
		}

	}
	/**
	 * Sends packet to everyone except player
	 * @param player
	 * @param pk
	 * @param args
	 */
	public final void sendPlayerPacket(PlayerInstance player, Packet pk) {
		for(int i = 0; i < this.playerList.size(); ++i) {
			if(this.playerList.get(i) != player) {
				try {
					this.playerList.get(i).sendPacket(pk);
				} catch (Exception e) {
					this.playerList.get(i).handleException(e);
				}
			}
		}

	}

	public void run() {
		logger.info("Now accepting input on " + this.port);
		int var1 = 50000000;
		int var2 = 500000000;

		try {
			long var3 = System.nanoTime();
			long var5 = System.nanoTime();
			int var7 = 0;

			while(true) {
				this.socketServer();

				for(; System.nanoTime() - var5 > (long)var1; ++var7) {
					var5 += (long)var1;
					this.tickLevel();
					if(var7 % 1200 == 0) {
						MinecraftServer var8 = this;

						try {
							new LevelIO(var8);
							LevelIO.save(var8.level, new FileOutputStream("server_level.dat"));
						} catch (Exception var10) {
							logger.severe("Failed to save the level! " + var10);
						}

						logger.info("Level saved! Load: " + this.playerList.size() + "/" + this.maxPlayers);
					}

					if(var7 % 900 == 0) {
						HashMap<String, Object> var9 = new HashMap<>();
						var9.put("name", this.serverName);
						var9.put("users", Integer.valueOf(this.playerList.size()));
						var9.put("max", Integer.valueOf(this.maxPlayers));
						var9.put("public", Boolean.valueOf(this.isPublic));
						var9.put("port", Integer.valueOf(this.port));
						var9.put("salt", this.salt);
						var9.put("version", Byte.valueOf((byte)6));
						String var12 = assembleHeartbeat(var9);
						(new HeartbeatThread(this, var12)).start();
					}
				}

				while(System.nanoTime() - var3 > (long)var2) {
					var3 += (long)var2;
					this.sendPacket(new TimedOutPacket());
				}

				Thread.sleep(5L);
			}
		} catch (Exception var11) {
			logger.log(java.util.logging.Level.SEVERE, "Error in main loop, server shutting down!", var11);
			var11.printStackTrace();
		}
	}

	private static String assembleHeartbeat(Map<String, Object> var0) {
		try {
			String var1 = "";

			String var3;
			for(Iterator var2 = var0.keySet().iterator(); var2.hasNext(); var1 = var1 + var3 + "=" + URLEncoder.encode(var0.get(var3).toString(), "UTF-8")) {
				var3 = (String)var2.next();
				if(var1 != "") {
					var1 = var1 + "&";
				}
			}

			return var1;
		} catch (Exception var4) {
			var4.printStackTrace();
			throw new RuntimeException("Failed to assemble heartbeat! This is pretty fatal");
		}
	}

	private void tickLevel() {
		Iterator var1 = this.playerList.iterator();

		while(var1.hasNext()) {
			PlayerInstance var2 = (PlayerInstance)var1.next();

			try {
				var2.handlePackets();
			} catch (Exception var8) {
				var2.handleException(var8);
			}
		}

		this.level.tick();

		for(int var9 = 0; var9 < this.sendTimers.size(); ++var9) {
			SendTimer var10 = (SendTimer)this.sendTimers.get(var9);
			this.disconnect(var10.netHandler);

			try {
				SocketConnection var3 = var10.netHandler;

				try {
					if(var3.writeBuffer.position() > 0) {
						var3.writeBuffer.flip();
						var3.socketChannel.write(var3.writeBuffer);
						var3.writeBuffer.compact();
					}
				} catch (IOException var6) {
				}

				if(var10.timer-- <= 0) {
					try {
						var10.netHandler.disconnect();
					} catch (Exception var5) {
					}

					this.sendTimers.remove(var9--);
				}
			} catch (Exception var7) {
				try {
					var10.netHandler.disconnect();
				} catch (Exception var4) {
				}
			}
		}

	}

	public final void beginLevelLoading(String var1) {
		logger.info(var1);
	}

	public final void levelLoadUpdate(String var1) {
		logger.fine(var1);
	}

	private void socketServer() {
		List var1 = this.playerList2;
		synchronized(var1) {
			while(this.playerList2.size() > 0) {
				//this.parseCommand((PlayerInstance)null, (String)this.playerList2.remove(0));
			}
		}

		try {
			ConnectionList var13 = this.connectionList;

			while(true) {
				SocketChannel var14 = var13.serverSocketChannel.accept();
				MinecraftServer server;
				if(var14 == null) {
					for(int var17 = 0; var17 < var13.connectionList.size(); ++var17) {
						SocketConnection con = var13.connectionList.get(var17);

						try {
							con.socketChannel.read(con.readBuffer);
							int var19 = 0;

							while(con.readBuffer.position() > 0 && var19++ != 100) {
								con.readBuffer.flip();
								byte var20 = con.readBuffer.get(0);
								Packet packet = Packet.create(var20);
								if(packet == null) {
									throw new IOException("Bad command: " + var20);
								}

								if(con.readBuffer.remaining() < packet.size + 1) {
									con.readBuffer.compact();
									break;
								}

								con.readBuffer.get(); //get pid
								packet.read(con);
								
								con.player.handlePacket(packet);
								
								if(!con.connected) {
									break;
								}

								con.readBuffer.compact();
							}

							if(con.writeBuffer.position() > 0) {
								con.writeBuffer.flip();
								con.socketChannel.write(con.writeBuffer);
								con.writeBuffer.compact();
							}
						} catch (Exception var9) {
							server = var13.minecraft;
							PlayerInstance var23 = (PlayerInstance)server.playerInstancesMap.get(con);
							if(var23 != null) {
								var23.handleException(var9);
							}
						}

						try {
							if(!con.connected) {
								con.disconnect();
								var13.minecraft.disconnect(con);
								var13.connectionList.remove(var17--);
							}
						} catch (Exception var8) {
							var8.printStackTrace();
						}
					}

					return;
				}

				try {
					var14.configureBlocking(false);
					SocketConnection var2 = new SocketConnection(var14);
					var13.connectionList.add(var2);
					SocketConnection con = var2;
					server = var13.minecraft;
					if(server.bannedIP.containsPlayer(var2.ip)) {
						var2.sendPacket(new KickPlayerPacket("You\'re banned!"));
						logger.info(var2.ip + " tried to connect, but is banned.");
						server.addTimer(var2);
					} else {
						int var5 = 0;
						
						for(PlayerInstance player : server.playerList) {
							var2 = player.connection;
							if(var2.ip.equals(con.ip)) {
								++var5;
							}
						}

						if(var5 >= server.maxConnectCount) {
							con.sendPacket(new KickPlayerPacket("Too many connection!"));
							logger.info(con.ip + " tried to connect, but is already connected " + var5 + " times.");
							server.addTimer(con);
						} else {
							int slot = server.freePlayerSlots();
							if(slot < 0) {
								con.sendPacket(new KickPlayerPacket("The server is full!"));
								logger.info(con.ip + " tried to connect, but failed because the server was full.");
								server.addTimer(con);
							} else {
								PlayerInstance player = new PlayerInstance(server, con, slot);
								logger.info(player + " connected");
								server.playerInstancesMap.put(con, player);
								server.playerList.add(player);
								if(player.playerID >= 0) {
									server.playerInstances[player.playerID] = player;
								}
							}
						}
					}
				} catch (IOException var10) {
					var14.close();
					throw var10;
				}
			}
		} catch (IOException var11) {
			throw new RuntimeException("IOException while ticking socketserver", var11);
		}
	}

	/*public final void parseCommand(PlayerInstance var1, String var2) {
		while(var2.startsWith("/")) {
			var2 = var2.substring(1);
		}

		logger.info((var1 == null ? "[console]" : var1.name) + " admins: " + var2);
		String[] var3 = var2.split(" ");
		if(var3[0].toLowerCase().equals("ban") && var3.length > 1) {
			this.ban(var3[1]);
		} else if(var3[0].toLowerCase().equals("kick") && var3.length > 1) {
			this.kick(var3[1]);
		} else if(var3[0].toLowerCase().equals("banip") && var3.length > 1) {
			this.banip(var3[1]);
		} else if(var3[0].toLowerCase().equals("unban") && var3.length > 1) {
			String var5 = var3[1];
			this.banned.removePlayer(var5);
		} else if(var3[0].toLowerCase().equals("op") && var3.length > 1) {
			this.op(var3[1]);
		} else if(var3[0].toLowerCase().equals("deop") && var3.length > 1) {
			this.deop(var3[1]);
		} else if(var3[0].toLowerCase().equals("setspawn")) {
			if(var1 != null) {
				this.level.setSpawnPos(var1.x / 32, var1.y / 32, var1.z / 32, (float)(var1.yaw * 320 / 256));
			} else {
				logger.info("Can\'t set spawn from console!");
			}
		} else {
			if(var3[0].toLowerCase().equals("solid")) {
				if(var1 != null) {
					var1.placeUnbreakable = !var1.placeUnbreakable;
					if(var1.placeUnbreakable) {
						var1.sendChatMessage("Now placing unbreakable stone");
						return;
					}

					var1.sendChatMessage("Now placing normal stone");
					return;
				}
			} else {
				if(var3[0].toLowerCase().equals("broadcast") && var3.length > 1) {
					this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), var2.substring("broadcast ".length()).trim()});
					return;
				}

				if(var3[0].toLowerCase().equals("say") && var3.length > 1) {
					this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), var2.substring("say ".length()).trim()});
					return;
				}

				if((var3[0].toLowerCase().equals("teleport") || var3[0].toLowerCase().equals("tp")) && var3.length > 1) {
					if(var1 == null) {
						logger.info("Can\'t teleport from console!");
						return;
					}

					PlayerInstance var4 = this.getPlayerByName(var3[1]);
					if(var4 == null) {
						var1.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), "No such player"});
						return;
					}

					var1.connection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{Integer.valueOf(-1), Integer.valueOf(var4.x), Integer.valueOf(var4.y), Integer.valueOf(var4.z), Integer.valueOf(var4.yaw), Integer.valueOf(var4.pitch)});
				} else if(var1 != null) {
					var1.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), "Unknown command!"});
				}
			}

		}
	}*/

	public final void setTile(int x, int y, int z) {
		this.sendPacket(new SetTilePacket((short)x, (short)y, (short)z, (byte)this.level.getTile(x, y, z)));
	}

	private int freePlayerSlots() {
		for(int var1 = 0; var1 < this.maxPlayers; ++var1) {
			if(this.playerInstances[var1] == null) {
				return var1;
			}
		}

		return -1;
	}

	public final List<PlayerInstance> getPlayerList() {
		return this.playerList;
	}

	/*private void kick(String var1) {
		boolean var2 = false;
		Iterator var3 = this.playerList.iterator();

		while(var3.hasNext()) {
			PlayerInstance var4 = (PlayerInstance)var3.next();
			if(var4.name.equalsIgnoreCase(var1)) {
				var2 = true;
				var4.kick("You were kicked");
			}
		}

		if(var2) {
			this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), var1 + " got kicked from the server!"});
		}

	}

	private void ban(String var1) {
		this.banned.addPlayer(var1);
		boolean var2 = false;
		Iterator var3 = this.playerList.iterator();

		while(var3.hasNext()) {
			PlayerInstance var4 = (PlayerInstance)var3.next();
			if(var4.name.equalsIgnoreCase(var1)) {
				var2 = true;
				var4.kick("You were banned");
			}
		}

		if(var2) {
			this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), var1 + " got banned!"});
		}

	}*/

	private void op(String var1) {
		this.admins.addPlayer(var1);
		Iterator var3 = this.playerList.iterator();

		while(var3.hasNext()) {
			PlayerInstance var2 = (PlayerInstance)var3.next();
			if(var2.name.equalsIgnoreCase(var1)) {
				var2.sendChatMessage("You\'re now op!");
			}
		}

	}

	private void deop(String var1) {
		this.admins.removePlayer(var1);
		Iterator var3 = this.playerList.iterator();

		while(var3.hasNext()) {
			PlayerInstance var2 = (PlayerInstance)var3.next();
			if(var2.name.equalsIgnoreCase(var1)) {
				var2.sendChatMessage("You\'re no longer op!");
			}
		}

	}

	private void banip(String var1) {
		boolean var2 = false;
		String var3 = "";
		Iterator var4 = this.playerList.iterator();

		while(true) {
			PlayerInstance var5;
			SocketConnection var6;
			do {
				if(!var4.hasNext()) {
					if(var2) this.sendPacket(new ChatMessagePacket(var3 + " got ip banned!"));

					return;
				}

				var5 = (PlayerInstance)var4.next();
				if(var5.name.equalsIgnoreCase(var1)) {
					break;
				}

				var6 = var5.connection;
				if(var6.ip.equalsIgnoreCase(var1)) {
					break;
				}

				var6 = var5.connection;
			} while(!var6.ip.equalsIgnoreCase("/" + var1));

			var6 = var5.connection;
			this.bannedIP.addPlayer(var6.ip);
			var5.kick("You were banned");
			if(var3 == "") {
				var3 = var3 + ", ";
			}

			var3 = var3 + var5.name;
			var2 = true;
		}
	}

	public final PlayerInstance getPlayerByName(String var1) {
		Iterator var3 = this.playerList.iterator();

		PlayerInstance var2;
		do {
			if(!var3.hasNext()) {
				return null;
			}

			var2 = (PlayerInstance)var3.next();
		} while(!var2.name.equalsIgnoreCase(var1));

		return var2;
	}

	public static void main(String[] args) {
		try {
			MinecraftServer var6 = new MinecraftServer();
			MinecraftServer var1 = var6;
			logger.info("Setting up");
			File var2 = new File("server_level.dat");
			if(var2.exists()) {
				try {
					var1.level = (new LevelIO(var1)).load(new FileInputStream(var2));
				} catch (Exception var4) {
					logger.warning("Failed to load level. Generating a new level");
					var4.printStackTrace();
				}
			} else {
				logger.warning("No level file found. Generating a new level");
			}

			if(var6.level == null) {
				var6.level = (new LevelGen(var6)).generateLevel("--", 256, 256, 64);
			}

			try {
				new LevelIO(var1);
				LevelIO.save(var1.level, new FileOutputStream("server_level.dat"));
			} catch (Exception var3) {
			}

			var6.level.addListener(var6);
			Thread var7 = new Thread(var6);
			var7.start();

			new ClassicBukkit(var6);
		} catch (Exception var5) {
			logger.severe("Failed to start the server!");
			var5.printStackTrace();
		}
	}

	static List a(MinecraftServer var0) {
		return var0.playerList2;
	}

	static String getServerURL(MinecraftServer server) {
		return server.serverURL;
	}

	static String setServerURL(MinecraftServer server, String url) {
		return server.serverURL = url;
	}

	static {
		LogFormatter var0 = new LogFormatter();
		Handler[] var1 = logger.getParent().getHandlers();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			Handler var4 = var1[var3];
			logger.getParent().removeHandler(var4);
		}

		ConsoleHandler var6 = new ConsoleHandler();
		var6.setFormatter(var0);
		logger.addHandler(var6);

		try {
			LogHandler var7 = new LogHandler(new FileOutputStream("server.log"), var0);
			var7.setFormatter(var0);
			logger.addHandler(var7);
		} catch (Exception var5) {
			logger.warning("Failed to open file server.log for writing: " + var5);
		}
	}
}
