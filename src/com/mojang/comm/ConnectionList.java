package com.mojang.comm;

import com.mojang.minecraft.server.MinecraftServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.LinkedList;
import java.util.List;

public final class ConnectionList {
	public ServerSocketChannel serverSocketChannel;
	public MinecraftServer minecraft;
	public List connectionList = new LinkedList();

	public ConnectionList(int var1, MinecraftServer var2) throws IOException {
		this.minecraft = var2;
		this.serverSocketChannel = ServerSocketChannel.open();
		this.serverSocketChannel.socket().bind(new InetSocketAddress(var1));
		this.serverSocketChannel.configureBlocking(false);
	}
}
