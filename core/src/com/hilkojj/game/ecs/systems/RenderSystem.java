package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.graphics.Drawable;
import com.hilkojj.game.level.Room;

import static com.hilkojj.game.ecs.components.Drawables.DrawLayer.NUMBER_OF_LAYERS;

public class RenderSystem extends EntitySystem {

	private ECSScreen esc;
	private SpriteBatch batch = new SpriteBatch();
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private ImmutableArray<Entity> entities;
	private ComponentMapper<Drawables> mapper = ComponentMapper.getFor(Drawables.class);

	public RenderSystem(ECSScreen esc) {
		super(-999999999);
		this.esc = esc;
		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void update(float deltaTime) {

		long startTime = System.currentTimeMillis();

		batch.setProjectionMatrix(esc.camera.combined);

		// start rendering:
		batch.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < NUMBER_OF_LAYERS; i++) {

			for (Entity e : entities) {

				Drawables ds = mapper.get(e);
				Drawables.Collection c = ds.collections[i];

				if (c == null) continue;

				for (Drawable d : c) d.draw(batch, deltaTime);
			}
		}

		// end rendering:
		batch.end();

		shapeRenderer.setProjectionMatrix(esc.camera.combined);

		shapeRenderer.begin();
		if (esc.getRoom() != null) renderRoomDebugLines(esc.getRoom(), shapeRenderer);
		shapeRenderer.end();

		Game.renderTime = System.currentTimeMillis() - startTime + 5;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(Drawables.class).get());
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entities = null;
	}

	private void renderRoomDebugLines(Room room, ShapeRenderer renderer) {

		renderer.setColor(Color.RED);

		for (int x = 0; x < room.xChunks * Room.CHUNK_WIDTH; x++) {

			for (int y = 0; y < room.yChunks * Room.CHUNK_HEIGHT; y++) {

				Room.BlockType b = room.blocks[x][y];

				if (b == null) continue;

				switch (b) {

					case SOLID:

						renderer.line(x, y, x + 1, y);
						renderer.line(x, y + 1, x + 1, y + 1);
						renderer.line(x, y, x, y + 1);
						renderer.line(x + 1, y, x + 1, y + 1);
						break;

					case PLATFORM:
						renderer.line(x, y + 1, x + 1, y + 1);
						break;

				}

			}
		}


	}

}
