package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hilkojj.game.Game;
import com.hilkojj.game.ecs.ECSScreen;
import com.hilkojj.game.ecs.components.Particles;
import com.hilkojj.game.ecs.components.Physics;
import com.hilkojj.game.ecs.components.movement.LandingParticles;
import com.hilkojj.game.level.Room;
import com.hilkojj.game.utils.MathUtilsss;
import com.hilkojj.game.utils.Random;

public class LandingParticlesSystem extends IteratingSystem {

	private ComponentMapper<Particles> particlesMapper = ComponentMapper.getFor(Particles.class);
	private ComponentMapper<LandingParticles> landingParticlesMapper = ComponentMapper.getFor(LandingParticles.class);
	private ECSScreen ecs;

	public LandingParticlesSystem(ECSScreen ecs) {
		super(Family.all(Particles.class, LandingParticles.class).get());
		this.ecs = ecs;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Particles particles = particlesMapper.get(entity);
		LandingParticles lP = landingParticlesMapper.get(entity);

		for (Physics.Body b : lP.bodies) {
			if (b.prevVelocity.y < 0 && b.onGround && !b.wasOnGround) {

				int x = (int) b.center.x;
				int y = (int) (b.center.y - b.halfSize.y - .1f);

				Room r = ecs.getRoom();
				if (r == null) return;

				Room.Block block = r.getBlock(x, y);

				if (block.tileset == null) continue;

				Texture tileset = Game.assetManager.get(block.tileset, Texture.class);

				lP.particlesSpawnArea.center.set(
						b.center.x, b.center.y - b.halfSize.y + .1f
				);
				lP.particlesSpawnArea.halfSize.set(
						b.halfSize.x, .1f
				);

				float multiplier = MathUtilsss.map(-b.prevVelocity.y, 0, 20, .1f, 1.5f);

				particles.spawners.add(
						new Particles.ParticleSpawner(
								lP.particlesSpawnArea,
								(int) (130 * multiplier), .04f, -90, 90,
								3, 6 * multiplier, -40, -30, 0, .1f,
								.3f, .5f,
								() -> new TextureRegion(
										tileset,
										(int) (Random.random() * 12),
										(int) (Random.random() * 12),
										4, 4
								)
						)
				);
				System.out.println("add spawner");

			}
		}

	}

}
