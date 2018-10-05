package com.hilkojj.game.ecs.systems.rendering.light;

// just a few imports?

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.Lights;

public class LightsSystem extends IteratingSystem {

	static final int
			HALF_LIGHT_RES = 128,
			LIGHT_RES = 2 * HALF_LIGHT_RES,
			LIGHTS_PER_ROW = 4,
			FBO_RES = LIGHT_RES * LIGHTS_PER_ROW;

	private ECSScreen ecs;
	private ComponentMapper<Lights> mapper = ComponentMapper.getFor(Lights.class);

	private FrameBuffer fbo = new FrameBuffer(
			Pixmap.Format.RGBA8888,
			FBO_RES, FBO_RES,
			false
	);

	private ShaderProgram bordersShader = new ShaderProgram(
			Gdx.files.internal("glslshaders/default.vert").readString(),
			Gdx.files.internal("glslshaders/draw_in_borders.frag").readString()
	);
	private SpriteBatch batch = new SpriteBatch(1000, bordersShader);
	private SpriteBatch tempBatch = new SpriteBatch();
	private Texture lightTex;

	private ShadowRenderer shadowRenderer = new ShadowRenderer();

	public LightsSystem(ECSScreen ecs) {
		super(Family.all(Lights.class).get());
		this.ecs = ecs;

		lightTex = Game.assetManager.get("sprites/light.png", Texture.class);

		// print shader errors/warnings
		System.out.println("Border-shader log: \n" + bordersShader.getLog());

		// set projection matrix of batch
		batch.setProjectionMatrix(
				new OrthographicCamera(FBO_RES, FBO_RES).combined
		);
	}

	private int lightIndex, lightX, lightY;

	@Override
	public void update(float deltaTime) {

		// start
		fbo.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		lightIndex = 0;
		// render light texture for each light
		batch.setColor(0, 1, 0, 1); // set borders
		for (Entity e : getEntities()) {
			for (Lights.Light l : mapper.get(e))
				for (int i = 0; i < 16; i++) {
					setLightPos();
					renderLight(l);
					lightIndex++;
				}
		}

		lightIndex = 0;
		// render shadows for each light
		for (Entity e : getEntities()) {
			for (Lights.Light l : mapper.get(e))
				for (int i = 0; i < 16; i++) {
					setLightPos();
					shadowRenderer.renderShadows(
							l, lightX, lightY, batch, ecs.getRoom()
					);
					lightIndex++;
				}
		}

		// end
		batch.end();
		fbo.end();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tempBatch.begin();
		Texture t = fbo.getColorBufferTexture();
		t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		tempBatch.draw(t, 10, 300, 300, -300);
		tempBatch.end();
	}

	private void setLightPos() {
		lightX = lightIndex % LIGHTS_PER_ROW;
		lightY = lightIndex / LIGHTS_PER_ROW;
	}

	private Vector2 lightRenderPos = new Vector2();

	private void renderLight(Lights.Light l) {
		float a = HALF_LIGHT_RES - l.radius * Game.PPM;
		lightRenderPos.set(-HALF_LIGHT_RES * LIGHTS_PER_ROW, HALF_LIGHT_RES * (LIGHTS_PER_ROW - 2))
				.add(a, a)
				.add(
						lightX * LIGHT_RES,
						-lightY * LIGHT_RES
				);

		float size = 2 * l.radius * Game.PPM;
		batch.draw(lightTex, lightRenderPos.x, lightRenderPos.y, size, size);
	}

	// this method is not used:
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
	}

}
