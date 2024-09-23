package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import java.util.Random;

public final class Bush extends Tile {
	protected Bush(int var1, int var2) {
		super(var1);
		this.setTicking(true);
	}

	public final void tick(Level var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getTile(var2, var3 - 1, var4);
		if(!var1.isLit(var2, var3, var4) || var6 != Tile.dirt.id && var6 != Tile.grass.id) {
			var1.setTile(var2, var3, var4, 0);
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
