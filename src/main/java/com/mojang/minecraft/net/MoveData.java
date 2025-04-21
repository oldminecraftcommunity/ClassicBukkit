package com.mojang.minecraft.net;

public class MoveData extends PosData{
	
	public byte yaw, pitch;
	
	public MoveData(short x, short y, short z, byte yaw, byte pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
}
