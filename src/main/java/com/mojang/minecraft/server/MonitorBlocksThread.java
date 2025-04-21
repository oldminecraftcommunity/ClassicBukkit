package com.mojang.minecraft.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.zip.GZIPOutputStream;

final class MonitorBlocksThread extends Thread {
	private byte[] blocks;
	private PlayerInstance playerInstance;

	MonitorBlocksThread(PlayerInstance var1, byte[] var2) {
		this.playerInstance = var1;
		this.blocks = var2;
	}

	public final void run() {
		try {
			ByteArrayOutputStream var1 = new ByteArrayOutputStream();
			Thread.sleep(500L);
			ByteArrayOutputStream var3 = var1;
			byte[] var2 = this.blocks;

			try {
				DataOutputStream var6 = new DataOutputStream(new GZIPOutputStream(var3));
				var6.writeInt(var2.length);
				var6.write(var2);
				var6.close();
			} catch (Exception var4) {
				throw new RuntimeException(var4);
			}

			Thread.sleep(500L);
			this.playerInstance.setBlocks(var1.toByteArray());
		} catch (InterruptedException var5) {
		}
	}
}
