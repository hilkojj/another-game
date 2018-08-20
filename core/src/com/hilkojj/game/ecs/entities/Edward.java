package com.hilkojj.game.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.graphics.DrawableSprite;

public class Edward extends Entity {

	public Edward() {

		add(
				new Drawables().addDrawable(new DrawableSprite(), Drawables.DrawLayer.MAIN)
		);

	}

}
