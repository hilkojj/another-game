package com.hilkojj.game.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.components.CameraTracking;
import com.hilkojj.game.ecs.components.DebugShapes;
import com.hilkojj.game.ecs.components.Physics;
import com.hilkojj.game.ecs.components.movement.Movement;
import com.hilkojj.game.ecs.components.movement.PlatformerMovement;
import com.hilkojj.game.ecs.components.movement.PlayerInput;

public class JavelinThrower extends Entity {

	public JavelinThrower() {

		Vector2 position = new Vector2(10, 10);

		add(
				new CameraTracking(position, .1f)
		);

		Physics p = new Physics(1);

		Physics.Body b = new Physics.Body(position, new Vector2(.3f, .9f), false);

		p.bodies.add(b);

		add(p);

		add(new DebugShapes().addAabb(b));

		add(
				new Movement()
		);

		add(
				new PlatformerMovement(
						b, 3.2f, 12, 2 * -9.8f, 10
				)
		);

		add(
				new PlayerInput()
		);
	}

}
