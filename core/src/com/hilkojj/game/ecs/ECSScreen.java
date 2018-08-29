package com.hilkojj.game.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hilkojj.game.ecs.entities.Bat;
import com.hilkojj.game.ecs.systems.CameraTrackingSystem;
import com.hilkojj.game.ecs.systems.PhysicsSystem;
import com.hilkojj.game.ecs.systems.RenderSystem;
import com.hilkojj.game.level.Room;

public class ECSScreen implements Screen {

	private Engine engine;
	private Room room;

	public final OrthographicCamera camera = new OrthographicCamera(320 / 16f, 180 / 16f);

	@Override
	public void show() {
		engine = new Engine();

		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new CameraTrackingSystem(camera));
		engine.addSystem(new RenderSystem(this));

		engine.addEntity(new Bat());

		room = new Room(3, 3);
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

	public Room getRoom() {
		return room;
	}

}
