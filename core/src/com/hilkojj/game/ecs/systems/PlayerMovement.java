package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.hilkojj.game.Input;
import com.hilkojj.game.ecs.components.movement.Movement;
import com.hilkojj.game.ecs.components.movement.PlayerInput;

public class PlayerMovement extends IteratingSystem {

	private ComponentMapper<Movement> mapper = ComponentMapper.getFor(Movement.class);

	public PlayerMovement() {
		super(Family.all(Movement.class, PlayerInput.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Movement m = mapper.get(entity);

		m.up = Input.UP.down;
		m.down = Input.DOWN.down;
		m.left = Input.LEFT.down;
		m.right = Input.RIGHT.down;
	}

}
