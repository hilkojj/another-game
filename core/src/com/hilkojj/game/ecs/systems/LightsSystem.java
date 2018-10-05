package com.hilkojj.game.ecs.systems;

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
			HALF_MAX_LIGHT_RES = 128,
			MAX_LIGHT_RES = 2 * HALF_MAX_LIGHT_RES,
			LIGHTS_PER_ROW = 1;

	private ECSScreen ecs;
	private ComponentMapper<Lights> mapper = ComponentMapper.getFor(Lights.class);

	private FrameBuffer fbo = new FrameBuffer(
			Pixmap.Format.RGBA8888,
			MAX_LIGHT_RES * LIGHTS_PER_ROW,
			MAX_LIGHT_RES * LIGHTS_PER_ROW,
			false
	);
	private Sprite shadowQuad;
	private ShaderProgram bordersShader = new ShaderProgram(
			Gdx.files.internal("glslshaders/default.vert").readString(),
			Gdx.files.internal("glslshaders/draw_in_borders.frag").readString()
	);
	private SpriteBatch batch = new SpriteBatch(1000, bordersShader);
	private SpriteBatch tempBatch = new SpriteBatch();

	public LightsSystem(ECSScreen ecs) {
		super(Family.all(Lights.class).get());
		this.ecs = ecs;
		shadowQuad = new Sprite(
				Game.assetManager.get("sprites/shadow_quad.png", Texture.class)
		);
		shadowQuad.setOrigin(0, 1);

		// print shader errors/warnings
		System.out.println(	"Border-shader log: \n" + bordersShader.getLog());

		// set projection matrix of batch
		batch.setProjectionMatrix(
				new OrthographicCamera(
						MAX_LIGHT_RES * LIGHTS_PER_ROW,
						MAX_LIGHT_RES * LIGHTS_PER_ROW
				).combined
		);
	}

	private int lightIndex, lightX, lightY;

	@Override
	public void update(float deltaTime) {

		// start
		fbo.begin();
		Gdx.gl.glClearColor(0, .1f, .2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		lightIndex = 0;

		super.update(deltaTime); // iterate over entities with Lights

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

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		for (Lights.Light l : mapper.get(entity)) renderLight(l, deltaTime);

	}

	private final AABB lightAabb = new AABB(new Vector2(), new Vector2());
	private Line temp = new Line();

	private void renderLight(Lights.Light l, float deltaTime) {

		lightX = lightIndex % LIGHTS_PER_ROW;
		lightY = lightIndex / LIGHTS_PER_ROW;

		shadowQuad.setColor(0, 1, 0, 1);

		RoomOutlines outlines = ecs.getRoom().getOutlines();

		lightAabb.center.set(l.pos);
		lightAabb.halfSize.set(l.radius, l.radius);

		for (AALine line : outlines) if (line.intersects(lightAabb)) renderLineShadow(l, temp.set(line));

		++lightIndex;
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
			point.scl(16);
			point.add(
					-(LIGHTS_PER_ROW  - 1) * HALF_MAX_LIGHT_RES,
					(LIGHTS_PER_ROW - 1) * HALF_MAX_LIGHT_RES
			);
			point.add(
					lightX * MAX_LIGHT_RES,
					-lightY * MAX_LIGHT_RES
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

}
