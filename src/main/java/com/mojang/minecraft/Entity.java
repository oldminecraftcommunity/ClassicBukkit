package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.phys.AABB;
import java.io.Serializable;
import java.util.ArrayList;

public class Entity implements Serializable {
	public static final long serialVersionUID = 0L;
	protected Level level;
	public float xo;
	public float yo;
	public float zo;
	public float x;
	public float y;
	public float z;
	public float xd;
	public float yd;
	public float zd;
	public float yRot;
	public float xRot;
	public float yRotO;
	public float xRotO;
	public AABB bb;
	public boolean onGround = false;
	public boolean horizontalCollision = false;
	public boolean removed = false;
	public float heightOffset = 0.0F;
	protected float bbWidth = 0.6F;
	protected float bbHeight = 1.8F;

	public Entity(Level level) {
		this.level = level;
		this.setPos(0.0F, 0.0F, 0.0F);
	}

	protected void resetPos() {
		if(this.level != null) {
			float var1 = (float)this.level.xSpawn + 0.5F;
			float var2 = (float)this.level.ySpawn;

			for(float var3 = (float)this.level.zSpawn + 0.5F; var2 > 0.0F; ++var2) {
				this.setPos(var1, var2, var3);
				if(this.level.getCubes(this.bb).size() == 0) {
					break;
				}
			}

			this.xd = this.yd = this.zd = 0.0F;
			this.yRot = this.level.rotSpawn;
			this.xRot = 0.0F;
		}
	}

	public void remove() {
		this.removed = true;
	}

	protected void setSize(float var1, float var2) {
		this.bbWidth = var1;
		this.bbHeight = var2;
	}

	protected void setPos() {
		this.setPos(this.x, this.y, this.z);
		this.setRot(this.yRot, this.xRot);
	}

	protected void setRot(float var1, float var2) {
		this.yRot = var1;
		this.xRot = var2;
	}

	protected void setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		float width = this.bbWidth / 2.0F;
		float height = this.bbHeight / 2.0F;
		this.bb = new AABB(x - width, y - height, z - width, x + width, y + height, z + width);
	}

	public void turn(float var1, float var2) {
		float var3 = this.xRot;
		float var4 = this.yRot;
		this.yRot = (float)((double)this.yRot + (double)var1 * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)var2 * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

		this.xRotO += this.xRot - var3;
		this.yRotO += this.yRot - var4;
	}

	public void interpolateTurn(float var1, float var2) {
		this.yRot = (float)((double)this.yRot + (double)var1 * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)var2 * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.xRotO = this.xRot;
		this.yRotO = this.yRot;
	}

	public boolean isFree(float var1, float var2, float var3) {
		AABB var4 = this.bb.cloneMove(var1, var2, var3);
		ArrayList<AABB> var5 = this.level.getCubes(var4);
		return var5.size() > 0 ? false : !this.level.containsAnyLiquid(var4);
	}

	public void move(float mx, float my, float mz) {
		float savedMotX = mx;
		float savedMotY = my;
		float savedMotZ = mz;
		ArrayList<AABB> collidingWith = this.level.getCubes(this.bb.expand(mx, my, mz));

		int var8;
		for(var8 = 0; var8 < collidingWith.size(); ++var8) {
			my = collidingWith.get(var8).clipYCollide(this.bb, my);
		}

		this.bb.move(0.0F, my, 0.0F);

		for(var8 = 0; var8 < collidingWith.size(); ++var8) {
			mx = collidingWith.get(var8).clipXCollide(this.bb, mx);
		}

		this.bb.move(mx, 0.0F, 0.0F);

		for(var8 = 0; var8 < collidingWith.size(); ++var8) {
			mz = collidingWith.get(var8).clipZCollide(this.bb, mz);
		}

		this.bb.move(0.0F, 0.0F, mz);
		this.horizontalCollision = savedMotX != mx || savedMotZ != mz;
		this.onGround = savedMotY != my && savedMotY < 0.0F;
		if(savedMotX != mx) {
			this.xd = 0.0F;
		}

		if(savedMotY != my) {
			this.yd = 0.0F;
		}

		if(savedMotZ != mz) {
			this.zd = 0.0F;
		}

		this.x = (this.bb.minX + this.bb.maxX) / 2.0F;
		this.y = this.bb.minY + this.heightOffset;
		this.z = (this.bb.minZ + this.bb.maxZ) / 2.0F;
	}

	public boolean isInWater() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), Liquid.water);
	}

	public boolean isInLava() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), Liquid.lava);
	}

	public void moveRelative(float var1, float var2, float var3) {
		float var4 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
		if(var4 >= 0.01F) {
			if(var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = var3 / var4;
			var1 *= var4;
			var2 *= var4;
			var3 = (float)Math.sin((double)this.yRot * Math.PI / 180.0D);
			var4 = (float)Math.cos((double)this.yRot * Math.PI / 180.0D);
			this.xd += var1 * var4 - var2 * var3;
			this.zd += var2 * var4 + var1 * var3;
		}
	}

	public boolean isLit() {
		int var1 = (int)this.x;
		int var2 = (int)this.y;
		int var3 = (int)this.z;
		return this.level.isLit(var1, var2, var3);
	}

	public float getBrightness() {
		int x = (int)this.x;
		int y = (int)(this.y + this.heightOffset / 2.0F);
		int z = (int)this.z;
		return this.level.getBrightness(x, y, z);
	}

	public void setLevel(Level var1) {
		this.level = var1;
	}

	public void moveTo(float var1, float var2, float var3, float var4, float var5) {
		this.xo = this.x = var1;
		this.yo = this.y = var2;
		this.zo = this.z = var3;
		this.yRot = var4;
		this.xRot = var5;
		this.setPos(var1, var2, var3);
	}
}
