package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class PlaceOrRemoveTilePacket extends Packet{
	public short x, y, z;
	public byte action, id;
	
	public PlaceOrRemoveTilePacket() {
		super();
		this.size = 2+2+2+1+1;
	}
	
	@Override
	public void read(SocketConnection con) {
		this.x = con.getShort();
		this.y = con.getShort();
		this.z = con.getShort();
		
		this.action = con.getByte();
		this.id = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeShort(this.x);
		con.writeShort(this.y);
		con.writeShort(this.z);
		
		con.writeByte(this.action);
		con.writeByte(this.id);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.PLACE_OR_REMOVE_TILE;
	}

}
