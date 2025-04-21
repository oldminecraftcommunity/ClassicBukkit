package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class LevelDataChunkPacket extends Packet{
	public short dataLength;
	public byte[] data;
	/**
	 * Uses (lastIndex + length) * 100 / worldBlocksLength as formula
	 */
	public byte done;
	
	public LevelDataChunkPacket() {
		super();
		this.size = 2+1024+1;
	}
	
	public LevelDataChunkPacket(short dataLength, byte[] data, byte done) {
		this();
		this.dataLength = dataLength;
		this.data = data;
		this.done = done;
	}
	
	@Override
	public void read(SocketConnection con) {
		this.dataLength = con.getShort();
		this.data = con.getBytearray();
		this.done = con.getByte();
	}

	@Override
	public void write(SocketConnection con) {
		con.writeShort(this.dataLength);
		con.writeBytearray(this.data);
		con.writeByte(this.done);
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.LEVEL_DATA_CHUNK;
	}

}
