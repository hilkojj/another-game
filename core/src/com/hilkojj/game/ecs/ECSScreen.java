package com.hilkojj.game.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hilkojj.game.ecs.entities.Bat;
import com.hilkojj.game.ecs.systems.CameraTrackingSystem;
import com.hilkojj.game.ecs.systems.RenderSystem;

public class ECSScreen implements Screen {

	private Engine engine;
	private OrthographicCamera camera = new OrthographicCamera(320 / 16f, 180 / 16f);

	@Override
	public void show() {
		engine = new Engine();

		engine.addSystem(new CameraTrackingSystem(camera));
		engine.addSystem(new RenderSystem(camera));

		engine.addEntity(new Bat());
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
		camera.setToOrtho(false, 13.5F * width / height, 13.5F);
		camera.update();
	}

}
