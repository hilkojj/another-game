package com.hilkojj.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hilkojj.game.graphics.Drawable;
import com.hilkojj.game.utils.AABB;

public class Particles implements Component, Drawable {

	public final Array<ParticleSpawner> spawners = new Array<>(8);
	public final Array<Particle> particles = new Array<>();

	@Override
	public void draw(SpriteBatch batch, float deltaTime) {

		for (Particle p : particles) {

			TextureRegion tr = p.textureRegion;

			float width = tr.getRegionWidth() / 16f, height = tr.getRegionHeight() / 16f;

			batch.draw(tr, p.body.center.x - width * .5f, p.body.center.y - height * .5f, width, height);
		}

	}

	public static class Particle {

		public Physics.Body body;
		public float
				gravity,
				bounce,
				lifeRemaining;
		public TextureRegion textureRegion;

	}

	public static class ParticleSpawner {

		public AABB spawnArea;
		public int particlesPerSecond;
		public float timeRemaining, particlesToSpawn;

		public float minAngle, maxAngle;
		public float minVelocity, maxVelocity;
		public float minGravity, maxGravity;
		public float minBounce, maxBounce;
		public float minLife, maxLife;

		public TextureRegionProvider textureRegionProvider;

		public ParticleSpawner(
				AABB spawnArea,
				int particlesPerSecond,
				float duration,
				float minAngle, float maxAngle,
				float minVelocity, float maxVelocity,
				float minGravity, float maxGravity,
				float minBounce, float maxBounce,
				float minLife, float maxLife,
				TextureRegionProvider textureRegionProvider
		) {
			this.spawnArea = spawnArea;
			this.particlesPerSecond = particlesPerSecond;
			this.timeRemaining = duration;
			this.minAngle = minAngle;
			this.maxAngle = maxAngle;
			this.minVelocity = minVelocity;
			this.maxVelocity = maxVelocity;
			this.minGravity = minGravity;
			this.maxGravity = maxGravity;
			this.minBounce = minBounce;
			this.maxBounce = maxBounce;
			this.minLife = minLife;
			this.maxLife = maxLife;
			this.textureRegionProvider = textureRegionProvider;
		}

	}

	@FunctionalInterface
	public interface TextureRegionProvider {

		TextureRegion get();

	}

}
