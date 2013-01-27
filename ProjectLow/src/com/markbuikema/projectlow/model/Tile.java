package com.markbuikema.projectlow.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

import com.markbuikema.projectlow.Tools;

/**
 * @author Mark
 * 
 */
public class Tile {

	private final static String TAG = "ProjectLow Tile";

	private FloatBuffer vertBuffer;
	private float[] vertices = { -1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f };

	private FloatBuffer textureBuffer;
	private float[] texture = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };

	private ShortBuffer pBuff;
	private short[] pIndex = { 0, 1, 2, 3 };

	private int[] textures = new int[1];

	private TileType tileType;
	private int x;
	private int y;

	public Tile(int x, int y, TileType type) {
		// store the passed parameters
		this.x = x;
		this.y = y;

		float a = (float) Tools.TILE_ASPECT_RATIO;

		float left = x - y - 1;
		float right = left + 2.0f;
		float bottom = -(2 * a * x) - (2 * a * y) - 1;
		float top = bottom + 2.0f;
		
		
		vertices = new float[] { left, bottom, 0, left, top, 0, right, bottom, 0, right, top, 0 };

		this.tileType = type;

		// ensure the tile has a type
		if (type == null)
			type = TileType.GRASS;

		// OpenGL initialization and memory allocation
		ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length * 4);
		bBuff.order(ByteOrder.nativeOrder());
		vertBuffer = bBuff.asFloatBuffer();
		vertBuffer.put(vertices);
		vertBuffer.position(0);

		bBuff = ByteBuffer.allocateDirect(pIndex.length * 2);
		bBuff.order(ByteOrder.nativeOrder());
		pBuff = bBuff.asShortBuffer();
		pBuff.put(pIndex);
		pBuff.position(0);

		bBuff = ByteBuffer.allocateDirect(texture.length * 4);
		bBuff.order(ByteOrder.nativeOrder());
		textureBuffer = bBuff.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

	}
	
	public TileType getTileType() {
		return tileType;
	}

	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}

	public void loadGLTexture(GL10 gl, Context context) {
		Bitmap bitmap = Tools.getTileBitmap(tileType);
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// bitmap.recycle();
	}

	public void draw(GL10 gl) {
		
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
