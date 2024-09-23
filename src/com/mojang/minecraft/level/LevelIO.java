package com.mojang.minecraft.level;

import com.mojang.minecraft.server.MinecraftServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class LevelIO {
	private MinecraftServer minecraft;

	public LevelIO(MinecraftServer var1) {
		this.minecraft = var1;
	}

	public final Level load(InputStream var1) {
		if(this.minecraft != null) {
			this.minecraft.beginLevelLoading("Loading level");
		}

		if(this.minecraft != null) {
			this.minecraft.levelLoadUpdate("Reading..");
		}

		try {
			DataInputStream var10 = new DataInputStream(new GZIPInputStream(var1));
			int var12 = var10.readInt();
			if(var12 != 656127880) {
				return null;
			} else {
				byte var13 = var10.readByte();
				if(var13 > 2) {
					return null;
				} else if(var13 <= 1) {
					System.out.println("Version is 1!");
					String var15 = var10.readUTF();
					String var16 = var10.readUTF();
					long var7 = var10.readLong();
					short var3 = var10.readShort();
					short var4 = var10.readShort();
					short var5 = var10.readShort();
					byte[] var6 = new byte[var3 * var4 * var5];
					var10.readFully(var6);
					var10.close();
					Level var11 = new Level();
					var11.setData(var3, var5, var4, var6);
					var11.name = var15;
					var11.creator = var16;
					var11.createTime = var7;
					return var11;
				} else {
					ObjectInputStream var14 = new ObjectInputStream(var10);
					Level var2 = (Level)var14.readObject();
					var2.initTransient();
					var14.close();
					return var2;
				}
			}
		} catch (Exception var9) {
			var9.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(var9.toString()).toString();
			return null;
		}
	}

	public static void save(Level var0, OutputStream var1) {
		try {
			DataOutputStream var3 = new DataOutputStream(new GZIPOutputStream(var1));
			var3.writeInt(656127880);
			var3.writeByte(2);
			ObjectOutputStream var4 = new ObjectOutputStream(var3);
			var4.writeObject(var0);
			var4.close();
		} catch (Exception var2) {
			var2.printStackTrace();
		}
	}
}
