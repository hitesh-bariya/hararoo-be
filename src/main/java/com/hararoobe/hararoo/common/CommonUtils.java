package com.hararoobe.hararoo.common;

import java.util.Base64;

public class CommonUtils {

	public static String encode(String input) {
		if (input == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(input);
		sb.reverse();
		String reverse = sb.toString();
		byte[] data = Base64.getEncoder().encode(reverse.getBytes());
		return new String(data);
	}
}
