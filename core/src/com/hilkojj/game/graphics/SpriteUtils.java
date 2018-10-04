package com.hilkojj.game.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteUtils {

	public static float[] setSpriteVerts(
			Sprite s,
			float x0, float y0, float x1, float y1,
			float x2, float y2, float x3, float y3
	) {
		float[] vertices = s.getVertices();
		vertices[SpriteBatch.X1] = x0;
		vertices[SpriteBatch.Y1] = y0;
		vertices[SpriteBatch.X4] = x1;
		vertices[SpriteBatch.Y4] = y1;
		vertices[SpriteBatch.X2] = x2;
		vertices[SpriteBatch.Y2] = y2;
		vertices[SpriteBatch.X3] = x3;
		vertices[SpriteBatch.Y3] = y3;
		return vertices;
	}

}
