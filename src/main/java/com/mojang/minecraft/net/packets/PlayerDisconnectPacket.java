package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class PlayerDisconnectPacket extends Packet{
	public byte playerID;
	
	public PlayerDisconnectPacket() {
		super();
		this.size = 1;
	}
	
	public PlayerDisconnectPacket(byte playerID) {
		this();
		this.playerID = playerID;
	}

	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.PLAYER_DISCONNECT;
	}
	
	
}
