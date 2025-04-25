package com.mojang.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

final class ConsoleInput extends Thread {
	private MinecraftServer minecraft;

	ConsoleInput(MinecraftServer var1) {
		this.minecraft = var1;
	}

	public void run() {
		try {
			BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));
			String s = null;

			while (true) {
				s = var1.readLine();
				if(s == null) {
					MinecraftServer.logger.warning("stdin: end of file! No more direct console input is possible.");
					return;
				}

				List<String> var3 = MinecraftServer.getConsoleCommands(this.minecraft);
				synchronized (var3) {
					MinecraftServer.getConsoleCommands(this.minecraft).add(s);
				}
			}
		} catch (IOException var5) {
			MinecraftServer.logger.warning("stdin: ioexception! No more direct console input is possible.");
			var5.printStackTrace();
		}
	}
}
