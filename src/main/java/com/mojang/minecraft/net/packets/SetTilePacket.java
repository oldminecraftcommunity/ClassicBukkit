package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class SetTilePacket extends Packet{
	public short x, y, z;
	public byte id;
	
	public SetTilePacket() {
		super();
		this.size = 2+2+2+1;
	}
	
	public SetTilePacket(short x, short y, short z, byte id) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

	@Override
	public void read(SocketConnection con) {
		this.x = con.getShort();
		this.y = con.getShort();
		this.z = con.getShort();
		
		this.id = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeShort(this.x);
		con.writeShort(this.y);
		con.writeShort(this.z);
		
		con.writeByte(this.id);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.SET_TILE;
	}

}
