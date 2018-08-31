package com.hilkojj.game.loading;

import com.badlogic.gdx.Screen;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.ECSScreen;

public class LoadingScreen implements Screen {

	private final Game game;

	public LoadingScreen(Game game) {
		this.game = game;
	}

	public void doneLoading() {
		game.setScreen(new ECSScreen());
	}

	@Override
	public void show() {
		AssetLoader.loadAssets(Game.assetManager);
	}

	@Override
	public void render(float delta) {

		if (Game.assetManager.update()) {
			doneLoading();
			System.out.println(Game.assetManager.getLoadedAssets() + " assets loaded");
			return;
		}

		System.out.println("Loading progress: " + Game.assetManager.getProgress() * 100 + "%");

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
