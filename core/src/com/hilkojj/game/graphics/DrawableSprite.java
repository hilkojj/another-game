package com.hilkojj.game.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hilkojj.game.Game;

public class DrawableSprite extends Sprite implements Drawable {

	public DrawableSprite(String spritePath) {

		super(Game.assetManager.get(spritePath, Texture.class));
		setSize(getWidth() / 16f, getHeight() / 16f);

	}

	@Override
	public void draw(SpriteBatch batch, float deltaTime) {
		super.draw(batch);
	}

}
