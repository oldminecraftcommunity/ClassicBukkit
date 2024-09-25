package com.mojang.minecraft.net.packets;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.net.Packet;

public class TimedOutPacket extends Packet{
	public TimedOutPacket() {
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
		return PacketID.TIMED_OUT;
	}
}
