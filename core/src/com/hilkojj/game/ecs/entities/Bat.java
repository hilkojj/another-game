package com.hilkojj.game.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.components.CameraTracking;
import com.hilkojj.game.ecs.components.DebugShapes;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.ecs.components.Physics;
import com.hilkojj.game.graphics.DrawableSprite;

public class Bat extends Entity {

	public Bat() {

		add(
				new Drawables().addDrawable(new DrawableSprite("sprites/bat.png"), Drawables.DrawLayer.MAIN)
		);

		Vector2 position = new Vector2();

		add(
				new CameraTracking(position)
		);

		Physics p = new Physics(1);

		Physics.Body b = new Physics.Body(position, new Vector2(.2f, .2f), false);

		p.bodies.add(b);

		add(p);

		add(new DebugShapes().addAabb(b));
	}

}
