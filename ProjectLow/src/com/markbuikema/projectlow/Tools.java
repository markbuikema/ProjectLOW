package com.markbuikema.projectlow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.markbuikema.projectlow.model.TileType;

public class Tools {

	public class Direction {
		// Directions
		public static final int N = 0;
		public static final int NE = 1;
		public static final int E = 2;
		public static final int SE = 3;
		public static final int S = 4;
		public static final int SW = 5;
		public static final int W = 6;
		public static final int NW = 7;
	}

	public static final float ZOOM = 6.2f; // 6.2f

	// The map dimensions, in tiles
	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 20;

	// The screen dimensions, in pixels, will be assigned in runtime
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	// The aspect ratio of a tile
	public static final float TILE_ASPECT_RATIO = 0.3f;// 0.3125

	// The speed of the player
	public static final float PLAYER_SPEED = 0.08f;

	// The maximum distance the joystick can be from the center of its socket,
	// divided by ZOOM.
	public static final float MAX_JOYSTICK_DISTANCE = .25f;

	// The distance to the edge of the screen, before the screen will start to
	// move along with the character
	public static final float MAX_DISTANCE_X = 5;
	public static final float MAX_DISTANCE_Y = 4;

	// Some bitmaps are stored here, to save memory
	private static Bitmap[] grass;
	private static int[] grassRes;
	private static Bitmap sand;

	public static void logConstants(String tag) {
		Log.d(tag, "SCREEN_WIDTH = " + Tools.SCREEN_WIDTH);
		Log.d(tag, "SCREEN_HEIGHT = " + Tools.SCREEN_HEIGHT);
	}

	public static Bitmap getTileBitmap(TileType type) {

		if (grassRes == null) {
			grassRes = new int[11];
			grassRes[0] = R.drawable.grass0;
			grassRes[1] = R.drawable.grass1;
			grassRes[2] = R.drawable.grass2;
			grassRes[3] = R.drawable.grass3;
			grassRes[4] = R.drawable.grass4;
			grassRes[5] = R.drawable.grass5;
			grassRes[6] = R.drawable.grass6;
			grassRes[7] = R.drawable.grass7;
			grassRes[8] = R.drawable.grass8;
			grassRes[9] = R.drawable.grass9;
			grassRes[10] = R.drawable.grass10;
		}
		if (grass == null)
			grass = new Bitmap[11];

		int random;

		switch (type) {
		case GRASS:
			random = (int) (Math.random() * 11);
			if (grass[random] == null || (grass[random] != null && grass[random].isRecycled()))
				grass[random] = BitmapFactory.decodeResource(ContextProvider.getInstance().getContext().getResources(), grassRes[random]);
			return grass[random];
		case SAND:
			if (sand == null || (sand != null && sand.isRecycled()))
				sand = BitmapFactory.decodeResource(ContextProvider.getInstance().getContext().getResources(), R.drawable.tile_sand);
			return sand;
		default:
			random = (int) (Math.random() * 11);
			if (grass[random] == null || (grass[random] != null && grass[random].isRecycled()))
				grass[random] = BitmapFactory.decodeResource(ContextProvider.getInstance().getContext().getResources(), grassRes[random]);
			return grass[random];
		}

	}

	public static void recycleBitmaps() {

		if (grass == null)
			return;
		for (Bitmap bmp : grass) {
			if (bmp != null)
				bmp.recycle();
		}
	}

	public static float glCoordX(float screenX) {
		return (screenX - Tools.SCREEN_WIDTH / 2) / (Tools.SCREEN_WIDTH / 2) * Tools.ZOOM * 1.774f; // 11
	}

	public static float glCoordY(float screenY) {
		return (screenY - Tools.SCREEN_HEIGHT / 2) / (Tools.SCREEN_HEIGHT / 2) * -Tools.ZOOM * 1.033f;// 6.4
	}

	public static float pythagoras(float a, float b) {
		return (float) Math.sqrt(a * a + b * b);
	}

}
