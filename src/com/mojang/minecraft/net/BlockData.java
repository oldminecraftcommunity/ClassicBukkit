package com.mojang.minecraft.net;

public class BlockData extends PosData {
	public byte id, action;
	
	public BlockData(short x, short y, short z, byte id, byte action) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.action = action;
	}
}
