package com.hilkojj.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

import java.util.Arrays;

public class Room {

	public final static int CHUNK_WIDTH = 24, CHUNK_HEIGHT = 16;

	public enum Block {
		AIR(0, null),
		GRASS(1, "sprites/tilesets/grass_but_mostly_bricks.png");

		public final int id;
		public final String tileset;

		Block(int id, String tileset) {
			this.id = id;
			this.tileset = tileset;
		}

	}

	private final static Block[] BLOCK_VALUES = Block.values();

	private final Block[][] blocks;

	private TileMap tileMap;
	private RoomOutlines outlines;

	public final int xChunks, yChunks;
	public final Block[] blocksUsed;

	public Room(String filePath) {

		XmlReader.Element xml = new XmlReader().parse(Gdx.files.internal(filePath));

		xChunks = xml.getIntAttribute("width") / CHUNK_WIDTH;
		yChunks = xml.getIntAttribute("height") / CHUNK_HEIGHT;

		blocks = new Block[xChunks * CHUNK_WIDTH][];

		String blockIds = xml.getChildByName("layer").getChildByName("data").getText().replaceAll("\\D+", "");

		Block[] blocksUsed = new Block[BLOCK_VALUES.length];
		int blocksUsedI = 0;

		for (int x = 0; x < xChunks * CHUNK_WIDTH; x++) {

			blocks[x] = new Block[yChunks * CHUNK_HEIGHT];

			for (int y = 0; y < yChunks * CHUNK_HEIGHT; y++) {

				int i = (yChunks* CHUNK_HEIGHT - y - 1) * xChunks * CHUNK_WIDTH + x;

				Block b = blockIdToBlock(
						Character.getNumericValue(blockIds.charAt(i))
				);

				blocks[x][y] = b;

				boolean unique = true;

				for (Block usedBlock : blocksUsed) if (usedBlock == b) {
					unique = false;
					break;
				}

				if (unique) blocksUsed[blocksUsedI++] = b;
			}
		}

		this.blocksUsed = Arrays.copyOfRange(blocksUsed, 0, blocksUsedI);
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || x >= xChunks * CHUNK_WIDTH || y < 0 || y >= yChunks * CHUNK_HEIGHT) return Block.AIR;
		return blocks[x][y];
	}

	public TileMap getTileMap() {
		if (tileMap == null) tileMap = new TileMap(this);
		return tileMap;
	}

	public RoomOutlines getOutlines() {
		if (outlines == null) this.outlines = new RoomOutlines(this);
		return outlines;
	}

	private Block blockIdToBlock(int id) {
		for (Block b : BLOCK_VALUES) if (b.id == id) return b;
		return null;
	}

}
