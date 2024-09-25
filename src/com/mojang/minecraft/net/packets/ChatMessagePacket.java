package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class ChatMessagePacket extends Packet{
	
	public byte playerID;
	public String message;
	
	public ChatMessagePacket() {
		super();
		this.size = 1+64;
	}
	
	public ChatMessagePacket(int playerID, String message) {
		this();
		this.playerID = (byte)playerID;
		this.message = message;
	}
	
	public ChatMessagePacket(String message) {
		this(-1, message);
	}
	
	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
		this.message = con.getString();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
		con.writeString(this.message);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.CHAT_MESSAGE;
	}

}
