package com.hilkojj.game.ecs.systems.rendering.fog;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.level.Room;
import com.hilkojj.game.utils.Random;

import static com.hilkojj.game.level.Room.CHUNK_HEIGHT;
import static com.hilkojj.game.level.Room.CHUNK_WIDTH;

public class FogSystem extends EntitySystem {

	private class FogCloud {

		float
				x, y,
				velX, velY,
				timer, lifeTime;
		int textureX, textureY;

	}

	private static final int
		CLOUD_SIZE = 64,
		CLOUDS_PER_ROW = 4;

	private Array<Vector2> spawnPoints = new Array<>();
	private Array<FogCloud> clouds;
	private Room room;
	private ECSScreen ecs;
	private float timeUntilSpawn;
	private Texture cloudGrid = Game.assetManager.get("sprites/fog_clouds.png", Texture.class);

	private SpriteBatch temp = new SpriteBatch();
	private Texture tempt = Game.assetManager.get("sprites/bat.png", Texture.class);

	public FogSystem(ECSScreen ecs) {
		this.ecs = ecs;
	}

	@Override
	public void update(float deltaTime) {

		if (ecs.getRoom() != room) {
			roomChanged();
			deltaTime = 10; // to simulate 10 seconds of spawning and moving
		}

		float d = deltaTime;
		while (true) {
			float step = Math.min(.1f, d);

			updateClouds(step);

			timeUntilSpawn -= step;
			if (timeUntilSpawn <= 0) {
				spawnClouds();
				timeUntilSpawn = .1f;
			}
			d -= step;
			if (d <= 0) break;
		}


		temp.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
		temp.setProjectionMatrix(ecs.camera.combined);
		temp.begin();
		for (FogCloud c: clouds) {
			float val = Math.min(c.timer / 2f, 1);
			val -= 1 -Math.min(c.lifeTime - c.timer, 1);
			val *= .03f;
			temp.setColor(val, val, val, 1);
			temp.draw(
					cloudGrid,
					c.x - CLOUD_SIZE / Game.PPM / 4f,
					c.y - CLOUD_SIZE / Game.PPM / 4f,
					CLOUD_SIZE / Game.PPM / 2f,
					CLOUD_SIZE / Game.PPM / 2f,
					CLOUD_SIZE * c.textureX,
					CLOUD_SIZE * c.textureY,
					CLOUD_SIZE, CLOUD_SIZE, false, false
			);
		}
		temp.end();
	}

	private void roomChanged() {

		room = ecs.getRoom();

		spawnPoints.clear();
		clouds = new Array<>(room.xChunks * CHUNK_WIDTH * (room.yChunks * CHUNK_HEIGHT) * 10);

		for (int x = 0; x <= room.xChunks * CHUNK_WIDTH; x++) {
			for (int y = 0; y < room.yChunks * CHUNK_HEIGHT; y++) {

				if (room.getBlock(x, y) != Room.Block.AIR && Random.random() > .1f) continue;

				int distToGround = room.distToGround(x, y);

				float p = 1 - distToGround * .3f;
				p += Random.random() * .7f;

				if (p >= 1) spawnPoints.add(new Vector2(x, y));
			}
		}
	}

	private void spawnClouds() {

		int i = (int) (8 * Random.random());
		while (i < spawnPoints.size) {

			Vector2 spawnPoint = spawnPoints.get(i);
			FogCloud c = new FogCloud();

			c.x = spawnPoint.x + Random.random();
			c.y = spawnPoint.y + Random.random();

			c.velX = (Random.random() - .5f) * .3f;
			c.velY = (Random.random() - .35f) * .3f;
			c.lifeTime = 3 + Random.random() * 5;

			c.textureX = (int) (Random.random() * CLOUDS_PER_ROW);
			c.textureY = (int) (Random.random() * CLOUDS_PER_ROW);

			clouds.add(c);

			i += 16 * Random.random();
		}
	}

	private void updateClouds(float deltaTime) {

		for (int i = clouds.size - 1; i >= 0; i--) {
			FogCloud c = clouds.get(i);

			c.timer += deltaTime;
			if (c.timer > c.lifeTime) {
				clouds.removeIndex(i);
				continue;
			}

			c.x += c.velX * deltaTime;
			c.y += c.velY * deltaTime;
		}
	}

}
