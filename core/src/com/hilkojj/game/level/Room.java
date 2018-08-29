package com.hilkojj.game.level;

public class Room {

	public final static int CHUNK_WIDTH = 24, CHUNK_HEIGHT = 16;

	public enum BlockType {
		SOLID,
		PLATFORM
	}

	public enum BlockMaterial {

		STONE(0),
		GRASS(1),
		ICE(2);

		public final int id;

		BlockMaterial(int id) {
			this.id = id;
		}

	}

	public final int xChunks, yChunks;
	public final BlockType[][] blocks;
	public final BlockMaterial[][] materials;

	public Room(int xChunks, int yChunks) {
		this.xChunks = xChunks;
		this.yChunks = yChunks;

		blocks = new BlockType[xChunks * CHUNK_WIDTH][];
		for (int x = 0; x < xChunks * CHUNK_WIDTH; x++) blocks[x] = new BlockType[yChunks * CHUNK_HEIGHT];

		materials = new BlockMaterial[xChunks * CHUNK_WIDTH][];
		for (int x = 0; x < xChunks * CHUNK_WIDTH; x++) materials[x] = new BlockMaterial[yChunks * CHUNK_HEIGHT];
	}

}
