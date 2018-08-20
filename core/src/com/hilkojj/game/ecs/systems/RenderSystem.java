package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.graphics.Drawable;

import static com.hilkojj.game.ecs.components.Drawables.DrawLayer.NUMBER_OF_LAYERS;

public class RenderSystem extends EntitySystem {

	private OrthographicCamera camera;
	private SpriteBatch batch = new SpriteBatch();
	private ImmutableArray<Entity> entities;
	private ComponentMapper<Drawables> mapper = ComponentMapper.getFor(Drawables.class);

	public RenderSystem(OrthographicCamera camera) {
		super(-999999999);
		this.camera = camera;
	}

	@Override
	public void update(float deltaTime) {

		long startTime = System.currentTimeMillis();

		batch.setProjectionMatrix(camera.combined);

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


}
