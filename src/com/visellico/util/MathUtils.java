package com.visellico.util;

public class MathUtils {
	
	private MathUtils() {
	}
	
	public static int smaller(int value, int min) {
		return value < min ? value : min;
	}
	
	public static int greater(int value, int max) {
		return value > max ? value : max;
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double dist = Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
		return dist;
	}
	
	public static int parseInt(String str) {
		
		//Regex is basically magic
		
		if (str.matches("(-?[0-9]+)")) {
			return Integer.parseInt(str);
		}
		
		return 0;
	}
	
	/**
	 * Returns the closest int from the given value inside of the range
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static int clamp(int value, int min, int max) {
		if (value < min) return min;
		if (value > max) return max;
		return value;
	}
	
	/**
	 * Returns the closest int from the given value inside of the range
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static double clamp(double value, double min, double max) {
		if (value < min) return min;
		if (value > max) return max;
		return value;
	}
	
}
