package com.mojang.minecraft.server;

import com.mojang.comm.SocketConnection;

public final class SendTimer {
	public SocketConnection netHandler;
	public int timer;

	public SendTimer(SocketConnection var1, int var2) {
		this.netHandler = var1;
		this.timer = 100;
	}
}
