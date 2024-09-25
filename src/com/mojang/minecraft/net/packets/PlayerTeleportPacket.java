package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class PlayerTeleportPacket extends Packet{
	
	public byte playerID;
	public short x, y, z;
	public byte yaw, pitch;
	
	public PlayerTeleportPacket() {
		super();
		this.size = 1+2+2+2+1+1;
	}
	public PlayerTeleportPacket(byte playerID, short x, short y, short z, byte yaw, byte pitch) {
		this();
		this.playerID = playerID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
		this.x = con.getShort();
		this.y = con.getShort();
		this.z = con.getShort();
		this.yaw = con.getByte();
		this.pitch = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
		con.writeShort(this.x);
		con.writeShort(this.y);
		con.writeShort(this.z);
		con.writeByte(this.yaw);
		con.writeByte(this.pitch);
	}
	@Override
	public PacketID getPacketID() {
		return PacketID.PLAYER_TELEPORT;
	}

}
