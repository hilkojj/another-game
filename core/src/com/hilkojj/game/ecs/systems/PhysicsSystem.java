package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.Physics;
import com.hilkojj.game.level.Room;

import static com.hilkojj.game.ecs.components.Physics.Body.PositionCorrection.CORRECT_POSITION;
import static com.hilkojj.game.ecs.components.Physics.Body.PositionCorrection.NONE;
import static com.hilkojj.game.ecs.components.Physics.Body.PositionCorrection.TO_STEP_POSITION;

public class PhysicsSystem extends IteratingSystem {

	private ECSScreen ecs;

	public PhysicsSystem(ECSScreen ecs) {
		super(Family.all(Physics.class).get());

		this.ecs = ecs;
	}

	private ComponentMapper<Physics> mapper = ComponentMapper.getFor(Physics.class);

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Physics p = mapper.get(entity);

		for (Physics.Body b : p.bodies) if (b.active) updateBody(b, deltaTime);

	}

	// Vectors that will be reused:
	private final Vector2
			deltaPos = new Vector2(),
			stepPos = new Vector2();

	private void updateBody(Physics.Body body, float deltaTime) {

		body.prevCenter.set(body.center);
		body.prevVelocity.set(body.velocity);
		body.touchedRightWall = body.touchesRightWall;
		body.touchedLeftWall = body.touchesLeftWall;
		body.wasOnGround = body.onGround;
		body.touchedCeiling = body.touchesCeiling;

		// update position using current velocity
		body.center.add(body.velocity.x * deltaTime, body.velocity.y * deltaTime);

		deltaPos.set(body.center).sub(body.prevCenter);
		float distTravelled = deltaPos.len();
		boolean collided = false;

		stepPos.set(body.prevCenter);

		if (body.positionCorrection != NONE) {

			for (float dist = body.step; dist < distTravelled && !collided; dist += body.step) {

				stepPos.add(
						deltaPos.x * (body.step / distTravelled), deltaPos.y * (body.step / distTravelled)
				);

				collided = collidesWithTerrain(body, stepPos);

				if (collided && body.positionCorrection == TO_STEP_POSITION)
					body.center.set(stepPos);
			}
		}

		if (!collided) {
			stepPos.set(body.center);
			collided = collidesWithTerrain(body, stepPos);
		}

		if (collided && body.positionCorrection == CORRECT_POSITION)
			body.center.set(stepPos);
	}

	// Vectors that will be reused:
	private final Vector2
			correctY = new Vector2(),
			correctX = new Vector2();

	private boolean collidesWithTerrain(Physics.Body body, Vector2 pos) {

		correctY.set(pos);
		boolean yCollided;
		if (body.velocity.y > 0) {
			yCollided = touchesCeiling(body, correctY);
			yCollided |= onGround(body, correctY);
		} else {
			yCollided = onGround(body, correctY);
			yCollided |= touchesCeiling(body, correctY);
		}

		correctX.set(pos);
		boolean xCollided = body.velocity.x < 0 ?
				touchesLeftWall(body, correctX) || touchesRightWall(body, correctX)
				:
				touchesRightWall(body, correctX) || touchesLeftWall(body, correctX);

		if (yCollided && xCollided) {

			if (Math.abs(correctY.y - pos.y) < Math.abs(correctX.x - pos.x)) {
				// y is least penetrated
				xCollided = false;
			} else {
				// x is least penetrated
				yCollided = false;
			}
		}

		if (yCollided) {

			pos.set(correctY);

			xCollided = touchesLeftWall(body, pos) || touchesRightWall(body, pos);

		} else if (xCollided) {

			pos.set(correctX);

			yCollided = onGround(body, pos) || touchesCeiling(body, pos);
		}

		return yCollided || xCollided;
	}

	private boolean onGround(Physics.Body body, Vector2 pos) {

		body.onGround = false;

		for (int i = 0; i < 2; i++) {

			int tileY = (int) (pos.y - body.halfSize.y - .00001F);

			if (i == 1) {
				int newTileY = (int) (pos.y - body.halfSize.y + .00001F); // + .00001F (7.999999995 -> 8.0000..)
				if (newTileY == tileY) break;
				tileY = newTileY;
			}

			for (int tileX = (int) (pos.x - body.halfSize.x);
				 tileX <= (int) (pos.x + body.halfSize.x); tileX++) {

				Room.Block block = ecs.getRoom().getBlock(tileX, tileY);
				Room.Block air = ecs.getRoom().getBlock(tileX, tileY + 1);

				if (block != Room.Block.AIR && air == Room.Block.AIR) {
					pos.y = tileY + 1 + body.halfSize.y;
					body.onGround = true;
				}
			}
		}
		return body.onGround;
	}

	private boolean touchesCeiling(Physics.Body body, Vector2 pos) {

		body.touchesCeiling = false;

		for (int i = 0; i < 2; i++) {

			int tileY = (int) (pos.y + body.halfSize.y + .0001F);

			if (i == 1) {
				int newTileY = (int) (pos.y + body.halfSize.y);
				if (newTileY == tileY) break;
				tileY = newTileY;
			}

			for (int tileX = (int) (pos.x - body.halfSize.x);
				 tileX <= (int) (pos.x + body.halfSize.x); tileX++) {

				Room.Block block = ecs.getRoom().getBlock(tileX, tileY);
				Room.Block air = ecs.getRoom().getBlock(tileX, tileY - 1);

				if (block != Room.Block.AIR && air == Room.Block.AIR) {
					pos.y = tileY - body.halfSize.y - .00001F;
					body.touchesCeiling = true;
				}
			}
		}
		return body.touchesCeiling;
	}

	private boolean touchesLeftWall(Physics.Body body, Vector2 pos) {

		body.touchesLeftWall = false;

		int tileX = (int) (pos.x - body.halfSize.x - .00001F);

		for (int tileY = (int) (pos.y - body.halfSize.y + .001f);
			 tileY <= (int) (pos.y + body.halfSize.y); tileY++) {

			Room.Block block = ecs.getRoom().getBlock(tileX, tileY);
			Room.Block air = ecs.getRoom().getBlock(tileX + 1, tileY);

			if (block != Room.Block.AIR && air == Room.Block.AIR) {
				pos.x = tileX + 1 + body.halfSize.x + .00001F;
				body.touchesLeftWall = true;
			}
		}

		return body.touchesLeftWall;
	}

	private boolean touchesRightWall(Physics.Body body, Vector2 pos) {

		body.touchesRightWall = false;

		int tileX = (int) (pos.x + body.halfSize.x + .00001F);

		for (int tileY = (int) (pos.y - body.halfSize.y + .001f);
			 tileY <= (int) (pos.y + body.halfSize.y); tileY++) {

			Room.Block block = ecs.getRoom().getBlock(tileX, tileY);
			Room.Block air = ecs.getRoom().getBlock(tileX - 1, tileY);

			if (block != Room.Block.AIR && air == Room.Block.AIR) {
				pos.x = tileX - body.halfSize.x - .00001F;
				body.touchesRightWall = true;
			}
		}
		return body.touchesRightWall;
	}

}
