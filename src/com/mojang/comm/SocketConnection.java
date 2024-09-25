package com.mojang.comm;

import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.server.PlayerInstance;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public final class SocketConnection {
	public volatile boolean connected;
	public SocketChannel socketChannel;
	public ByteBuffer readBuffer = ByteBuffer.allocate(1048576);
	public ByteBuffer writeBuffer = ByteBuffer.allocate(1048576);
	public PlayerInstance player;
	private Socket socket;
	private boolean initialized = false;
	public String ip;
	private byte[] stringPacket = new byte[64];

	public SocketConnection(SocketChannel var1) throws IOException {
		this.socketChannel = var1;
		this.socketChannel.configureBlocking(false);
		System.currentTimeMillis();
		this.socket = this.socketChannel.socket();
		this.connected = true;
		this.readBuffer.clear();
		this.writeBuffer.clear();
		this.socket.setTcpNoDelay(true);
		this.socket.setTrafficClass(24);
		this.socket.setKeepAlive(false);
		this.socket.setReuseAddress(false);
		this.socket.setSoTimeout(100);
		this.ip = this.socket.getInetAddress().toString();
	}

	public final void disconnect() {
		try {
			if(this.writeBuffer.position() > 0) {
				this.writeBuffer.flip();
				this.socketChannel.write(this.writeBuffer);
				this.writeBuffer.compact();
			}
		} catch (Exception var2) {
		}

		this.connected = false;

		try {
			this.socketChannel.close();
		} catch (Exception var1) {
		}

		this.socket = null;
		this.socketChannel = null;
	}

	public final void sendPacket(Packet packet) {
		if(this.connected) {
			this.writeBuffer.put((byte) packet.getPacketID().ordinal());
			packet.write(this);
		}
	}

	public Object read(Class var1) {
		if(!this.connected) {
			return null;
		} else {
			try {
				if(var1 == Long.TYPE) {
					return Long.valueOf(this.readBuffer.getLong());
				} else if(var1 == Integer.TYPE) {
					return Integer.valueOf(this.readBuffer.getInt());
				} else if(var1 == Short.TYPE) {
					return this.getShort();
				} else if(var1 == Byte.TYPE) {
					return Byte.valueOf(this.getByte());
				} else if(var1 == Double.TYPE) {
					return Double.valueOf(this.readBuffer.getDouble());
				} else if(var1 == Float.TYPE) {
					return Float.valueOf(this.readBuffer.getFloat());
				} else if(var1 == String.class) {
					return this.getString();
				} else if(var1 == byte[].class) {
					return this.getBytearray();
				} else {
					return null;
				}
			} catch (Exception var2) {
				this.player.handleException(var2);
				return null;
			}
		}
	}
	public String getString() {
		try {
			this.readBuffer.get(this.stringPacket);
			return (new String(this.stringPacket, "UTF-8")).trim();
		}catch(UnsupportedEncodingException e) {
			this.player.handleException(e);
			return null;
		}
	}
	public byte getByte() {
		return this.readBuffer.get();
	}

	public void writeByte(byte b) {
		this.writeBuffer.put(b);
	}

	public void writeString(String s) {
		try {
			byte[] str = s.getBytes("UTF-8");
			Arrays.fill(this.stringPacket, (byte)32); //XXX might be useless
	
			int si;
			for(si = 0; si < 64 && si < str.length; ++si) {
				this.stringPacket[si] = str[si];
			}
	
			for(si = str.length; si < 64; ++si) {
				this.stringPacket[si] = 32;
			}
	
			this.writeBuffer.put(this.stringPacket);
		}catch(UnsupportedEncodingException e) {
			this.player.handleException(e);
		}
	}

	public byte[] getBytearray() {
		byte[] var3 = new byte[1024];
		this.readBuffer.get(var3);
		return var3;
	}

	public short getShort() {
		return this.readBuffer.getShort();
	}

	public void writeBytearray(byte[] data) {
		if(data.length < 1024) {
			data = Arrays.copyOf(data, 1024);
		}

		this.writeBuffer.put(data);
	}

	public void writeShort(short s) {
		this.writeBuffer.putShort(s);
	}
	
	
}
