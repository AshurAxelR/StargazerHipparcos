package com.xrbpowered.utils;

public abstract class MathUtils {

	public static double snap(double s) {
		return snap(s, 0.0, 1.0);
	}

	public static double snap(double s, double min, double max) {
		if(s<min)
			return min;
		else if(s>max)
			return max;
		else
			return s;
	}

	public static int snap(int s, int min, int max) {
		if(s<min)
			return min;
		else if(s>max)
			return max;
		else
			return s;
	}
	
	public static double lerp(double x0, double x1, double s) {
		return x0 * (1.0-s) + x1 * s;
	}
	
	public static float lerp(float x0, float x1, float s) {
		return x0 * (1f-s) + x1 * s;
	}
	
	public static double cosInt(double s) {
		return (1.0 - Math.cos(s * Math.PI)) * 0.5;
	}
	
	public static float cosInt(float s) {
		return (1f - (float)Math.cos(s * Math.PI)) * 0.5f;
	}

}
