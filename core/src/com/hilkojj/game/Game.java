package com.hilkojj.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.hilkojj.game.systems.RenderSystem;

public class Game extends ApplicationAdapter {

	public static Engine engine;

	@Override
	public void create () {

		engine = new Engine();
		engine.addSystem(new RenderSystem());

	}

	@Override
	public void render () {


		engine.update(Gdx.graphics.getDeltaTime());


	}
	
	@Override
	public void dispose () {

	}

}
