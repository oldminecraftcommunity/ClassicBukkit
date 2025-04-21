package com.mojang.minecraft.level.levelgen.synth;

import java.util.Random;

public final class PerlinNoise extends Synth {
	private ImprovedNoise[] noiseLevels = new ImprovedNoise[8];
	private int levels = 8;

	public PerlinNoise(Random var1, int var2) {
		for(int i = 0; i < var2; ++i) {
			this.noiseLevels[i] = new ImprovedNoise(var1);
		}

	}

	public final double getValue(double x, double y) {
		double value = 0.0D;
		double var7 = 1.0D;

		for(int var9 = 0; var9 < this.levels; ++var9) {
			value += this.noiseLevels[var9].getValue(x / var7, y / var7) * var7;
			var7 *= 2.0D;
		}

		return value;
	}
}
