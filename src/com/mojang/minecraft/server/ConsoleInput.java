package com.mojang.minecraft.server;

import met.realfreehij.classicbukkit.ClassicBukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

final class ConsoleInput extends Thread {
	private MinecraftServer minecraft;

	ConsoleInput(MinecraftServer var1) {
		this.minecraft = var1;
	}

	public final void run() {
		try {
			BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));
			String var2 = null;

			while(true) {
				var2 = var1.readLine();
				if(var2 == null) {
					MinecraftServer.logger.warning("stdin: end of file! No more direct console input is possible.");
					return;
				}

				List var3 = MinecraftServer.a(this.minecraft);
				synchronized(var3) {
					MinecraftServer.a(this.minecraft).add(var2);
				}
			}
		} catch (IOException var5) {
			MinecraftServer.logger.warning("stdin: ioexception! No more direct console input is possible.");
			var5.printStackTrace();
		}
	}
}
