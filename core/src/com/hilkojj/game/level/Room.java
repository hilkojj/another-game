package com.hilkojj.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

public class Room {

	public final static int CHUNK_WIDTH = 24, CHUNK_HEIGHT = 16;

	public enum Block {
		AIR(0),
		GRASS(1),
		ICE(2);

		public final int id;

		Block(int id) {
			this.id = id;
		}

	}

	private final static Block[] BLOCK_VALUES = Block.values();

	public final int xChunks, yChunks;
	public final Block[][] blocks;

	public Room(int xChunks, int yChunks) {
		this.xChunks = xChunks;
		this.yChunks = yChunks;

		blocks = new Block[xChunks * CHUNK_WIDTH][];
		for (int x = 0; x < xChunks * CHUNK_WIDTH; x++) {

			blocks[x] = new Block[yChunks * CHUNK_HEIGHT];

			for (int y = 0; y < yChunks * CHUNK_HEIGHT; y++) blocks[x][y] = Block.AIR;
		}
	}

	public Room(String filePath) {

		XmlReader.Element xml = new XmlReader().parse(Gdx.files.internal(filePath));

		xChunks = xml.getIntAttribute("width") / CHUNK_WIDTH;
		yChunks = xml.getIntAttribute("height") / CHUNK_HEIGHT;

		blocks = new Block[xChunks * CHUNK_WIDTH][];

		String blockIds = xml.getChildByName("layer").getChildByName("data").getText().replaceAll("\\D+", "");


		for (int x = 0; x < xChunks * CHUNK_WIDTH; x++) {

			blocks[x] = new Block[yChunks * CHUNK_HEIGHT];

			for (int y = 0; y < yChunks * CHUNK_HEIGHT; y++) {

				int i = (yChunks* CHUNK_HEIGHT - y - 1) * xChunks * CHUNK_WIDTH + x;
				blocks[x][y] = blockIdToBlock(
						Character.getNumericValue(blockIds.charAt(i))
				);
			}
		}
	}

	private Block blockIdToBlock(int id) {
		for (Block b : BLOCK_VALUES) if (b.id == id) return b;
		return null;
	}

}
