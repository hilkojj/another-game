package com.hilkojj.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.hilkojj.game.entities.Edward;
import com.hilkojj.game.systems.RenderSystem;

public class Game extends ApplicationAdapter {

	public static Engine engine;
	public static AssetManager assetManager;

	public static long renderTime;

	@Override
	public void create () {

		engine = new Engine();
		engine.addSystem(new RenderSystem());

		engine.addEntity(new Edward());

	}

	@Override
	public void render () {


		engine.update(Gdx.graphics.getDeltaTime());

		Gdx.graphics.setTitle("fps: " + Gdx.graphics.getFramesPerSecond() + " RenderTime: " + renderTime + "ms");


	}
	
	@Override
	public void dispose () {

	}

}
