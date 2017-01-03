package com.visellico.util;

public class StringUtils {

	private StringUtils() {
	}
	
	public static String capOnlyFirst(String str) {
		
		if (str.length() < 2) return str.toUpperCase();
		
		String result = str.substring(0,1).toUpperCase();
		result = result + str.substring(1).toLowerCase();
		
		return result;
	}
	
}
