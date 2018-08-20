package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.graphics.Drawable;

import static com.hilkojj.game.ecs.components.Drawables.DrawLayer.NUMBER_OF_LAYERS;

public class RenderSystem extends EntitySystem {

	private SpriteBatch batch = new SpriteBatch();
	private ImmutableArray<Entity> entities;
	private ComponentMapper<Drawables> mapper = ComponentMapper.getFor(Drawables.class);

	@Override
	public void update(float deltaTime) {

		long startTime = System.currentTimeMillis();

		// start rendering:
		batch.begin();

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
