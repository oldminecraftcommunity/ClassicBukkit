package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class KickPlayerPacket extends Packet{
	
	public String message;
	
	public KickPlayerPacket() {
		super();
		this.size = 64;
	}
	
	public KickPlayerPacket(String msg) {
		this();
		this.message = msg;
	}

	@Override
	public void read(SocketConnection con) {
		this.message = con.getString();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeString(this.message);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.KICK_PLAYER;
	}

}
