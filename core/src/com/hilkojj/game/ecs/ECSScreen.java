package com.hilkojj.game.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.hilkojj.game.ecs.systems.RenderSystem;

public class ECSScreen implements Screen {

	private Engine engine;

	@Override
	public void show() {
		engine = new Engine();

		engine.addSystem(new RenderSystem());
	}

	@Override
	public void render(float delta) {

		engine.update(delta);

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		engine = null;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {

	}

}
