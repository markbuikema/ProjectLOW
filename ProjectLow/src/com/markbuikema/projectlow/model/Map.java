package com.markbuikema.projectlow.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.microedition.khronos.opengles.GL10;

import com.markbuikema.projectlow.ContextProvider;

public class Map {

	private final static String TAG = "ProjectLow Map";

	

	// The tiles. Key of root map is x. Value of root map is a Map with key y and
	// value tile.
	private HashMap<Integer, HashMap<Integer, Tile>> tiles;

	public Map() {
		tiles = new HashMap<Integer, HashMap<Integer, Tile>>();

		

	}


	/**
	 * Returns the tile at the given coordinates
	 * 
	 * @param x
	 * @param y
	 * @return the Tile at the coordinates
	 */
	public Tile getTile(int x, int y) {
		return tiles.get(x).get(y);
	}

	/**
	 * Adds a tile to the map at the passed coordinates. If there is already a
	 * tile on that coordinate, the tileType of that tile is changed.
	 * 
	 * @param x
	 *          the X of the tile to put
	 * @param y
	 *          the Y of the tile to put
	 * @param tileType
	 *          the type of the tile
	 */
	public void putTile(int x, int y, TileType tileType) {

		if (!tiles.containsKey(x)) {
			tiles.put(x, new HashMap<Integer, Tile>());
		}
		if (tiles.get(x).containsKey(y)) {
			tiles.get(x).get(y).setTileType(tileType);
		} else {
			tiles.get(x).put(y, new Tile(x, y, tileType));
		}
	}


	public void draw(GL10 gl) {
		for (Entry<Integer, HashMap<Integer, Tile>> x : tiles.entrySet()) {
			for (Entry<Integer, Tile> y : x.getValue().entrySet()) {
				y.getValue().draw(gl);
			}
		}
	}


	public void loadTextures(GL10 gl) {
		for (Entry<Integer, HashMap<Integer, Tile>> x : tiles.entrySet()) {
			for (Entry<Integer, Tile> y : x.getValue().entrySet()) {
				y.getValue().loadGLTexture(gl, ContextProvider.getInstance().getContext());
			}
		}		
	}
}
