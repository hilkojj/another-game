package com.hilkojj.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.hilkojj.game.ecs.components.DebugShapes;
import com.hilkojj.game.ecs.components.Drawables;
import com.hilkojj.game.ecs.components.Particles;
import com.hilkojj.game.ecs.components.Physics;
import com.hilkojj.game.utils.Random;

import java.util.Iterator;

public class ParticleSystem extends IteratingSystem {

	private ComponentMapper<Particles> mapper = ComponentMapper.getFor(Particles.class);
	private ComponentMapper<Physics> physicsMapper = ComponentMapper.getFor(Physics.class);
	private ComponentMapper<DebugShapes> debugMapper = ComponentMapper.getFor(DebugShapes.class);

	public ParticleSystem() {
		super(Family.all(Particles.class, Drawables.class, Physics.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Particles p = mapper.get(entity);
		if (p.spawners.size > 0) spawnNewParticles(p, entity, deltaTime);

		if (p.particles.size > 0) updateParticles(p, entity, deltaTime);

	}

	private void spawnNewParticles(Particles p, Entity entity, float deltaTime) {

		Physics physics = physicsMapper.get(entity);
		DebugShapes debugShapes = debugMapper.get(entity);

		Iterator<Particles.ParticleSpawner> it = p.spawners.iterator();

		while (it.hasNext()) {

			Particles.ParticleSpawner spawner = it.next();

			float time = deltaTime < spawner.timeRemaining ? deltaTime : spawner.timeRemaining;
			spawner.particlesToSpawn += time * spawner.particlesPerSecond;

			int spawnNow = (int) spawner.particlesToSpawn;

			for (int i = 0; i < spawnNow; i++) {

				spawner.particlesToSpawn--;

				Particles.Particle particle = new Particles.Particle();
				particle.body = new Physics.Body(new Vector2(
						spawner.spawnArea.center.x - spawner.spawnArea.halfSize.x
								+ (Random.random() * 2 * spawner.spawnArea.halfSize.x),
						spawner.spawnArea.center.y - spawner.spawnArea.halfSize.y
								+ (Random.random() * 2 * spawner.spawnArea.halfSize.y)
				), new Vector2(.05f, .05f), true);

				physics.bodies.add(particle.body);
				particle.body.velocity.set(0, Random.random(spawner.minVelocity, spawner.maxVelocity));
				particle.body.velocity.rotate(Random.random(spawner.minAngle, spawner.maxAngle));

				particle.bounce = Random.random(spawner.minBounce, spawner.maxBounce);
				particle.gravity = Random.random(spawner.minGravity, spawner.maxGravity);
				particle.lifeRemaining = Random.random(spawner.minLife, spawner.maxLife);
				particle.textureRegion = spawner.textureRegionProvider.get();

				p.particles.add(particle);

				if (debugShapes != null) debugShapes.addAabb(particle.body);
			}

			spawner.timeRemaining -= deltaTime;
			if (spawner.timeRemaining <= 0)
				it.remove();
		}

	}

	private void updateParticles(Particles p, Entity entity, float deltaTime) {

		Physics physics = null;
		DebugShapes debugShapes = null;

		for (int i = p.particles.size - 1; i >= 0; i--) {

			Particles.Particle part = p.particles.get(i);
			Vector2 vel = part.body.velocity;

			vel.y += part.gravity * deltaTime;

			if (part.body.onGround && vel.y < 0) vel.y = 0;
			else if (part.body.touchesCeiling && vel.y > 0) vel.y = 0;

			part.lifeRemaining -= deltaTime;

			if (part.lifeRemaining <= 0) {
				p.particles.removeIndex(i);

				if (physics == null) physics = physicsMapper.get(entity);
				if (debugShapes == null) debugShapes = debugMapper.get(entity);

				physics.bodies.removeValue(part.body, true);
				debugShapes.removeAabb(part.body);
			}
		}

	}

}
