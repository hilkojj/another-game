package com.hilkojj.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.hilkojj.game.graphics.Drawable;


public class Drawables implements Component {

	public enum DrawLayer {

		MAIN(0);

		public final static int NUMBER_OF_LAYERS = DrawLayer.values().length;

		public final int index;

		DrawLayer(int index) {
			this.index = index;
		}

	}

	public final Collection[] collections = new Collection[DrawLayer.NUMBER_OF_LAYERS];

	public class Collection extends Array<Drawable> {

		Collection(int capacity) {
			super(capacity);
		}

	}

	public Drawables addDrawable(Drawable d, DrawLayer layer) {
		return addDrawable(d, layer, 8);
	}

	/**
	 * Use this function if you know the number of drawables the entity will draw on this layer
	 */
	public Drawables addDrawable(Drawable d, DrawLayer layer, int layerCapacity) {

		if (collections[layer.index] == null) collections[layer.index] = new Collection(layerCapacity);

		collections[layer.index].add(d);

		return this;
	}

	public boolean removeDrawable(Drawable d) {
		for (Collection c : collections) if (c != null && c.removeValue(d, true)) return true;
		return false;
	}

}
