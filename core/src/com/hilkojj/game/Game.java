package com.hilkojj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class Game extends com.badlogic.gdx.Game {

	public AssetManager assetManager;

	public static long renderTime;

	@Override
	public void create () {

		assetManager = new AssetManager();

		setScreen(new LoadingScreen(this));

	}

	@Override
	public void render () {

		super.render();

		Gdx.graphics.setTitle("fps: " + Gdx.graphics.getFramesPerSecond() + " RenderTime: " + renderTime + "ms");

	}

}
