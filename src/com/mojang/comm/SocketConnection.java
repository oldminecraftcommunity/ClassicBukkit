package com.mojang.comm;

import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.server.PlayerInstance;
import java.io.IOException;
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

	public final void sendPacket(Packet packet, Object... data) {
		if(this.connected) {
			this.writeBuffer.put(packet.id);

			for(int di = 0; di < data.length; ++di) {
				Object dataValue = data[di];
				Class dataType = packet.fields[di];
				SocketConnection con = this;
				if(this.connected) {
					try {
						if(dataType == Long.TYPE) {
							con.writeBuffer.putLong(((Long)dataValue).longValue());
						} else if(dataType == Integer.TYPE) {
							con.writeBuffer.putInt(((Number)dataValue).intValue());
						} else if(dataType == Short.TYPE) {
							con.writeBuffer.putShort(((Number)dataValue).shortValue());
						} else if(dataType == Byte.TYPE) {
							con.writeBuffer.put(((Number)dataValue).byteValue());
						} else if(dataType == Double.TYPE) {
							con.writeBuffer.putDouble(((Double)dataValue).doubleValue());
						} else if(dataType == Float.TYPE) {
							con.writeBuffer.putFloat(((Float)dataValue).floatValue());
						} else if(dataType == byte[].class){
							byte[] arr = (byte[])((byte[])dataValue);
							if(arr.length < 1024) {
								arr = Arrays.copyOf(arr, 1024);
							}

							con.writeBuffer.put(arr);
						}else if(dataType == String.class){
							byte[] str = ((String)dataValue).getBytes("UTF-8");
							Arrays.fill(con.stringPacket, (byte)32); //XXX might be useless

							int si;
							for(si = 0; si < 64 && si < str.length; ++si) {
								con.stringPacket[si] = str[si];
							}

							for(si = str.length; si < 64; ++si) {
								con.stringPacket[si] = 32;
							}

							con.writeBuffer.put(con.stringPacket);
						}
					} catch (Exception e) {
						this.player.handleException(e);
					}
				}
			}

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
					return Short.valueOf(this.readBuffer.getShort());
				} else if(var1 == Byte.TYPE) {
					return Byte.valueOf(this.readBuffer.get());
				} else if(var1 == Double.TYPE) {
					return Double.valueOf(this.readBuffer.getDouble());
				} else if(var1 == Float.TYPE) {
					return Float.valueOf(this.readBuffer.getFloat());
				} else if(var1 == String.class) {
					this.readBuffer.get(this.stringPacket);
					return (new String(this.stringPacket, "UTF-8")).trim();
				} else if(var1 == byte[].class) {
					byte[] var3 = new byte[1024];
					this.readBuffer.get(var3);
					return var3;
				} else {
					return null;
				}
			} catch (Exception var2) {
				this.player.handleException(var2);
				return null;
			}
		}
	}
}
