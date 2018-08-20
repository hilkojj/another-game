package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hilkojj.game.ecs.components.CameraTracking;

public class CameraTrackingSystem extends IteratingSystem {

	private OrthographicCamera camera;

	public CameraTrackingSystem(OrthographicCamera camera) {

		super(Family.all(CameraTracking.class).get());

		this.camera = camera;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		camera.position.set(0, 0, 0);
		camera.update();

	}

}
