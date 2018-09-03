package com.hilkojj.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hilkojj.game.Game;
import com.hilkojj.game.utils.Random;

import static com.hilkojj.game.level.Room.CHUNK_HEIGHT;
import static com.hilkojj.game.level.Room.CHUNK_WIDTH;
import static com.hilkojj.game.level.TileMap.BlockType.*;

public class TileMap {

	protected enum BlockType {

		AIR,
		BLK, // block
		ANY  // any block or air

	}

	public static class Tile {

		public final static Tile[] TILES = new Tile[]{

				new Tile(
						1, 1,
						new BlockType[]{
								ANY, AIR, ANY,
								AIR, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						2, 1,
						new BlockType[]{
								ANY, AIR, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						3, 1,
						new BlockType[]{
								ANY, AIR, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						4, 1,
						new BlockType[]{
								ANY, AIR, ANY,
								BLK, BLK, AIR,
								ANY, BLK, ANY
						}
				),

				new Tile(
						5, 0,
						new BlockType[]{
								ANY, AIR, ANY,
								AIR, BLK, AIR,
								ANY, AIR, ANY
						}
				),

				new Tile(
						6, 1,
						new BlockType[]{
								ANY, AIR, ANY,
								AIR, BLK, AIR,
								ANY, BLK, ANY
						}
				),

				new Tile(
						6, 2,
						new BlockType[]{
								ANY, BLK, ANY,
								AIR, BLK, AIR,
								ANY, BLK, ANY
						}
				),

				new Tile(
						6, 3,
						new BlockType[]{
								ANY, BLK, ANY,
								AIR, BLK, AIR,
								ANY, BLK, ANY
						}
				),

				new Tile(
						6, 4,
						new BlockType[]{
								ANY, BLK, ANY,
								AIR, BLK, AIR,
								ANY, AIR, ANY
						}
				),

				new Tile(
						1, 2,
						new BlockType[]{
								ANY, BLK, ANY,
								AIR, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						1, 3,
						new BlockType[]{
								ANY, BLK, ANY,
								AIR, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						1, 4,
						new BlockType[]{
								ANY, BLK, ANY,
								AIR, BLK, BLK,
								ANY, AIR, ANY
						}
				),

				new Tile(
						2, 4,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, AIR, ANY
						}
				),

				new Tile(
						3, 4,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, AIR, ANY
						}
				),

				new Tile(
						4, 4,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, AIR,
								ANY, AIR, ANY
						}
				),

				new Tile(
						4, 3,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, AIR,
								ANY, BLK, ANY
						}
				),

				new Tile(
						4, 2,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, AIR,
								ANY, BLK, ANY
						}
				),

				new Tile(
						2, 2,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						3, 3,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						},
						.04f
				),

				new Tile(
						3, 2,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						},
						.02f
				),

				new Tile(
						2, 3,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						},
						.02f
				),

				new Tile(
						1, 6,
						new BlockType[]{
								ANY, AIR, ANY,
								AIR, BLK, BLK,
								ANY, AIR, ANY
						}
				),

				new Tile(
						2, 6,
						new BlockType[]{
								ANY, AIR, ANY,
								BLK, BLK, BLK,
								ANY, AIR, ANY
						}
				),

				new Tile(
						3, 6,
						new BlockType[]{
								ANY, AIR, ANY,
								BLK, BLK, BLK,
								ANY, AIR, ANY
						}
				),

				new Tile(
						4, 6,
						new BlockType[]{
								ANY, AIR, ANY,
								BLK, BLK, AIR,
								ANY, AIR, ANY
						}
				),

				new Tile(
						6, 6,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								ANY, BLK, AIR
						}
				),

				new Tile(
						7, 6,
						new BlockType[]{
								ANY, BLK, ANY,
								BLK, BLK, BLK,
								AIR, BLK, ANY
						}
				),

				new Tile(
						6, 7,
						new BlockType[]{
								ANY, BLK, AIR,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						}
				),

				new Tile(
						7, 7,
						new BlockType[]{
								AIR, BLK, ANY,
								BLK, BLK, BLK,
								ANY, BLK, ANY
						}
				)

		};

		public final int textureX, textureY;
		public final BlockType[] blockTypes;
		public final int specificity;
		public final float prob;

		Tile(int textureX, int textureY, BlockType[] blockTypes) {
			this(textureX, textureY, blockTypes, .5f);
		}

		Tile(int textureX, int textureY, BlockType[] blockTypes, float prob) {
			this.prob = prob;
			this.textureX = textureX;
			this.textureY = textureY;

			if (blockTypes.length != 9) throw new RuntimeException("blockTypes array incorrect size");

			this.blockTypes = blockTypes;

			int specificity = 9;
			for (BlockType bT : blockTypes) if (bT == ANY) specificity--;
			this.specificity = specificity;
		}

	}

	public final Tile[][] map;
	public final Room room;

	/**
	 * Create a tilemap for a room
	 *
	 * @param room The room.
	 */
	public TileMap(Room room) {

		this.room = room;

		map = new Tile[room.xChunks * CHUNK_WIDTH][];

		for (int x = 0; x < map.length; x++) {
			map[x] = new Tile[room.yChunks * CHUNK_HEIGHT];

			for (int y = 0; y < map[x].length; y++) map[x][y] = findTileFor(x, y, room);
		}
	}

	private Tile findTileFor(int x, int y, Room room) {

		if (room.getBlock(x, y) == Room.Block.AIR) return null;

		Tile bestTile = null;

		searchForTile:
		for (Tile tile : Tile.TILES) {

			int i = 0;

			for (int surroundingY = y + 1; surroundingY > y - 2; surroundingY--) {

				for (int surroundingX = x - 1; surroundingX < x + 2; surroundingX++) {

					Room.Block b = Room.Block.GRASS;

					if (surroundingX >= 0 && surroundingX < room.xChunks * CHUNK_WIDTH
							&& surroundingY >= 0 && surroundingY < room.yChunks * CHUNK_HEIGHT)
						 b = room.getBlock(surroundingX, surroundingY);

					BlockType blockType = tile.blockTypes[i++];

					if (blockType != ANY)
						if ((b != Room.Block.AIR && blockType == AIR)
								|| (b == Room.Block.AIR && blockType == BLK))
							continue searchForTile;
				}
			}

			if (bestTile == null) bestTile = tile;
			else {

				if (bestTile.specificity < tile.specificity
						|| (bestTile.specificity == tile.specificity && Random.random() < tile.prob))
					bestTile = tile;

			}

		}

		return bestTile;
	}

	public void render(SpriteBatch batch) {

		for (Room.Block b : room.blocksUsed) {

			if (b.tileset == null) continue;

			Texture tileset = Game.assetManager.get(b.tileset, Texture.class);


			for (int x = 0; x < room.xChunks * CHUNK_WIDTH; x++) {

				for (int y = 0; y < room.yChunks * CHUNK_HEIGHT; y++) {

					if (room.getBlock(x, y) != b) continue;

					Tile t = map[x][y];

					if (t == null) continue;

					batch.draw(tileset, x, y, 1, 1, 16 * t.textureX, 16 * t.textureY, 16, 16, false, false);

				}

			}

		}

	}

}
