package com.hilkojj.game.utils;

import com.badlogic.gdx.math.Vector2;

public class Line {

	public Vector2 p0 = new Vector2(), p1 = new Vector2();

	public Vector2 point(int i) {
		return i == 0 ? p0 : p1;
	}

	public Line set(AALine l) {
		this.p0.x = l.x;
		this.p0.y = l.y;
		this.p1.x = l.horizontal ? l.x + l.length : l.x;
		this.p1.y = l.horizontal ? l.y : l.y + l.length;
		return this;
	}

	public Line set(Line l) {
		p0.set(l.p0);
		p1.set(l.p1);
		return this;
	}

	public Line move(Vector2 v) {
		p0.add(v);
		p1.add(v);
		return this;
	}

}
