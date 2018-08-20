package com.hilkojj.game.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.components.CameraTracking;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.graphics.DrawableSprite;

public class Bat extends Entity {

	public Bat() {

		add(
				new Drawables().addDrawable(new DrawableSprite("sprites/bat.png"), Drawables.DrawLayer.MAIN)
		);

		add(
				new CameraTracking(new Vector2())
		);

	}

}
