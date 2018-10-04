package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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
			MAX_LIGHT_RES = 256,
			LIGHTS_PER_ROW = 4;

	private ECSScreen ecs;
	private ComponentMapper<Lights> mapper = ComponentMapper.getFor(Lights.class);

	private FrameBuffer fbo = new FrameBuffer(
			Pixmap.Format.RGBA8888,
			MAX_LIGHT_RES * LIGHTS_PER_ROW,
			MAX_LIGHT_RES * LIGHTS_PER_ROW,
			false
	);
	private SpriteBatch batch = new SpriteBatch();
	private final Sprite shadowQuad;

	public LightsSystem(ECSScreen ecs) {
		super(Family.all(Lights.class).get());
		this.ecs = ecs;
		shadowQuad = new Sprite(
				Game.assetManager.get("sprites/shadow_quad.png", Texture.class)
		);
		shadowQuad.setOrigin(0, 1);
	}

	private int lightIndex;

	@Override
	public void update(float deltaTime) {

		// start
//		fbo.begin();
		batch.setProjectionMatrix(ecs.camera.combined);
		batch.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		lightIndex = 0;

		super.update(deltaTime); // iterate over entities with Lights

		// end
		batch.end();
//		fbo.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		for (Lights.Light l : mapper.get(entity)) renderLight(l, deltaTime);

	}

	private final AABB lightAabb = new AABB(new Vector2(), new Vector2());

	private Line temp = new Line();

	private void renderLight(Lights.Light l, float deltaTime) {

		RoomOutlines outlines = ecs.getRoom().getOutlines();

		lightAabb.center.set(l.pos);
		lightAabb.halfSize.set(l.radius, l.radius);

		for (AALine line : outlines) if (line.intersects(lightAabb)) renderLineShadow(l, temp.set(line));

		++lightIndex;
	}

	private Vector2 tempVec = new Vector2();
	private Line shadowLine = new Line();

	private void renderLineShadow(Lights.Light l, Line line) {

		shadowLine.set(line);
		for (int i = 0; i < 2; i++) {

			Vector2 point = shadowLine.point(i);
			tempVec.set(
					point.x - l.pos.x,
					point.y - l.pos.y
			);
			tempVec.nor();
			tempVec.scl(l.radius);
			point.add(tempVec);
		}
		renderShadowQuad(line, shadowLine);
	}

	private void renderShadowQuad(Line line0, Line line1) {
		float[] verts = SpriteUtils.setSpriteVerts(
				shadowQuad,
				line0.p0.x, line0.p0.y,
				line0.p1.x, line0.p1.y,
				line1.p0.x, line1.p0.y,
				line1.p1.x, line1.p1.y
		);
		batch.draw(
				shadowQuad.getTexture(),
				verts,
				0, verts.length
		);
	}

}
