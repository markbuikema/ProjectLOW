package com.markbuikema.projectlow.views;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import com.markbuikema.projectlow.Tools;
import com.markbuikema.projectlow.model.Enemy;
import com.markbuikema.projectlow.model.Map;
import com.markbuikema.projectlow.model.Player;
import com.markbuikema.projectlow.model.TileType;

public class GameRenderer implements Renderer {

	public static final String TAG = "ProjectLow PlayView";
	public static final int ZOOM = 6;

	private ArrayList<Enemy> enemies;
	private ArrayList<Player> players;
	private Map currentMap;
	private int xOffset = 0;
	private int yOffset = 0;
	private Context context;
	
	public static int depth = 0;

	public GameRenderer(Context context) {
		this.context = context;
		players = new ArrayList<Player>();
		enemies = new ArrayList<Enemy>();

		currentMap = new Map();

		// populate the map with tiles

		for (int x = -Tools.MAP_WIDTH; x < Tools.MAP_WIDTH; x++) {
			for (int y = -Tools.MAP_HEIGHT; y < Tools.MAP_HEIGHT; y++) {
				currentMap.putTile(x, y, TileType.GRASS);
				depth++;
			}
		}

	}

	public TileType randomType() {
		if (Math.random() > 0.3) {
			return TileType.GRASS;
		} else
			return TileType.SAND;
	}

	/**
	 * Change the camera view on the map.
	 * 
	 * @param xOffset
	 *          the xOffset to set it to
	 * @param yOffset
	 *          the yOffset to set it to
	 * @param append
	 *          whether the passed parameters should be added to its original
	 *          values
	 */
	public void setOffset(int xOffset, int yOffset, boolean append) {
		if (append) {
			this.xOffset += xOffset;
			this.yOffset += yOffset;
		} else {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
		currentMap.loadTextures(gl);

		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClearDepthf(1f);

		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL10.GL_GREATER, 0);

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

	}

	@Override
	public void onDrawFrame(GL10 gl) {

		gl.glEnable(GL10.GL_DITHER);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslatef(xOffset, yOffset, 0.0f);

		GLU.gluLookAt(gl, 0, 0, ZOOM, 0, 0, 0, 0, 2, 0);

		currentMap.draw(gl);
		Tools.recycleBitmaps();

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 25);

	}

}
