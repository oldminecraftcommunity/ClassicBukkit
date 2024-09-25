package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.packets.*;

public abstract class Packet {
	
	public enum PacketID{
		LOGIN,
		TIMED_OUT,
		LEVEL_INITIALIZE,
		LEVEL_DATA_CHUNK,
		LEVEL_FINALIZE,
		PLACE_OR_REMOVE_TILE,
		SET_TILE,
		PLAYER_JOIN,
		PLAYER_TELEPORT,
		PLAYER_MOVE_AND_ROTATE,
		PLAYER_MOVE,
		PLAYER_ROTATE,
		PLAYER_DISCONNECT,
		CHAT_MESSAGE,
		KICK_PLAYER;
	}
	
	/*public static final Packet[] PACKETS = new Packet[256];
	public static final Packet LOGIN = new Packet(new Class[]{Byte.TYPE, String.class, String.class, Byte.TYPE});
	public static final Packet TIMED_OUT = new Packet(new Class[0]);
	public static final Packet LEVEL_INITIALIZE = new Packet(new Class[0]);
	public static final Packet LEVEL_DATA_CHUNK = new Packet(new Class[]{Short.TYPE, byte[].class, Byte.TYPE});
	public static final Packet LEVEL_FINALIZE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE});
	public static final Packet PLACE_OR_REMOVE_TILE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet SET_TILE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE});
	public static final Packet PLAYER_JOIN = new Packet(new Class[]{Byte.TYPE, String.class, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_TELEPORT = new Packet(new Class[]{Byte.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_MOVE_AND_ROTATE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_MOVE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_ROTATE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_DISCONNECT = new Packet(new Class[]{Byte.TYPE});
	public static final Packet CHAT_MESSAGE = new Packet(new Class[]{Byte.TYPE, String.class});
	public static final Packet KICK_PLAYER = new Packet(new Class[]{String.class});
	public final int size;*/
	public int size = -1;
	//public Class[] fields;

	public Packet() {
		//if(!(this instanceof TimedOutPacket)) System.out.println("Created new "+this.getClass().getName());
	}
	
	public abstract PacketID getPacketID();
	public abstract void read(SocketConnection con);
	public abstract void write(SocketConnection con);
	public static Packet create(PacketID pid) {
		switch(pid) {
			case LOGIN:
				return new LoginPacket();
			case TIMED_OUT:
				return new TimedOutPacket();
			case LEVEL_INITIALIZE:
				return new LevelInitializePacket();
			case LEVEL_DATA_CHUNK:
				return new LevelDataChunkPacket();
			case LEVEL_FINALIZE:
				return new LevelFinalizePacket();
			case PLACE_OR_REMOVE_TILE:
				return new PlaceOrRemoveTilePacket();
			case SET_TILE:
				return new SetTilePacket();
			case PLAYER_JOIN:
				return new PlayerJoinPacket();
			case PLAYER_TELEPORT:
				return new PlayerTeleportPacket();
			case PLAYER_MOVE_AND_ROTATE:
				return new PlayerMoveAndRotatePacket();
			case PLAYER_MOVE:
				return new PlayerMovePacket();
			case PLAYER_ROTATE:
				return new PlayerRotatePacket();
			case PLAYER_DISCONNECT:
				return new PlayerDisconnectPacket();
			case CHAT_MESSAGE:
				return new ChatMessagePacket();
			case KICK_PLAYER:
				return new KickPlayerPacket();
			default:
				return null;
		}
	}
	public static Packet create(int pid) {
		PacketID id = pid >= PacketID.values().length ? null : PacketID.values()[pid];
		return create(id);
	}
	
}
