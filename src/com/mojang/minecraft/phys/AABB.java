package com.mojang.minecraft.phys;

import java.io.Serializable;

public class AABB implements Serializable {
	public static final long serialVersionUID = 0L;
	private float epsilon = 0.0F;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;

	public AABB(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.minX = var1;
		this.minY = var2;
		this.minZ = var3;
		this.maxX = var4;
		this.maxY = var5;
		this.maxZ = var6;
	}

	public AABB expand(float x, float y, float z) {
		float minX = this.minX;
		float minY = this.minY;
		float minZ = this.minZ;
		float maxX = this.maxX;
		float maxY = this.maxY;
		float maxZ = this.maxZ;
		if(x < 0.0F) {
			minX += x;
		}

		if(x > 0.0F) {
			maxX += x;
		}

		if(y < 0.0F) {
			minY += y;
		}

		if(y > 0.0F) {
			maxY += y;
		}

		if(z < 0.0F) {
			minZ += z;
		}

		if(z > 0.0F) {
			maxZ += z;
		}

		return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public AABB grow(float var1, float var2, float var3) {
		float var4 = this.minX - var1;
		float var5 = this.minY - var2;
		float var6 = this.minZ - var3;
		var1 += this.maxX;
		var2 += this.maxY;
		float var7 = this.maxZ + var3;
		return new AABB(var4, var5, var6, var1, var2, var7);
	}

	public AABB cloneMove(float var1, float var2, float var3) {
		return new AABB(this.minX + var3, this.minY + var2, this.minZ + var3, this.maxX + var1, this.maxY + var2, this.maxZ + var3);
	}

	public float clipXCollide(AABB var1, float var2) {
		if(var1.maxY > this.minY && var1.minY < this.maxY) {
			if(var1.maxZ > this.minZ && var1.minZ < this.maxZ) {
				float var3;
				if(var2 > 0.0F && var1.maxX <= this.minX) {
					var3 = this.minX - var1.maxX - this.epsilon;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.minX >= this.maxX) {
					var3 = this.maxX - var1.minX + this.epsilon;
					if(var3 > var2) {
						var2 = var3;
					}
				}
			}
		}
		return var2;
	}

	public float clipYCollide(AABB var1, float var2) {
		if(var1.maxX > this.minX && var1.minX < this.maxX) {
			if(var1.maxZ > this.minZ && var1.minZ < this.maxZ) {
				float var3;
				if(var2 > 0.0F && var1.maxY <= this.minY) {
					var3 = this.minY - var1.maxY - this.epsilon;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.minY >= this.maxY) {
					var3 = this.maxY - var1.minY + this.epsilon;
					if(var3 > var2) {
						var2 = var3;
					}
				}
			}
		}
		return var2;
	}

	public float clipZCollide(AABB var1, float var2) {
		if(var1.maxX > this.minX && var1.minX < this.maxX) {
			if(var1.maxY > this.minY && var1.minY < this.maxY) {
				float var3;
				if(var2 > 0.0F && var1.maxZ <= this.minZ) {
					var3 = this.minZ - var1.maxZ - this.epsilon;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.minZ >= this.maxZ) {
					var3 = this.maxZ - var1.minZ + this.epsilon;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				
			}
		}
		return var2;
	}

	public boolean intersects(AABB var1) {
		return var1.maxX > this.minX && var1.minX < this.maxX ? (var1.maxY > this.minY && var1.minY < this.maxY ? var1.maxZ > this.minZ && var1.minZ < this.maxZ : false) : false;
	}

	public void move(float var1, float var2, float var3) {
		this.minX += var1;
		this.minY += var2;
		this.minZ += var3;
		this.maxX += var1;
		this.maxY += var2;
		this.maxZ += var3;
	}
}
