package com.hilkojj.game.utils;

import com.badlogic.gdx.math.Vector2;

public class AABB {

	public final Vector2 center, halfSize;

	public AABB(Vector2 center, Vector2 halfSize) {
		this.center = center;
		this.halfSize = halfSize;
	}

	public boolean overlaps(AABB other) {
		return !(Math.abs(center.x - other.center.x) > halfSize.x + other.halfSize.x)
				&& !(Math.abs(center.y - other.center.y) > halfSize.y + other.halfSize.y);
	}

	public boolean pointInAABB(int x, int y) {
		return x <= center.x + halfSize.x && x >= center.x - halfSize.x
			&& y <= center.y + halfSize.y && y >= center.y - halfSize.y;
	}

}
