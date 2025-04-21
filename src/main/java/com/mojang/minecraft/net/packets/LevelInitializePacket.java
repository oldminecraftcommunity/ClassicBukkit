package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class LevelInitializePacket extends Packet{
	
	public LevelInitializePacket() {
		super();
		this.size = 0;
	}
	
	@Override
	public void read(SocketConnection con) {
		
	}

	@Override
	public void write(SocketConnection con) {
		
	}

	@Override
	public PacketID getPacketID() {
		return PacketID.LEVEL_INITIALIZE;
	}

}
