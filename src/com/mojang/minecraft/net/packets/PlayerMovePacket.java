package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class PlayerMovePacket extends Packet{
	public byte playerID;
	public byte xrel, yrel, zrel;
	
	public PlayerMovePacket() {
		super();
		this.size = 1+1+1+1;
	}
	
	public PlayerMovePacket(byte playerID, byte xrel, byte yrel, byte zrel) {
		this();
		this.playerID = playerID;
		this.xrel = xrel;
		this.yrel = yrel;
		this.zrel = zrel;
	}
	
	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
		this.xrel = con.getByte();
		this.yrel = con.getByte();
		this.zrel = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
		con.writeByte(this.xrel);
		con.writeByte(this.yrel);
		con.writeByte(this.zrel);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.PLAYER_MOVE;
	}

}
