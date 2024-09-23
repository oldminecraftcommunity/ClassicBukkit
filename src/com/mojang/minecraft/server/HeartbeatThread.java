package com.mojang.minecraft.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

final class HeartbeatThread extends Thread {
	private String content;
	private MinecraftServer minecraft;

	HeartbeatThread(MinecraftServer var1, String var2) {
		this.minecraft = var1;
		this.content = var2;
	}

	public final void run() {
		/*HttpURLConnection var1 = null;

		try {
			URL var2 = new URL("http://www.minecraft.net/heartbeat.jsp");
			var1 = (HttpURLConnection)var2.openConnection();
			var1.setRequestMethod("POST");
			var1.setDoOutput(true);
			var1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			var1.setRequestProperty("Content-Length", "" + Integer.toString(this.content.getBytes().length));
			var1.setRequestProperty("Content-Language", "en-US");
			var1.setUseCaches(false);
			var1.setDoInput(true);
			var1.setDoOutput(true);
			var1.connect();
			DataOutputStream var9 = new DataOutputStream(var1.getOutputStream());
			var9.writeBytes(this.content);
			var9.flush();
			var9.close();
			BufferedReader var10 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
			String var3 = var10.readLine();
			if(!MinecraftServer.b(this.minecraft).equals(var3)) {
				MinecraftServer.logger.info("To connect directly to this server, surf to: " + var3);
				PrintWriter var4 = new PrintWriter(new FileWriter("externalurl.txt"));
				var4.println(var3);
				var4.close();
				MinecraftServer.logger.info("(This is also in externalurl.txt)");
				MinecraftServer.a(this.minecraft, var3);
			}

			var10.close();
			return;
		} catch (Exception var7) {
			MinecraftServer.logger.severe("Failed to assemble heartbeat: " + var7);
			var7.printStackTrace();
		} finally {
			if(var1 != null) {
				var1.disconnect();
			}

		}*/
		System.out.println("Server is alive, heartbeat");
	}
}
