package com.hilkojj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.hilkojj.game.loading.LoadingScreen;

public class Game extends com.badlogic.gdx.Game {

	public static final int
			PPM = 16, // pixels per meter
			WIDTH = PPM * 24,
			HEIGHT = WIDTH / 16 * 9;

	public static AssetManager assetManager;
	public static long renderTime;

	@Override
	public void create () {

		assetManager = new AssetManager();

		setScreen(new LoadingScreen(this));

	}

	@Override
	public void render () {

		Input.update();

		super.render();

		Gdx.graphics.setTitle("fps: " + Gdx.graphics.getFramesPerSecond() + " RenderTime: " + renderTime + "ms");

	}

}
