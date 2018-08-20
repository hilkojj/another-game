package com.hilkojj.game.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

class AssetLoader {

	enum Assets {

		TEXTURES(
				new String[]{"sprites"},
				new String[]{"png", "jpg"},
				Texture.class
		);

		final String[] paths;
		final String[] extensions;
		final Class<?> type;

		Assets(String[] paths, String[] extensions, Class<?> type) {
			this.paths = paths;
			this.extensions = extensions;
			this.type = type;
		}

	}

	static void loadAssets(AssetManager manager) {

		for (Assets a : Assets.values()) {

			for (String path : a.paths) {

				String[] files = Gdx.files.internal(path + "/index.txt").readString().split("\n");

				for (String fileName : files) {

					boolean valid = false;
					for (String ext : a.extensions) {
						if (fileName.endsWith("." + ext)) {
							valid = true;
							break;
						}
					}

					if (!valid) continue;

					manager.load(path + "/" + fileName, a.type);
				}

			}

		}

	}

	private AssetLoader() {}

}
