package com.mojang.minecraft.net;

public final class Packet {
	public static final Packet[] PACKETS = new Packet[256];
	public static final Packet LOGIN = new Packet(new Class[]{Byte.TYPE, String.class, String.class, Byte.TYPE});
	public static final Packet TIMED_OUT = new Packet(new Class[0]);
	public static final Packet LEVEL_INITIALIZE = new Packet(new Class[0]);
	public static final Packet LEVEL_DATA_CHUNK = new Packet(new Class[]{Short.TYPE, byte[].class, Byte.TYPE});
	public static final Packet LEVEL_FINALIZE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE});
	public static final Packet PLACE_OR_REMOVE_TILE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet SET_TILE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE});
	public static final Packet PLAYER_JOIN = new Packet(new Class[]{Byte.TYPE, String.class, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_TELEPORT = new Packet(new Class[]{Byte.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_MOVE_AND_ROTATE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_MOVE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_ROTATE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_DISCONNECT = new Packet(new Class[]{Byte.TYPE});
	public static final Packet CHAT_MESSAGE = new Packet(new Class[]{Byte.TYPE, String.class});
	public static final Packet KICK_PLAYER = new Packet(new Class[]{String.class});
	public final int size;
	private static int nextId = 0;
	public final byte id = (byte)(nextId++);
	public Class[] fields;

	private Packet(Class... var1) {
		PACKETS[this.id] = this;
		this.fields = new Class[var1.length];
		int var2 = 0;

		for(int var3 = 0; var3 < var1.length; ++var3) {
			Class var4 = var1[var3];
			this.fields[var3] = var4;
			if(var4 == Long.TYPE) {
				var2 += 8;
			} else if(var4 == Integer.TYPE) {
				var2 += 4;
			} else if(var4 == Short.TYPE) {
				var2 += 2;
			} else if(var4 == Byte.TYPE) {
				++var2;
			} else if(var4 == Float.TYPE) {
				var2 += 4;
			} else if(var4 == Double.TYPE) {
				var2 += 8;
			} else if(var4 == byte[].class) {
				var2 += 1024;
			} else if(var4 == String.class) {
				var2 += 64;
			}
		}

		this.size = var2;
	}
}
