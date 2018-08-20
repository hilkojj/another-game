package com.hilkojj.game.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.graphics.DrawableSprite;

public class Bat extends Entity {

	public Bat() {

		add(
				new Drawables().addDrawable(new DrawableSprite("sprites/bat.png"), Drawables.DrawLayer.MAIN)
		);

	}

}
