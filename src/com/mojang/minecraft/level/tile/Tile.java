package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.phys.AABB;
import java.util.Random;

public class Tile {
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock;
	public static final Tile grass;
	public static final Tile dirt;
	public static final Tile wood;
	public static final Tile stoneBrick;
	public static final Tile bush;
	public static final Tile unbreakable;
	public static final Tile water;
	public static final Tile calmWater;
	public static final Tile lava;
	public static final Tile calmLava;
	public static final Tile sand;
	public static final Tile gravel;
	public static final Tile oreGold;
	public static final Tile oreIron;
	public static final Tile oreCoal;
	public static final Tile log;
	public static final Tile leaf;
	public static final Tile sponge;
	public static final Tile glass;
	public static final Tile clothRed;
	public static final Tile clothOrange;
	public static final Tile clothYellow;
	public static final Tile clothChartreuse;
	public static final Tile clothGreen;
	public static final Tile clothSpringGreen;
	public static final Tile clothCyan;
	public static final Tile clothCapri;
	public static final Tile clothUltramarine;
	public static final Tile clothViolet;
	public static final Tile clothPurple;
	public static final Tile clothMagenta;
	public static final Tile clothRose;
	public static final Tile clothDarkGray;
	public static final Tile clothGray;
	public static final Tile clothWhite;
	public static final Tile plantYellow;
	public static final Tile plantRed;
	public static final Tile mushroomBrown;
	public static final Tile mushroomRed;
	public static final Tile blockGold;
	public final int id;

	protected Tile(int var1) {
		//new Random();
		tiles[var1] = this;
		this.id = var1;
	}

	protected final void setTicking(boolean var1) {
		shouldTick[this.id] = var1;
	}

	protected Tile(int var1, int var2) {
		this(var1);
	}

	public final void setTickSpeed(int var1) {
		tickSpeed[this.id] = 16;
	}

	public AABB getAABB(int var1, int var2, int var3) {
		return new AABB((float)var1, (float)var2, (float)var3, (float)(var1 + 1), (float)(var2 + 1), (float)(var3 + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level var1, int var2, int var3, int var4, int var5) {
	}

	public void onBlockAdded(Level var1, int var2, int var3, int var4) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level var1, int var2, int var3, int var4) {
	}

	public void onTileRemoved(Level var1, int var2, int var3, int var4) {
	}

	static {
		Tile var10000 = new Tile(1, 1);
		float var0 = 1.0F;
		var0 = 1.0F;
		Tile var1 = var10000;
		rock = var1;
		GrassTile var8 = new GrassTile(2);
		var0 = 1.0F;
		var0 = 0.9F;
		GrassTile var2 = var8;
		grass = var2;
		DirtTile var10 = new DirtTile(3, 2);
		var0 = 1.0F;
		var0 = 0.8F;
		DirtTile var3 = var10;
		dirt = var3;
		var10000 = new Tile(4, 16);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		wood = var1;
		var10000 = new Tile(5, 4);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		stoneBrick = var1;
		Bush var12 = new Bush(6, 15);
		var0 = 1.0F;
		var0 = 0.7F;
		Bush var4 = var12;
		bush = var4;
		var10000 = new Tile(7, 17);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		unbreakable = var1;
		LiquidTile var13 = new LiquidTile(8, Liquid.water);
		var0 = 1.0F;
		var0 = 1.0F;
		LiquidTile var5 = var13;
		water = var5;
		CalmLiquidTile var15 = new CalmLiquidTile(9, Liquid.water);
		var0 = 1.0F;
		var0 = 1.0F;
		CalmLiquidTile var6 = var15;
		calmWater = var6;
		var13 = new LiquidTile(10, Liquid.lava);
		var0 = 1.0F;
		var0 = 1.0F;
		var5 = var13;
		lava = var5;
		var15 = new CalmLiquidTile(11, Liquid.lava);
		var0 = 1.0F;
		var0 = 1.0F;
		var6 = var15;
		calmLava = var6;
		FallingTile var17 = new FallingTile(12, 18);
		var0 = 1.0F;
		var0 = 0.8F;
		FallingTile var7 = var17;
		sand = var7;
		var17 = new FallingTile(13, 19);
		var0 = 1.0F;
		var0 = 0.8F;
		var7 = var17;
		gravel = var7;
		var10000 = new Tile(14, 32);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		oreGold = var1;
		var10000 = new Tile(15, 33);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		oreIron = var1;
		var10000 = new Tile(16, 34);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		oreCoal = var1;
		LogTile var18 = new LogTile(17);
		var0 = 1.0F;
		var0 = 1.0F;
		LogTile var9 = var18;
		log = var9;
		LeafTile var19 = new LeafTile(18, 22, true);
		var0 = 0.4F;
		var0 = 1.0F;
		LeafTile var11 = var19;
		leaf = var11;
		SpongeTile var20 = new SpongeTile(19);
		var0 = 0.9F;
		var0 = 1.0F;
		SpongeTile var14 = var20;
		sponge = var14;
		GlassTile var21 = new GlassTile(20, 49, false);
		var0 = 1.0F;
		var0 = 1.0F;
		GlassTile var16 = var21;
		glass = var16;
		var10000 = new Tile(21, 64);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothRed = var1;
		var10000 = new Tile(22, 65);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothOrange = var1;
		var10000 = new Tile(23, 66);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothYellow = var1;
		var10000 = new Tile(24, 67);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothChartreuse = var1;
		var10000 = new Tile(25, 68);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothGreen = var1;
		var10000 = new Tile(26, 69);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothSpringGreen = var1;
		var10000 = new Tile(27, 70);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothCyan = var1;
		var10000 = new Tile(28, 71);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothCapri = var1;
		var10000 = new Tile(29, 72);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothUltramarine = var1;
		var10000 = new Tile(30, 73);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothViolet = var1;
		var10000 = new Tile(31, 74);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothPurple = var1;
		var10000 = new Tile(32, 75);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothMagenta = var1;
		var10000 = new Tile(33, 76);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothRose = var1;
		var10000 = new Tile(34, 77);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothDarkGray = var1;
		var10000 = new Tile(35, 78);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothGray = var1;
		var10000 = new Tile(36, 79);
		var0 = 1.0F;
		var0 = 1.0F;
		var1 = var10000;
		clothWhite = var1;
		var12 = new Bush(37, 13);
		var0 = 1.0F;
		var0 = 0.7F;
		var4 = var12;
		plantYellow = var4;
		var12 = new Bush(38, 12);
		var0 = 1.0F;
		var0 = 0.7F;
		var4 = var12;
		plantRed = var4;
		var12 = new Bush(39, 29);
		var0 = 1.0F;
		var0 = 0.7F;
		var4 = var12;
		mushroomBrown = var4;
		var12 = new Bush(40, 28);
		var0 = 1.0F;
		var0 = 0.7F;
		var4 = var12;
		mushroomRed = var4;
		var10000 = new Tile(41, 40);
		var0 = 1.0F;
		var0 = 0.7F;
		var1 = var10000;
		blockGold = var1;
	}
}
