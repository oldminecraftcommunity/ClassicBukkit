package com.mojang.minecraft.level.tile;

public final class GlassTile extends Tile {
	protected GlassTile(int var1, int var2, boolean var3) {
		super(20, 49);
	}

	public final boolean isSolid() {
		return false;
	}

	public final boolean blocksLight() {
		return false;
	}
}
