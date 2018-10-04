package com.hilkojj.game.level;

import com.badlogic.gdx.utils.Array;
import com.hilkojj.game.utils.AALine;

import static com.hilkojj.game.level.Room.Block.AIR;
import static com.hilkojj.game.level.Room.CHUNK_HEIGHT;
import static com.hilkojj.game.level.Room.CHUNK_WIDTH;


public class RoomOutlines extends Array<AALine> {

	public RoomOutlines(Room room) {
		for (int x = 0; x < room.xChunks * CHUNK_WIDTH; x++) {

			for (int i = 0; i < 2; i++) { // for left (0) and right (1) walls

				int yBegin = -1;

				for (int y = 0; y < room.yChunks * CHUNK_HEIGHT; y++) {

					Room.Block air = room.getBlock(x + (i == 0 ? 1 : -1), y);
					boolean isWall = room.getBlock(x, y) != AIR && air == AIR;

					if (yBegin == -1 && isWall) yBegin = y;
					else if (yBegin != -1 && !isWall) {

						int lineX = i == 0 ? x + 1 : x;
						AALine l = new AALine(lineX, yBegin, y - yBegin, false);
						add(l);
						yBegin = -1;
					}
				}
			}
		}

		for (int y = 0; y < room.yChunks * CHUNK_HEIGHT; y++) {

			for (int i = 0; i < 2; i++) { // floor then ceiling

				int xBegin = -1;

				for (int x = 0; x < room.xChunks * CHUNK_WIDTH; x++) {

					Room.Block air = room.getBlock(x, y + (i == 0 ? 1 : -1));
					boolean isWall = room.getBlock(x, y) != AIR && air == AIR;

					if (xBegin == -1 && isWall) xBegin = x;
					else if (xBegin != -1 && !isWall) {

						int lineY = i == 0 ? y + 1 : y;
						AALine l = new AALine(xBegin, lineY, x - xBegin, true);
						add(l);
						xBegin = -1;
					}
				}
			}
		}
	}

}
