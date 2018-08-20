package com.hilkojj.game;

import com.badlogic.gdx.Screen;
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

	}

	@Override
	public void render(float delta) {
		doneLoading();
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
