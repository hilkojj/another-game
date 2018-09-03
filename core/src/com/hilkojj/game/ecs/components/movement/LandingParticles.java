package com.hilkojj.game.ecs.components.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.components.Physics;
import com.hilkojj.game.utils.AABB;

public class LandingParticles implements Component {

	public final AABB particlesSpawnArea = new AABB(new Vector2(), new Vector2());
	public Physics.Body[] bodies;

	public LandingParticles(Physics.Body... bodies) {
		this.bodies = bodies;
	}

}
