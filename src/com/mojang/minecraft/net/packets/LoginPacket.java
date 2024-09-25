package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class LoginPacket extends Packet{
	public byte protocol;
	public String username;
	public String something;
	public byte isop;
	
	public LoginPacket() {
		super();
		this.size = 1+64+64+1;
	}
	
	public LoginPacket(byte protocol, String serverName, String motd, int isop) {
		this();
		this.protocol = protocol;
		this.username = serverName;
		this.something = motd;
		this.isop = (byte) isop;
	}

	public void read(SocketConnection connection) {
		this.protocol = connection.getByte();
		this.username = connection.getString();
		this.something = connection.getString();
		this.isop = connection.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.protocol);
		con.writeString(this.username);
		con.writeString(this.something);
		con.writeByte(this.isop);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.LOGIN;
	}
}
