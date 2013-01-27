package com.markbuikema.projectlow.views;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import com.markbuikema.projectlow.ContextProvider;
import com.markbuikema.projectlow.Tools;
import com.markbuikema.projectlow.model.Map;
import com.markbuikema.projectlow.model.TileType;
import com.markbuikema.projectlow.ui.JoyStickSocket;

public class GameRenderer implements Renderer {

	public static final String TAG = "ProjectLow GameRenderer";

	private Map currentMap;
	private JoyStickSocket joyStick;
	private float xOffset = 0;
	private float yOffset = 0;
	

	public GameRenderer(Context context) {

		currentMap = new Map();
		joyStick = new JoyStickSocket();

		// populate the map with tiles

		for (int x = -Tools.MAP_WIDTH; x < Tools.MAP_WIDTH; x++) {
			for (int y = -Tools.MAP_HEIGHT; y < Tools.MAP_HEIGHT; y++) {
				currentMap.putTile(x, y, randomType());
			}
		}
		
	}

	public TileType randomType() {
		if (Math.random() > 0.1) {
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
	 *          
	 */
	public void setOffset(float xOffset, float yOffset, boolean append) {
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
		joyStick.loadGLTexture(gl, ContextProvider.getInstance().getContext());

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
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onDrawFrame(GL10 gl) {


		
		gl.glEnable(GL10.GL_DITHER);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

	
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		 


		GLU.gluLookAt(gl, 0, 0, Tools.ZOOM, 0, 0, 0, 0, 2, 0);

		gl.glPushMatrix();
		gl.glTranslatef(xOffset, yOffset, 0.0f);
		currentMap.draw(gl);
		gl.glPopMatrix();
		
		joyStick.draw(gl);
		
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

	public JoyStickSocket getJoyStick() {
		return joyStick;
	}

	public Map getCurrentMap() {
		return currentMap;
	}

	public float getXOffset() {
		return xOffset;
	}
	
	public float getYOffset() {
		return yOffset;
	}

	

}
