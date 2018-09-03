package com.hilkojj.game.utils;

public class MathUtilsss {

	private MathUtilsss() {}

	public static float map(float x, float minIn, float maxIn, float minOut, float maxOut) {
		return (x - minIn) * (maxOut - minOut) / (maxIn - minIn) + minOut;
	}

}
