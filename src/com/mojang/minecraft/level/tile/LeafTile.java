package com.mojang.minecraft.level.tile;

public final class LeafTile extends Tile {
	protected LeafTile(int var1, int var2, boolean var3) {
		super(18, 22);
	}

	public final boolean isSolid() {
		return false;
	}

	public final boolean blocksLight() {
		return false;
	}
}
