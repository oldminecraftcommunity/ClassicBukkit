package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.server.PlayerInstance;

public class PlayerJoinPacket extends Packet{

	public byte playerID;
	public String username;
	public short x, y, z;
	public byte yaw, pitch;
	
	public PlayerJoinPacket() {
		super();
		this.size = 1+64+2+2+2+1+1;
	}
	
	public PlayerJoinPacket(byte playerID, String username, short x, short y, short z, byte yaw, byte pitch) {
		this();
		this.playerID = playerID;
		this.username = username;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public PlayerJoinPacket(PlayerInstance player) {
		this((byte)player.playerID, player.name, (short)player.x, (short)player.y, (short)player.z, (byte)player.yaw, (byte)player.pitch);
	}

	@Override
	public void read(SocketConnection con) {
		this.playerID = con.getByte();
		this.username = con.getString();
		this.x = con.getShort();
		this.y = con.getShort();
		this.z = con.getShort();
		this.yaw = con.getByte();
		this.pitch = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeByte(this.playerID);
		con.writeString(this.username);
		con.writeShort(this.x);
		con.writeShort(this.y);
		con.writeShort(this.z);
		con.writeByte(this.yaw);
		con.writeByte(this.pitch);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.PLAYER_JOIN;
	}

}
