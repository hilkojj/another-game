package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.DebugShapes;
import com.hilkojj.game.level.Room;
import com.hilkojj.game.utils.AABB;

public class DebugSystem extends EntitySystem {

	private ECSScreen ecs;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private ImmutableArray<Entity> entities;
	private ComponentMapper<DebugShapes> mapper = ComponentMapper.getFor(DebugShapes.class);

	public DebugSystem(ECSScreen ecs) {
		super(999999999);
		this.ecs = ecs;
		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void update(float deltaTime) {
		shapeRenderer.setProjectionMatrix(ecs.camera.combined);

		shapeRenderer.begin();
		shapeRenderer.setColor(Color.RED);
		if (ecs.getRoom() != null) drawRoom(ecs.getRoom(), shapeRenderer);

		shapeRenderer.setColor(Color.BLUE);
		for (Entity e : entities) {

			DebugShapes s = mapper.get(e);

			for (AABB aabb : s.aabbs) drawAabb(aabb, shapeRenderer);
		}

		shapeRenderer.end();
	}

	private void drawAabb(AABB aabb, ShapeRenderer renderer) {

//			1
//		_________
//		|		|
//		|		|
//		|2  	| 3
//		|		|
//		_________
//			4

		// 1
		renderer.line(
				aabb.center.x - aabb.halfSize.x, aabb.center.y + aabb.halfSize.y,
				aabb.center.x + aabb.halfSize.x, aabb.center.y + aabb.halfSize.y
		);

		// 2
		renderer.line(
				aabb.center.x - aabb.halfSize.x, aabb.center.y + aabb.halfSize.y,
				aabb.center.x - aabb.halfSize.x, aabb.center.y - aabb.halfSize.y
		);

		// 3
		renderer.line(
				aabb.center.x + aabb.halfSize.x, aabb.center.y + aabb.halfSize.y,
				aabb.center.x + aabb.halfSize.x, aabb.center.y - aabb.halfSize.y
		);

		// 4
		renderer.line(
				aabb.center.x - aabb.halfSize.x, aabb.center.y - aabb.halfSize.y,
				aabb.center.x + aabb.halfSize.x, aabb.center.y - aabb.halfSize.y
		);
	}

	private void drawRoom(Room room, ShapeRenderer renderer) {

		renderer.setColor(Color.RED);

		for (int x = 0; x < room.xChunks * Room.CHUNK_WIDTH; x++) {

			for (int y = 0; y < room.yChunks * Room.CHUNK_HEIGHT; y++) {

				if (room.getBlock(x, y) == Room.Block.AIR) continue;

				renderer.line(x, y, x + 1, y);
				renderer.line(x, y + 1, x + 1, y + 1);
				renderer.line(x, y, x, y + 1);
				renderer.line(x + 1, y, x + 1, y + 1);
			}
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(DebugShapes.class).get());
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entities = null;
	}

}
