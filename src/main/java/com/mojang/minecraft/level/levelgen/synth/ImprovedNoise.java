package com.mojang.minecraft.level.levelgen.synth;

import java.util.Random;

public final class ImprovedNoise extends Synth {
	private int[] p;

	public ImprovedNoise() {
		this(new Random());
	}

	public ImprovedNoise(Random rand) {
		this.p = new int[512];

		for(int i = 0; i < 256; ++i) {
			this.p[i] = i;
		}

		for(int i = 0; i < 256; ++i) {
			int var3 = rand.nextInt(256 - i) + i;
			int var4 = this.p[i];
			this.p[i] = this.p[var3];
			this.p[var3] = var4;
			this.p[i + 256] = this.p[i];
		}

	}

	private static double fade(double var0) {
		return var0 * var0 * var0 * (var0 * (var0 * 6.0D - 15.0D) + 10.0D);
	}

	private static double lerp(double var0, double var2, double var4) {
		return var2 + var0 * (var4 - var2);
	}

	private static double grad(int var0, double var1, double var3, double var5) {
		var0 &= 15;
		double var8 = var0 < 8 ? var1 : var3;
		double var10 = var0 < 4 ? var3 : (var0 != 12 && var0 != 14 ? var5 : var1);
		return ((var0 & 1) == 0 ? var8 : -var8) + ((var0 & 2) == 0 ? var10 : -var10);
	}

	public final double getValue(double x, double y) {
		double var10 = 0.0D;
		double var8 = y;
		double var6 = x;
		int var21 = (int)Math.floor(x) & 255;
		int var2 = (int)Math.floor(y) & 255;
		int var22 = (int)Math.floor(0.0D) & 255;
		var6 -= Math.floor(var6);
		var8 -= Math.floor(var8);
		var10 = 0.0D - Math.floor(0.0D);
		double var15 = fade(var6);
		double var17 = fade(var8);
		double var19 = fade(var10);
		int var4 = this.p[var21] + var2;
		int var5 = this.p[var4] + var22;
		var4 = this.p[var4 + 1] + var22;
		var21 = this.p[var21 + 1] + var2;
		var2 = this.p[var21] + var22;
		var21 = this.p[var21 + 1] + var22;
		return lerp(var19, lerp(var17, lerp(var15, grad(this.p[var5], var6, var8, var10), grad(this.p[var2], var6 - 1.0D, var8, var10)), lerp(var15, grad(this.p[var4], var6, var8 - 1.0D, var10), grad(this.p[var21], var6 - 1.0D, var8 - 1.0D, var10))), lerp(var17, lerp(var15, grad(this.p[var5 + 1], var6, var8, var10 - 1.0D), grad(this.p[var2 + 1], var6 - 1.0D, var8, var10 - 1.0D)), lerp(var15, grad(this.p[var4 + 1], var6, var8 - 1.0D, var10 - 1.0D), grad(this.p[var21 + 1], var6 - 1.0D, var8 - 1.0D, var10 - 1.0D))));
	}
}
