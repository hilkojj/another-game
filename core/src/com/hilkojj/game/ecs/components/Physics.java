package com.hilkojj.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hilkojj.game.utils.AABB;

public class Physics implements Component {

	public Array<Body> bodies;

	/**
	 * Physics.
	 *
	 * @param bodyCapacity The number of bodies the entity will have
	 */
	public Physics(int bodyCapacity) {
		bodies = new Array<>(bodyCapacity);
	}

	public static class Body extends AABB {

		public enum PositionCorrection {
			NONE,
			TO_STEP_POSITION,
			CORRECT_POSITION
		}

		public boolean active = true;

		public final Vector2
				prevCenter = new Vector2(),
				prevVelocity = new Vector2(),
				velocity = new Vector2();

		public PositionCorrection positionCorrection = PositionCorrection.CORRECT_POSITION;
		public float step = .6f;

		public boolean
				touchedRightWall,
				touchesRightWall,

				touchedLeftWall,
				touchesLeftWall,

				wasOnGround,
				onGround,

				touchedCeiling,
				touchesCeiling,

				collidesWithPlatforms;

		public Body(Vector2 center, Vector2 halfSize, boolean collidesWithPlatforms) {

			super(center, halfSize);

			this.collidesWithPlatforms = collidesWithPlatforms;

		}

	}

}
