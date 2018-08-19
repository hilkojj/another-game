package com.hilkojj.game.graphics;

public enum DrawLayer {

	MAIN(0);

	public final static int NUMBER_OF_LAYERS = values().length;

	public final int index;

	DrawLayer(int index) {
		this.index = index;
	}

}
