package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.hilkojj.game.ecs.components.Physics;

public class PhysicsSystem extends IteratingSystem {

	public PhysicsSystem() {
		super(Family.all(Physics.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

	}

}
