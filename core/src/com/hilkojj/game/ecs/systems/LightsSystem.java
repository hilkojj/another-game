package com.hilkojj.game.ecs.systems;

// just a few imports?

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.Lights;
import com.hilkojj.game.graphics.SpriteUtils;
import com.hilkojj.game.level.RoomOutlines;
import com.hilkojj.game.utils.AABB;
import com.hilkojj.game.utils.AALine;
import com.hilkojj.game.utils.Line;

public class LightsSystem extends IteratingSystem {

	private static final int
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
	private Sprite shadowQuad;
	private ShaderProgram bordersShader = new ShaderProgram(
			Gdx.files.internal("glslshaders/default.vert").readString(),
			Gdx.files.internal("glslshaders/draw_in_borders.frag").readString()
	);
	private SpriteBatch batch = new SpriteBatch(1000, bordersShader);
	private SpriteBatch tempBatch = new SpriteBatch();
	private Texture lightTex;

	public LightsSystem(ECSScreen ecs) {
		super(Family.all(Lights.class).get());
		this.ecs = ecs;
		shadowQuad = new Sprite(
				Game.assetManager.get("sprites/shadow_quad.png", Texture.class)
		);
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
					renderShadows(l);
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

	private final AABB lightAabb = new AABB(new Vector2(), new Vector2());
	private Line temp = new Line();

	private void renderShadows(Lights.Light l) {

		shadowQuad.setColor(        // color attribute is (mis-)used by shader to determine borders
				// min X
				(float) (lightX * LIGHT_RES) / FBO_RES,
				// max X
				(float) (lightX + 1) * LIGHT_RES / FBO_RES,
				// min Y
				(float) (LIGHT_RES * LIGHTS_PER_ROW - ((lightY + 1) * LIGHT_RES)) / FBO_RES,
				// max Y
				(float) (LIGHT_RES * LIGHTS_PER_ROW - (lightY * LIGHT_RES)) / FBO_RES
		);

		RoomOutlines outlines = ecs.getRoom().getOutlines();

		lightAabb.center.set(l.pos);
		lightAabb.halfSize.set(l.radius, l.radius);

		for (AALine line : outlines) if (line.intersects(lightAabb)) renderLineShadow(l, temp.set(line));
	}

	private Vector2 tempVec = new Vector2();
	private Line shadowLine = new Line(), tempLine = new Line();

	private void renderLineShadow(Lights.Light l, Line line) {

		shadowLine.set(line);
		for (int i = 0; i < 2; i++) {

			Vector2 point = shadowLine.point(i);
			tempVec.set(
					point.x - l.pos.x,
					point.y - l.pos.y
			).nor().scl(l.radius);
			point.add(tempVec);
		}
		renderShadowQuad(l, line, shadowLine);

		Vector2 normal = normalThatPointsAwayFromLight(line, l).scl(l.radius);
		tempLine.set(shadowLine);
		for (int i = 0; i < 2; i++)
			shadowLine.point(i).add(normal);
		renderShadowQuad(l, tempLine, shadowLine);
	}

	private Vector2 normal = new Vector2(), nTemp = new Vector2();

	private Vector2 normalThatPointsAwayFromLight(Line line, Lights.Light light) {

		normal.set(line.p1).sub(line.p0).rotate90(1).nor();
		float
				distsq0 = nTemp.set(normal).add(line.p0).sub(light.pos).len2(),
				distsq1 = nTemp.set(normal).scl(-1).add(line.p0).sub(light.pos).len2();

		return distsq0 > distsq1 ? normal : normal.scl(-1);
	}

	private Line mappedLine0 = new Line(), mappedLine1 = new Line();

	private void renderShadowQuad(Lights.Light l, Line line0, Line line1) {

		// map lines
		mappedLine0.set(line0);
		mappedLine1.set(line1);

		for (int i = 0; i < 4; i++) {
			Vector2 point = i < 2 ? mappedLine0.point(i) : mappedLine1.point(i - 2);

			point.sub(l.pos);
			point.scl(Game.PPM);
			point.add(
					-(LIGHTS_PER_ROW - 1) * HALF_LIGHT_RES,
					(LIGHTS_PER_ROW - 1) * HALF_LIGHT_RES
			);
			point.add(
					lightX * LIGHT_RES,
					-lightY * LIGHT_RES
			);
		}
		// set vertices of sprite
		float[] verts = SpriteUtils.setSpriteVerts(
				shadowQuad,
				mappedLine0.p0.x, mappedLine0.p0.y,
				mappedLine0.p1.x, mappedLine0.p1.y,
				mappedLine1.p0.x, mappedLine1.p0.y,
				mappedLine1.p1.x, mappedLine1.p1.y
		);
		batch.draw(
				shadowQuad.getTexture(),
				verts,
				0, verts.length
		);
	}

	// this method is not used:
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
	}

}
