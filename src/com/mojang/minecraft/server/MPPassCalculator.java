package com.mojang.minecraft.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MPPassCalculator {
	private String salt;

	public MPPassCalculator(String var1) {
		this.salt = var1;
	}

	public final String calcMPass(String var1) {
		try {
			String var3 = this.salt + var1;
			MessageDigest var4 = MessageDigest.getInstance("MD5");
			var4.update(var3.getBytes(), 0, var3.length());
			return (new BigInteger(1, var4.digest())).toString(16);
		} catch (NoSuchAlgorithmException var2) {
			throw new RuntimeException(var2);
		}
	}
}
