package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class PlayerMoveAndRotatePacket extends Packet{
	public byte playerID;
	public byte xrel, yrel, zrel;
	public byte yaw, pitch;
	
	public PlayerMoveAndRotatePacket() {
		super();
		this.size = 1+1+1+1+1+1;
	}
	
	public PlayerMoveAndRotatePacket(byte playerID, byte xrel, byte yrel, byte zrel, byte yaw, byte pitch) {
		this();
		this.playerID = playerID;
		this.xrel = xrel;
		this.yrel = yrel;
		this.zrel = zrel;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
		this.xrel = con.getByte();
		this.yrel = con.getByte();
		this.zrel = con.getByte();
		this.yaw = con.getByte();
		this.pitch = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
		con.writeByte(this.xrel);
		con.writeByte(this.yrel);
		con.writeByte(this.zrel);
		con.writeByte(this.yaw);
		con.writeByte(this.pitch);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.PLAYER_MOVE_AND_ROTATE;
	}

}
