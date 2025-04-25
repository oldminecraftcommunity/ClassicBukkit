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
		
	}
}
