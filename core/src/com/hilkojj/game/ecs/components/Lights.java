package com.hilkojj.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Lights extends Array<Lights.Light> implements Component {

	public static class Light {

		public Vector2 pos;
		public float radius;
		public Color color;

		public Light(Vector2 pos, float radius, Color color) {
			this.pos = pos;
			this.radius = radius;
			this.color = color;
		}

	}

}
