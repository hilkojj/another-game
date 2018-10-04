package com.hilkojj.game.utils;

public class AALine {

	/*

	^
	|
	|
	|	axis aligned line
	|
	O-------->

	 */

	public float x, y, length;
	public boolean horizontal;

	public AALine(float x, float y, float length, boolean horizontal) {

		this.x = x;
		this.y = y;
		this.length = length;
		this.horizontal = horizontal;
	}

	public void set(AALine l) {
		this.x = l.x;
		this.y = l.y;
		this.length = l.length;
		this.horizontal = l.horizontal;
	}

	public boolean intersects(AABB aabb) {

		if (horizontal) {

			return y >= aabb.center.y - aabb.halfSize.y
					&& y <= aabb.center.y + aabb.halfSize.y
					&& x <= aabb.center.x + aabb.halfSize.x
					&& x + length >= aabb.center.x - aabb.halfSize.x;

		} else {

			return x >= aabb.center.x - aabb.halfSize.x
					&& x <= aabb.center.x + aabb.halfSize.x
					&& y <= aabb.center.y + aabb.halfSize.y
					&& y + length >= aabb.center.y - aabb.halfSize.y;

		}
	}

}
