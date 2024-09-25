package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class PlayerRotatePacket extends Packet{
	public byte playerID;
	public byte yaw, pitch;
	
	public PlayerRotatePacket() {
		super();
		this.size = 1+1+1;
	}
	public PlayerRotatePacket(byte playerID, byte yaw, byte pitch) {
		this();
		this.playerID = playerID;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
		this.yaw = con.getByte();
		this.pitch = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
		con.writeByte(this.yaw);
		con.writeByte(this.pitch);
	}
	@Override
	public PacketID getPacketID() {
		return PacketID.PLAYER_ROTATE;
	}

}
