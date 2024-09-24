package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class SpongeTile extends Tile {
	protected SpongeTile(int var1) {
		super(19);
	}

	public final void onTileAdded(Level level, int x, int y, int z) {
		for(int bx = x - 2; bx <= x + 2; ++bx) {
			for(int by = y - 2; by <= y + 2; ++by) {
				for(int bz = z - 2; bz <= z + 2; ++bz) {
					if(level.isWater(bx, by, bz)) {
						level.setTileNoNeighborChange(bx, by, bz, 0);
					}
				}
			}
		}

	}

	public final void onTileRemoved(Level var1, int var2, int var3, int var4) {
		for(int var7 = var2 - 2; var7 <= var2 + 2; ++var7) {
			for(int var5 = var3 - 2; var5 <= var3 + 2; ++var5) {
				for(int var6 = var4 - 2; var6 <= var4 + 2; ++var6) {
					var1.updateNeighborsAt(var7, var5, var6, var1.getTile(var7, var5, var6));
				}
			}
		}

	}
}
