package com.hilkojj.game.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hilkojj.game.ecs.entities.JavelinThrower;
import com.hilkojj.game.ecs.systems.*;
import com.hilkojj.game.level.Room;

public class ECSScreen implements Screen {

	private Engine engine;
	private Room room;

	public final OrthographicCamera camera = new OrthographicCamera(320 / 16f, 180 / 16f);

	@Override
	public void show() {
		engine = new Engine();

		engine.addSystem(new PhysicsSystem(this));
		engine.addSystem(new CameraTrackingSystem(this));
//		engine.addSystem(new RenderSystem(this));
		engine.addSystem(new DebugSystem(this));
		engine.addSystem(new PlayerMovement());
		engine.addSystem(new PlatformerMovementSystem());
		engine.addSystem(new ParticleSystem());
		engine.addSystem(new LandingParticlesSystem(this));
		engine.addSystem(new LightsSystem(this));

		engine.addEntity(new JavelinThrower());

		room = new Room("rooms/testroom.tmx");
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
