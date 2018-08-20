package com.hilkojj.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class CameraTracking implements Component {

	public Vector2 position;

	public CameraTracking(Vector2 position) {
		this.position = position;
	}

}
