package com.hilkojj.game.utils;

public class Random {

	private static final java.util.Random R = new java.util.Random();

	public static float random() {
		return R.nextFloat();
	}

	public static float random(float min, float max) {

		return min + (max - min) * random();
	}

}
