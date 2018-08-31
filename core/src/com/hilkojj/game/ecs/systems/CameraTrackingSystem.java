package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.CameraTracking;
import com.hilkojj.game.level.Room;

import static com.hilkojj.game.level.Room.CHUNK_HEIGHT;
import static com.hilkojj.game.level.Room.CHUNK_WIDTH;

public class CameraTrackingSystem extends IteratingSystem {

	private ECSScreen ecs;
	private ComponentMapper<CameraTracking> mapper = ComponentMapper.getFor(CameraTracking.class);
	private Vector2 position = new Vector2();

	public CameraTrackingSystem(ECSScreen ecs) {

		super(Family.all(CameraTracking.class).get());

		this.ecs = ecs;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		CameraTracking c = mapper.get(entity);

		position.lerp(c.position, (float) (1 - Math.pow(c.smooth, deltaTime)));

		Room r = ecs.getRoom();
		if (r != null) {

			float xMargin = ecs.camera.viewportWidth / 2f;
			float yMargin = ecs.camera.viewportHeight / 2f;

			position.x = Math.max(xMargin, position.x);
			position.x = Math.min(r.xChunks * CHUNK_WIDTH - xMargin, position.x);

			position.y = Math.max(yMargin, position.y);
			position.y = Math.min(r.yChunks * CHUNK_HEIGHT - yMargin, position.y);
		}

		ecs.camera.position.set(position.x, position.y, 0);
		ecs.camera.update();

	}

}
