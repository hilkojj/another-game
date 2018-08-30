package com.hilkojj.game.ecs.components.movement;

import com.badlogic.ashley.core.Component;
import com.hilkojj.game.ecs.components.Physics;

public class PlatformerMovement implements Component {

	public enum State {
		IDLE,
		WALKING,
		JUMPING,
		FALLING
	}

	public State state = State.IDLE;
	public boolean stateJustChanged = true;
	public Physics.Body body;
	public float
			walkSpeed,
			initialJumpVelocity,
			gravity,
			airXAcceleration;

	public float jumpVelocity;
	public boolean jumpInitialPress;

	/**
	 * PlatformerMovement
	 *
	 * @param body                The body to be moved
	 * @param walkSpeed           How fast the body can walk
	 * @param initialJumpVelocity The initial y-velocity of a jump
	 * @param gravity             Gravity acceleration
	 * @param airXAcceleration    X acceleration when in air. (in m/s²) Note: x-velocity won't get higher than {@link #walkSpeed}
	 */
	public PlatformerMovement(Physics.Body body, float walkSpeed, float initialJumpVelocity, float gravity, float airXAcceleration) {
		this.body = body;
		this.walkSpeed = walkSpeed;
		this.initialJumpVelocity = initialJumpVelocity;
		this.gravity = gravity;
		this.airXAcceleration = airXAcceleration;
	}

}
