package com.hilkojj.game.ecs.systems.rendering.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.components.Lights;

import static com.hilkojj.game.ecs.systems.rendering.light.LightsSystem.HALF_LIGHT_RES;
import static com.hilkojj.game.ecs.systems.rendering.light.LightsSystem.LIGHTS_PER_ROW;
import static com.hilkojj.game.ecs.systems.rendering.light.LightsSystem.LIGHT_RES;

public class LightMapGenerator {

	public static Texture lightMap;

	private FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGB888, Game.WIDTH, Game.HEIGHT, false);
	private SpriteBatch batch = new SpriteBatch();
	private Texture lightsGrid;

	void start(Texture lightsGrid, Camera c) {

		batch.setProjectionMatrix(c.combined);
		fbo.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		this.lightsGrid = lightsGrid;
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
	}

	void draw(Lights.Light l, int lightX, int lightY) {

		float halfSize = HALF_LIGHT_RES / Game.PPM;
		batch.setColor(l.color);
		batch.draw(
				lightsGrid,
				l.pos.x - halfSize,
				l.pos.y - halfSize,
				halfSize * 2, halfSize * 2,
				lightX * LIGHT_RES, LIGHT_RES * LIGHTS_PER_ROW - lightY * LIGHT_RES - LIGHT_RES,
				LIGHT_RES, LIGHT_RES,
				false, true
		);

	}

	void end() {
		batch.end();
		fbo.end();
		lightMap = fbo.getColorBufferTexture();
		lightMap.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		lightsGrid = null;
	}

}
