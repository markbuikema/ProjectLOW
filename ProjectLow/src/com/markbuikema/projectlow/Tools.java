package com.markbuikema.projectlow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.markbuikema.projectlow.model.TileType;

public class Tools {

	// The map dimensions, in tiles
	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 20;

	// The screen dimensions, in pixels, will be assigned in runtime
	public static double SCREEN_WIDTH;
	public static double SCREEN_HEIGHT;

	// The aspect ratio of a tile
	public static final double TILE_ASPECT_RATIO = 0.3;// 0.3125

	// All bitmaps are stored here, to save memory
	private static Bitmap[] grass;
	private static int[] grassRes;

	public static void logConstants(String tag) {
		Log.d(tag, "SCREEN_WIDTH = " + Tools.SCREEN_WIDTH);
		Log.d(tag, "SCREEN_HEIGHT = " + Tools.SCREEN_HEIGHT);
	}

	public static Bitmap getBitmap(TileType type) {

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

}
