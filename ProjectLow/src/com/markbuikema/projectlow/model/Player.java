package com.markbuikema.projectlow.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.markbuikema.projectlow.ContextProvider;
import com.markbuikema.projectlow.R;
import com.markbuikema.projectlow.Tools;
import com.markbuikema.projectlow.Tools.Direction;
import com.markbuikema.projectlow.views.GameRenderer;

public class Player {

	private final static String TAG = "ProjectLow Player";

	private FloatBuffer vertBuffer;
	private float[] vertices = { -1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f };

	private FloatBuffer textureBuffer;
	private float[] texture = { 0.0f, 0.25f, 0.0f, 0.0f, 0.25f, 0.25f, 0.25f, 0.0f };

	private ShortBuffer pBuff;
	private short[] pIndex = { 0, 1, 2, 3 };

	private int[] textures = new int[1];

	private float xMovement = 0;
	private float yMovement = 0;

	private float x = 0;
	private float y = 0;

	private int dir;

	public Player() {
		setDirection(Direction.SE);

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

	public void stopMoving() {
		xMovement = 0f;
		yMovement = 0f;
	}

	public void setMovement(float x, float y) {
		xMovement = x;
		yMovement = y;
		
	}

	public void loadGLTexture(GL10 gl, Context context) {

		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_pack);
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

		bmp.recycle();
	}

	public void setDirection(int cardinalDir) {
		this.dir = cardinalDir;
		float left = cardinalDir > 3 ? 0.25f * (cardinalDir - 4) : 0.25f * cardinalDir;
		float right = left + 0.25f;
		float top = cardinalDir > 3 ? 0.25f : 0.0f;
		float bottom = top + 0.25f;
		texture = new float[] { left, bottom, left, top, right, bottom, right, top };
		if (textureBuffer != null) {
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}

	public void draw(GL10 gl) {

		GameRenderer r = ContextProvider.getInstance().getRenderer();
		if (xMovement > 0 && this.x + r.getXOffset() > Tools.glCoordX(Tools.SCREEN_WIDTH) - Tools.MAX_DISTANCE_X) {
			r.setOffset(-xMovement, 0, true);
		}
		
		if (yMovement > 0 && this.y + r.getYOffset() > Tools.glCoordY(0) - Tools.MAX_DISTANCE_Y) {
			r.setOffset(0, -yMovement, true);
		}
		
		if (xMovement < 0 && this.x +r.getXOffset() < Tools.glCoordX(0) + Tools.MAX_DISTANCE_X) {
			r.setOffset(-xMovement, 0, true);
		}
		
		if (yMovement < 0 && this.y + r.getYOffset() < Tools.glCoordY(Tools.SCREEN_HEIGHT) + Tools.MAX_DISTANCE_Y) {
			r.setOffset(0, -yMovement, true);
		}
		
		
		
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glPushMatrix();
		gl.glTranslatef(x + xMovement, y + yMovement, 0f);
		x += xMovement;
		y += yMovement;
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
