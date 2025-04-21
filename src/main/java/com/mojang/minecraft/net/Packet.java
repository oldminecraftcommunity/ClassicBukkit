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
	
	public int size = -1;

	public Packet() {}
	
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
