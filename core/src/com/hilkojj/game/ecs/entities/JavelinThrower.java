package com.hilkojj.game.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.components.*;
import com.hilkojj.game.ecs.components.movement.LandingParticles;
import com.hilkojj.game.ecs.components.movement.Movement;
import com.hilkojj.game.ecs.components.movement.PlatformerMovement;
import com.hilkojj.game.ecs.components.movement.PlayerInput;

public class JavelinThrower extends Entity {

	public JavelinThrower() {

		Vector2 position = new Vector2(10, 10);

		add(
				new CameraTracking(position, 0.02f)
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
						b, 5.2f, 14, 2.8f * -9.8f, 10
				)
		);

		add(
				new PlayerInput()
		);

		Particles particles = new Particles();

		add(
				particles
		);

		add(
				new Drawables().addDrawable(particles, Drawables.DrawLayer.MAIN)
		);

		add(
				new LandingParticles(b)
		);


		Lights l = new Lights();
		l.add(new Lights.Light(
				b.center,
				8,
				new Color(.8f, .6f, .4f, 1)
		));
		l.add(new Lights.Light(
				new Vector2(17, 2),
				5,
				new Color(.2f, .5f, .2f, 1)
		));
		l.add(new Lights.Light(
				new Vector2(2, 4),
				3,
				new Color(.2f, .4f, .7f, 1)
		));
		add(l);
	}

}
