package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import java.util.Random;

public final class Bush extends Tile {
	protected Bush(int var1, int var2) {
		super(var1);
		this.setTicking(true);
	}

	public final void tick(Level level, int x, int y, int z, Random rand) {
		int id = level.getTile(x, y - 1, z);
		if(!level.isLit(x, y, z) || id != Tile.dirt.id && id != Tile.grass.id) {
			level.setTile(x, y, z, 0);
		}

	}

	public final AABB getAABB(int var1, int var2, int var3) {
		return null;
	}

	public final boolean blocksLight() {
		return false;
	}

	public final boolean isSolid() {
		return false;
	}
}
