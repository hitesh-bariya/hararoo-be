package com.hararoobe.hararoo.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

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
	
	public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
}
