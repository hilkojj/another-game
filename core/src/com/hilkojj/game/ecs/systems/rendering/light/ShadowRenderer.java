package com.hilkojj.game.ecs.systems.rendering.light;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.components.Lights;
import com.hilkojj.game.graphics.SpriteUtils;
import com.hilkojj.game.level.Room;
import com.hilkojj.game.level.RoomOutlines;
import com.hilkojj.game.utils.AABB;
import com.hilkojj.game.utils.AALine;
import com.hilkojj.game.utils.Line;

import static com.hilkojj.game.ecs.systems.rendering.light.LightsSystem.*;

public class ShadowRenderer {

	private Sprite shadowQuad;

	ShadowRenderer() {
		shadowQuad = new Sprite(
				Game.assetManager.get("sprites/shadow_quad.png", Texture.class)
		);
	}

	private final AABB lightAabb = new AABB(new Vector2(), new Vector2());
	private Line temp = new Line();

	void renderShadows(Lights.Light l, int lightX, int lightY, SpriteBatch batch, Room room) {

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

		RoomOutlines outlines = room.getOutlines();

		lightAabb.center.set(l.pos);
		lightAabb.halfSize.set(l.radius, l.radius);

		for (AALine line : outlines) if (line.intersects(lightAabb))
			renderLineShadow(l, temp.set(line), lightX, lightY, batch);
	}

	private Vector2 tempVec = new Vector2();
	private Line shadowLine = new Line(), tempLine = new Line();

	private void renderLineShadow(
			Lights.Light l, Line line, int lightX, int lightY, SpriteBatch batch
	) {

		shadowLine.set(line);
		for (int i = 0; i < 2; i++) {

			Vector2 point = shadowLine.point(i);
			tempVec.set(
					point.x - l.pos.x,
					point.y - l.pos.y
			).nor().scl(l.radius);
			point.add(tempVec);
		}
		renderShadowQuad(l, line, shadowLine, lightX, lightY, batch);

		Vector2 normal = normalThatPointsAwayFromLight(line, l).scl(l.radius);
		tempLine.set(shadowLine);
		for (int i = 0; i < 2; i++)
			shadowLine.point(i).add(normal);
		renderShadowQuad(l, tempLine, shadowLine, lightX, lightY, batch);
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

	private void renderShadowQuad(
			Lights.Light l, Line line0, Line line1, int lightX, int lightY, SpriteBatch batch
	) {
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

}
