package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.server.MinecraftServer;
import java.util.ArrayList;
import java.util.Random;

public final class LevelGen {
	private MinecraftServer minecraft;
	private int width;
	private int height;
	private int depth;
	private Random random = new Random();
	private byte[] blocks;
	private int[] coords = new int[1048576];

	public LevelGen(MinecraftServer var1) {
		this.minecraft = var1;
	}

	public final Level generateLevel(String var1, int var2, int var3, int var4) {
		this.minecraft.beginLevelLoading("Generating level");
		this.width = 256;
		this.height = 256;
		this.depth = 64;
		this.blocks = new byte[256 << 8 << 6];
		this.minecraft.levelLoadUpdate("Raising..");
		LevelGen var30 = this;
		Distort var8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort var9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise var10 = new PerlinNoise(this.random, 8);
		int[] var11 = new int[this.width * this.height];
		float var31 = 1.3F;

		int var13;
		int var14;
		for(var13 = 0; var13 < var30.width; ++var13) {
			for(var14 = 0; var14 < var30.height; ++var14) {
				double var15 = var8.getValue((double)((float)var13 * var31), (double)((float)var14 * var31)) / 8.0D - 8.0D;
				double var17 = var9.getValue((double)((float)var13 * var31), (double)((float)var14 * var31)) / 6.0D + 6.0D;
				double var19 = var10.getValue((double)var13, (double)var14) / 8.0D;
				if(var19 > 0.0D) {
					var17 = var15;
				}

				double var21 = Math.max(var15, var17) / 2.0D;
				if(var21 < 0.0D) {
					var21 *= 0.8D;
				}

				var11[var13 + var14 * var30.width] = (int)var21;
			}
		}

		this.minecraft.levelLoadUpdate("Eroding..");
		int[] var33 = var11;
		var30 = this;
		var9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort var39 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int var16;
		int var42;
		int var45;
		for(var42 = 0; var42 < var30.width; ++var42) {
			for(var4 = 0; var4 < var30.height; ++var4) {
				double var44 = var9.getValue((double)(var42 << 1), (double)(var4 << 1)) / 8.0D;
				var45 = var39.getValue((double)(var42 << 1), (double)(var4 << 1)) > 0.0D ? 1 : 0;
				if(var44 > 2.0D) {
					var16 = var33[var42 + var4 * var30.width];
					var16 = ((var16 - var45) / 2 << 1) + var45;
					var33[var42 + var4 * var30.width] = var16;
				}
			}
		}

		this.minecraft.levelLoadUpdate("Soiling..");
		var33 = var11;
		var30 = this;
		int var36 = this.width;
		int var41 = this.height;
		var42 = this.depth;
		PerlinNoise var32 = new PerlinNoise(this.random, 8);

		int var18;
		int var49;
		for(var13 = 0; var13 < var36; ++var13) {
			for(var14 = 0; var14 < var41; ++var14) {
				var45 = (int)(var32.getValue((double)var13, (double)var14) / 24.0D) - 4;
				var16 = var33[var13 + var14 * var36] + var42 / 2;
				var49 = var16 + var45;
				var33[var13 + var14 * var36] = Math.max(var16, var49);

				for(var18 = 0; var18 < var42; ++var18) {
					int var51 = (var18 * var30.height + var14) * var30.width + var13;
					int var20 = 0;
					if(var18 <= var16) {
						var20 = Tile.dirt.id;
					}

					if(var18 <= var49) {
						var20 = Tile.rock.id;
					}

					var30.blocks[var51] = (byte)var20;
				}
			}
		}

		this.minecraft.levelLoadUpdate("Carving..");
		boolean var38 = true;
		boolean var34 = false;
		var30 = this;
		var41 = this.width;
		var42 = this.height;
		var4 = this.depth;
		var13 = var41 * var42 * var4 / 256 / 64;

		for(var14 = 0; var14 < var13; ++var14) {
			float var46 = var30.random.nextFloat() * (float)var41;
			float var47 = var30.random.nextFloat() * (float)var4;
			float var50 = var30.random.nextFloat() * (float)var42;
			var18 = (int)((var30.random.nextFloat() + var30.random.nextFloat()) * 75.0F);
			float var52 = (float)((double)var30.random.nextFloat() * Math.PI * 2.0D);
			float var53 = 0.0F;
			float var54 = (float)((double)var30.random.nextFloat() * Math.PI * 2.0D);
			float var22 = 0.0F;

			for(int var5 = 0; var5 < var18; ++var5) {
				var46 = (float)((double)var46 + Math.sin((double)var52) * Math.cos((double)var54));
				var50 = (float)((double)var50 + Math.cos((double)var52) * Math.cos((double)var54));
				var47 = (float)((double)var47 + Math.sin((double)var54));
				var52 += var53 * 0.2F;
				var53 *= 0.9F;
				var53 += var30.random.nextFloat() - var30.random.nextFloat();
				var54 += var22 * 0.5F;
				var54 *= 0.5F;
				var22 *= 0.9F;
				var22 += var30.random.nextFloat() - var30.random.nextFloat();
				if(var30.random.nextFloat() >= 0.3F) {
					float var6 = var46 + var30.random.nextFloat() * 4.0F - 2.0F;
					float var7 = var47 + var30.random.nextFloat() * 4.0F - 2.0F;
					float var35 = var50 + var30.random.nextFloat() * 4.0F - 2.0F;
					float var40 = (float)(Math.sin((double)var5 * Math.PI / (double)var18) * 2.5D + 1.0D);

					for(int var12 = (int)(var6 - var40); var12 <= (int)(var6 + var40); ++var12) {
						for(int var23 = (int)(var7 - var40); var23 <= (int)(var7 + var40); ++var23) {
							for(int var24 = (int)(var35 - var40); var24 <= (int)(var35 + var40); ++var24) {
								float var25 = (float)var12 - var6;
								float var26 = (float)var23 - var7;
								float var27 = (float)var24 - var35;
								var25 = var25 * var25 + var26 * var26 * 2.0F + var27 * var27;
								if(var25 < var40 * var40 && var12 >= 1 && var23 >= 1 && var24 >= 1 && var12 < var30.width - 1 && var23 < var30.depth - 1 && var24 < var30.height - 1) {
									int var55 = (var23 * var30.height + var24) * var30.width + var12;
									if(var30.blocks[var55] == Tile.rock.id) {
										var30.blocks[var55] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.carveTunnels(Tile.oreCoal.id, 90, 1, 4);
		this.carveTunnels(Tile.oreIron.id, 70, 2, 4);
		this.carveTunnels(Tile.oreGold.id, 50, 3, 4);
		this.minecraft.levelLoadUpdate("Watering..");
		var30 = this;
		long var37 = System.nanoTime();
		long var43 = 0L;
		var13 = Tile.calmWater.id;

		for(var14 = 0; var14 < var30.width; ++var14) {
			var43 += var30.floodFillLiquid(var14, var30.depth / 2 - 1, 0, 0, var13);
			var43 += var30.floodFillLiquid(var14, var30.depth / 2 - 1, var30.height - 1, 0, var13);
		}

		for(var14 = 0; var14 < var30.height; ++var14) {
			var43 += var30.floodFillLiquid(0, var30.depth / 2 - 1, var14, 0, var13);
			var43 += var30.floodFillLiquid(var30.width - 1, var30.depth / 2 - 1, var14, 0, var13);
		}

		var14 = var30.width * var30.height / 200;

		for(var45 = 0; var45 < var14; ++var45) {
			var16 = var30.random.nextInt(var30.width);
			var49 = var30.depth / 2 - 1 - var30.random.nextInt(3);
			var18 = var30.random.nextInt(var30.height);
			if(var30.blocks[(var49 * var30.height + var18) * var30.width + var16] == 0) {
				var43 += var30.floodFillLiquid(var16, var49, var18, 0, var13);
			}
		}

		long var48 = System.nanoTime();
		System.out.println("Flood filled " + var43 + " tiles in " + (double)(var48 - var37) / 1000000.0D + " ms");
		this.minecraft.levelLoadUpdate("Melting..");
		this.addLava();
		this.minecraft.levelLoadUpdate("Growing..");
		this.addBeaches(var11);
		this.minecraft.levelLoadUpdate("Planting..");
		this.plantTrees(var11);
		Level var29 = new Level();
		var29.setData(256, 64, 256, this.blocks);
		var29.createTime = System.currentTimeMillis();
		var29.creator = var1;
		var29.name = "A Nice World";
		return var29;
	}

	private void addBeaches(int[] var1) {
		int var2 = this.width;
		int var3 = this.height;
		int var4 = this.depth;
		PerlinNoise var5 = new PerlinNoise(this.random, 8);
		PerlinNoise var6 = new PerlinNoise(this.random, 8);

		for(int var7 = 0; var7 < var2; ++var7) {
			for(int var8 = 0; var8 < var3; ++var8) {
				boolean var9 = var5.getValue((double)var7, (double)var8) > 8.0D;
				boolean var10 = var6.getValue((double)var7, (double)var8) > 12.0D;
				int var11 = var1[var7 + var8 * var2];
				int var12 = (var11 * this.height + var8) * this.width + var7;
				int var13 = this.blocks[((var11 + 1) * this.height + var8) * this.width + var7] & 255;
				if((var13 == Tile.water.id || var13 == Tile.calmWater.id) && var11 <= var4 / 2 - 1 && var10) {
					this.blocks[var12] = (byte)Tile.gravel.id;
				}

				if(var13 == 0) {
					int var14 = Tile.grass.id;
					if(var11 <= var4 / 2 - 1 && var9) {
						var14 = Tile.sand.id;
					}

					this.blocks[var12] = (byte)var14;
				}
			}
		}

	}

	private void plantTrees(int[] var1) {
		int var2 = this.width;
		int var3 = this.width * this.height / 4000;

		for(int var4 = 0; var4 < var3; ++var4) {
			int var5 = this.random.nextInt(this.width);
			int var6 = this.random.nextInt(this.height);

			for(int var7 = 0; var7 < 20; ++var7) {
				int var8 = var5;
				int var9 = var6;

				for(int var10 = 0; var10 < 20; ++var10) {
					var8 += this.random.nextInt(6) - this.random.nextInt(6);
					var9 += this.random.nextInt(6) - this.random.nextInt(6);
					if(var8 >= 0 && var9 >= 0 && var8 < this.width && var9 < this.height) {
						int var11 = var1[var8 + var9 * var2] + 1;
						int var12 = this.random.nextInt(3) + 4;
						boolean var13 = true;

						int var14;
						int var16;
						int var17;
						int var18;
						for(var14 = var11; var14 <= var11 + 1 + var12; ++var14) {
							byte var15 = 1;
							if(var14 >= var11 + 1 + var12 - 2) {
								var15 = 2;
							}

							for(var16 = var8 - var15; var16 <= var8 + var15 && var13; ++var16) {
								for(var17 = var9 - var15; var17 <= var9 + var15 && var13; ++var17) {
									if(var16 >= 0 && var14 >= 0 && var17 >= 0 && var16 < this.width && var14 < this.depth && var17 < this.height) {
										var18 = this.blocks[(var14 * this.height + var17) * this.width + var16] & 255;
										if(var18 != 0) {
											var13 = false;
										}
									} else {
										var13 = false;
									}
								}
							}
						}

						if(var13) {
							var14 = (var11 * this.height + var9) * this.width + var8;
							int var22 = this.blocks[((var11 - 1) * this.height + var9) * this.width + var8] & 255;
							if(var22 == Tile.grass.id && var11 < this.depth - var12 - 1) {
								this.blocks[var14 - 1 * this.width * this.height] = (byte)Tile.dirt.id;

								for(var16 = var11 - 3 + var12; var16 <= var11 + var12; ++var16) {
									var17 = var16 - (var11 + var12);
									var18 = 1 - var17 / 2;

									for(int var21 = var8 - var18; var21 <= var8 + var18; ++var21) {
										var22 = var21 - var8;

										for(int var19 = var9 - var18; var19 <= var9 + var18; ++var19) {
											int var20 = var19 - var9;
											if(Math.abs(var22) != var18 || Math.abs(var20) != var18 || this.random.nextInt(2) != 0 && var17 != 0) {
												this.blocks[(var16 * this.height + var19) * this.width + var21] = (byte)Tile.leaf.id;
											}
										}
									}
								}

								for(var16 = 0; var16 < var12; ++var16) {
									this.blocks[var14 + var16 * this.width * this.height] = (byte)Tile.log.id;
								}
							}
						}
					}
				}
			}
		}

	}

	private void carveTunnels(int var1, int var2, int var3, int var4) {
		byte var25 = (byte)var1;
		var3 = this.width;
		var4 = this.height;
		int var5 = this.depth;
		int var6 = var3 * var4 * var5 / 256 / 64 * var2 / 100;

		for(int var7 = 0; var7 < var6; ++var7) {
			float var8 = this.random.nextFloat() * (float)var3;
			float var9 = this.random.nextFloat() * (float)var5;
			float var10 = this.random.nextFloat() * (float)var4;
			int var11 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)var2 / 100.0F);
			float var12 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float var13 = 0.0F;
			float var14 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float var15 = 0.0F;

			for(int var16 = 0; var16 < var11; ++var16) {
				var8 = (float)((double)var8 + Math.sin((double)var12) * Math.cos((double)var14));
				var10 = (float)((double)var10 + Math.cos((double)var12) * Math.cos((double)var14));
				var9 = (float)((double)var9 + Math.sin((double)var14));
				var12 += var13 * 0.2F;
				var13 *= 0.9F;
				var13 += this.random.nextFloat() - this.random.nextFloat();
				var14 += var15 * 0.5F;
				var14 *= 0.5F;
				var15 *= 0.9F;
				var15 += this.random.nextFloat() - this.random.nextFloat();
				float var17 = (float)(Math.sin((double)var16 * Math.PI / (double)var11) * (double)var2 / 100.0D + 1.0D);

				for(int var18 = (int)(var8 - var17); var18 <= (int)(var8 + var17); ++var18) {
					for(int var19 = (int)(var9 - var17); var19 <= (int)(var9 + var17); ++var19) {
						for(int var20 = (int)(var10 - var17); var20 <= (int)(var10 + var17); ++var20) {
							float var21 = (float)var18 - var8;
							float var22 = (float)var19 - var9;
							float var23 = (float)var20 - var10;
							var21 = var21 * var21 + var22 * var22 * 2.0F + var23 * var23;
							if(var21 < var17 * var17 && var18 >= 1 && var19 >= 1 && var20 >= 1 && var18 < this.width - 1 && var19 < this.depth - 1 && var20 < this.height - 1) {
								int var24 = (var19 * this.height + var20) * this.width + var18;
								if(this.blocks[var24] == Tile.rock.id) {
									this.blocks[var24] = var25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void addLava() {
		int var1 = 0;
		int var2 = this.width * this.height * this.depth / 10000;

		for(int var3 = 0; var3 < var2; ++var3) {
			int var4 = this.random.nextInt(this.width);
			int var5 = this.random.nextInt(this.depth / 2 - 4);
			int var6 = this.random.nextInt(this.height);
			if(this.blocks[(var5 * this.height + var6) * this.width + var4] == 0) {
				++var1;
				this.floodFillLiquid(var4, var5, var6, 0, Tile.calmLava.id);
			}
		}

		System.out.println("LavaCount: " + var1);
	}

	private long floodFillLiquid(int var1, int var2, int var3, int var4, int var5) {
		byte var20 = (byte)var5;
		ArrayList var21 = new ArrayList();
		byte var6 = 0;
		int var7 = 1;

		int var8;
		for(var8 = 1; 1 << var7 < this.width; ++var7) {
		}

		while(1 << var8 < this.height) {
			++var8;
		}

		int var9 = this.height - 1;
		int var10 = this.width - 1;
		int var22 = var6 + 1;
		this.coords[0] = ((var2 << var8) + var3 << var7) + var1;
		long var13 = 0L;
		var1 = this.width * this.height;

		while(var22 > 0) {
			--var22;
			var2 = this.coords[var22];
			if(var22 == 0 && var21.size() > 0) {
				System.out.println("IT HAPPENED!");
				this.coords = (int[])var21.remove(var21.size() - 1);
				var22 = this.coords.length;
			}

			var3 = var2 >> var7 & var9;
			int var11 = var2 >> var7 + var8;
			int var12 = var2 & var10;

			int var15;
			for(var15 = var12; var12 > 0 && this.blocks[var2 - 1] == 0; --var2) {
				--var12;
			}

			while(var15 < this.width && this.blocks[var2 + var15 - var12] == 0) {
				++var15;
			}

			int var16 = var2 >> var7 & var9;
			int var17 = var2 >> var7 + var8;
			if(var16 != var3 || var17 != var11) {
				System.out.println("hoooly fuck");
			}

			boolean var23 = false;
			boolean var24 = false;
			boolean var18 = false;
			var13 += (long)(var15 - var12);

			for(var12 = var12; var12 < var15; ++var12) {
				this.blocks[var2] = var20;
				boolean var19;
				if(var3 > 0) {
					var19 = this.blocks[var2 - this.width] == 0;
					if(var19 && !var23) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - this.width;
					}

					var23 = var19;
				}

				if(var3 < this.height - 1) {
					var19 = this.blocks[var2 + this.width] == 0;
					if(var19 && !var24) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 + this.width;
					}

					var24 = var19;
				}

				if(var11 > 0) {
					byte var25 = this.blocks[var2 - var1];
					if((var20 == Tile.lava.id || var20 == Tile.calmLava.id) && (var25 == Tile.water.id || var25 == Tile.calmWater.id)) {
						this.blocks[var2 - var1] = (byte)Tile.rock.id;
					}

					var19 = var25 == 0;
					if(var19 && !var18) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - var1;
					}

					var18 = var19;
				}

				++var2;
			}
		}

		return var13;
	}
}
