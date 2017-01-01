package com.visellico.util;

public class CoordinatePair<V> {
	
	public V obj;
	public Vector2i coords;
	
	public CoordinatePair(V obj, Vector2i coords) {
		this.obj = obj;
		this.coords = coords;
	}
}