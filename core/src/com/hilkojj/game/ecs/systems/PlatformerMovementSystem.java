package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.hilkojj.game.ecs.components.movement.Movement;
import com.hilkojj.game.ecs.components.movement.PlatformerMovement;

import static com.hilkojj.game.ecs.components.movement.PlatformerMovement.State.*;

public class PlatformerMovementSystem extends IteratingSystem {

	private ComponentMapper<Movement> movementMapper = ComponentMapper.getFor(Movement.class);
	private ComponentMapper<PlatformerMovement> platformerMapper = ComponentMapper.getFor(PlatformerMovement.class);

	public PlatformerMovementSystem() {
		super(Family.all(PlatformerMovement.class, Movement.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Movement m = movementMapper.get(entity);
		PlatformerMovement pM = platformerMapper.get(entity);

		PlatformerMovement.State prevState;

		do {

			prevState = pM.state;

			switch (pM.state) {

				case IDLE:
					idle(m, pM);
					break;
				case WALKING:
					walking(m, pM);
					break;
				case JUMPING:
					jumping(m, pM, deltaTime);
					break;
				case FALLING:
					falling(m, pM, deltaTime);
					break;
			}

			pM.stateJustChanged = prevState != pM.state;
			if (pM.stateJustChanged) System.out.println(pM.state.toString());

		} while (prevState != pM.state);
	}

	private void idle(Movement m, PlatformerMovement pM) {

		if (m.up) pM.state = JUMPING;
		else if (!pM.body.onGround) pM.state = FALLING;
		else if (left(m, pM) != right(m, pM)) pM.state = WALKING;

		pM.body.velocity.setZero();
	}

	private void walking(Movement m, PlatformerMovement pM) {

		boolean left = left(m, pM), right = right(m, pM);

		if (left == right) {
			pM.state = IDLE;
			return;
		} else if (m.up) {
			pM.state = JUMPING;
			return;
		} else if (!pM.body.onGround) {
			pM.state = FALLING;
			return;
		}

		pM.body.velocity.set(left ? -pM.walkSpeed : pM.walkSpeed, 0);
	}

	private void jumping(Movement m, PlatformerMovement pM, float deltaTime) {

		if (pM.stateJustChanged) {

			pM.jumpVelocity = pM.initialJumpVelocity;
			pM.jumpInitialPress = true;
			return;
		}

		pM.jumpInitialPress &= m.up;

		float gravity = pM.gravity;
		if (!pM.jumpInitialPress)
			gravity *= 4;
		else if (pM.body.touchesCeiling)
			gravity *= 2;

		pM.jumpVelocity += gravity * deltaTime;

		pM.body.velocity.set(
				airXVelocity(m, pM, deltaTime),
				pM.body.touchesCeiling ? 0 : pM.jumpVelocity
		);

		if (pM.jumpVelocity <= 0)
			pM.state = FALLING;

	}

	private void falling(Movement m, PlatformerMovement pM, float deltaTime) {

		if (pM.body.onGround) {
			pM.state = left(m, pM) == right(m, pM) ? IDLE : WALKING;
			return;
		}

		pM.body.velocity.set(airXVelocity(m, pM, deltaTime), pM.body.velocity.y + pM.gravity * deltaTime);
	}

	private boolean left(Movement m, PlatformerMovement pM) {
		return m.left && !pM.body.touchesLeftWall;
	}

	private boolean right(Movement m, PlatformerMovement pM) {
		return m.right && !pM.body.touchesRightWall;
	}

	private float airXVelocity(Movement m, PlatformerMovement pM, float deltaTime) {

		boolean left = left(m, pM), right = right(m, pM);

		return left == right ? 0 : (
				left ?
						Math.max(-pM.walkSpeed, pM.body.velocity.x - pM.airXAcceleration * deltaTime)
						:
						Math.min(+pM.walkSpeed, pM.body.velocity.x + pM.airXAcceleration * deltaTime)
		);
	}

}
