package com.hilkojj.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.hilkojj.game.utils.AABB;

public class DebugShapes implements Component {

	public Array<AABB> aabbs;

	public DebugShapes addAabb(AABB aabb) {
		if (aabbs == null) aabbs = new Array<>();

		aabbs.add(aabb);

		return this;
	}

}
