package com.hilkojj.game;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Created by hilkojj.
 */
public enum Input {

	LEFT(A),
	RIGHT(D),
	UP(SPACE),
	DOWN(S),
	TOGGLE_FULLSCREEN(F11),
	SLOW_DOWN(T);

	private static final Input[] VALUES = values();

	public int value;
	public boolean hit, down, up;

	Input(int defaultValue) {

		value = defaultValue;

	}

	public static void update() {

		for (Input input : VALUES) {
			input.hit = false;
			input.up = false;
			if (input.down && !Gdx.input.isKeyPressed(input.value)) {
				input.down = false;
				input.up = true;
				continue;
			}
			if (!input.down && Gdx.input.isKeyPressed(input.value)) {
				input.down = true;
				input.hit = true;
			}
		}

	}

}
