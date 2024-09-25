package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.net.Packet;

public class LevelFinalizePacket extends Packet{
	
	public short width, depth, height;
	
	public LevelFinalizePacket() {
		super();
		this.size = 2+2+2;
	}
	
	public LevelFinalizePacket(Level level) {
		this();
		this.width = (short) level.width;
		this.height = (short) level.height;
		this.depth = (short) level.depth;
	}

	@Override
	public void read(SocketConnection con) {
		this.width = con.getShort();
		this.depth = con.getShort();
		this.height = con.getShort();
		
	}

	@Override
	public void write(SocketConnection con) {
		con.writeShort(this.width);
		con.writeShort(this.depth);
		con.writeShort(this.height);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.LEVEL_FINALIZE;
	}

}
