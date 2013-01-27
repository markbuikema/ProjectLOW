package com.markbuikema.projectlow.ui;

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
import com.markbuikema.projectlow.model.Player;

public class JoyStick {

	private static final String TAG = "ProjectLow JoyStick-";

	private FloatBuffer vertBuffer;
	private float[] vertices = { -10.0f, -5.0f, 0.0f, -10.0f, -1.0f, 0.0f, -6.0f, -5.0f, 0.0f, -6.0f, -1.0f, 0.0f };

	private FloatBuffer textureBuffer;
	private float[] texture = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };

	private ShortBuffer pBuff;
	private short[] pIndex = { 0, 1, 2, 3 };

	private int[] textures = new int[1];

	private float initX;
	private float initY;
	private float x;
	private float y;
	private float xFinger;
	private float yFinger;

	private boolean show = false;

	public JoyStick() {

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

	public void show(float x, float y) {
		initX = x;
		initY = y;
		show = true;
		moveTo(x, y);
	}

	public void moveTo(float x, float y) {

		xFinger = x;
		yFinger = y;

		float distance = Tools.pythagoras(xFinger - initX, yFinger - initY);
		if (distance > Tools.ZOOM * Tools.MAX_JOYSTICK_DISTANCE) {

//			this.x = initX + (float) (Tools.ZOOM * Tools.MAX_JOYSTICK_DISTANCE * Math.cos(getDirection()));
//			this.y = initY + (float) (Tools.ZOOM * Tools.MAX_JOYSTICK_DISTANCE * Math.sin(getDirection()));

			this.x = initX + (Tools.ZOOM * Tools.MAX_JOYSTICK_DISTANCE * ((xFinger-initX)/distance));
			this.y = initY + (Tools.ZOOM * Tools.MAX_JOYSTICK_DISTANCE * ((yFinger-initY)/distance));
			
			
		} else {
			this.xFinger = x;
			this.yFinger = y;
			this.x = x;
			this.y = y;
		}
		float left = this.x - 1f;
		float top = this.y + 1f;
		float right = this.x + 1f;
		float bottom = this.y - 1f;

		vertices = new float[] { left, bottom, 0.0f, left, top, 0.0f, right, bottom, 0.0f, right, top, 0.0f };

		vertBuffer.put(vertices);
		vertBuffer.position(0);

		Player p = ContextProvider.getInstance().getRenderer().getCurrentMap().getPlayer();
		p.setDirection(getCardinalDirection());
		p.setMovement((this.x - initX) * Tools.PLAYER_SPEED, (this.y - initY) * Tools.PLAYER_SPEED);

	}

	public void disappear() {
		show = false;
		ContextProvider.getInstance().getRenderer().getCurrentMap().getPlayer().stopMoving();
	}

	public float getDirection() {
		if (initY - yFinger == 0)
			if (xFinger > initX)
				return 0f;
			else
				return (float) Math.PI;
		float dir = (float) (Math.atan((yFinger - initY) / (xFinger - initX)));
		if (dir < 0 && xFinger > initX && yFinger < initY) {
			dir = (float) (2 * Math.PI + dir);
		} else if (xFinger < initX) {
			dir = (float) (dir + Math.PI);
		}
		return dir;
	}

	public int getCardinalDirection() {
		int cDir = (int) (8 - (getDirection() - Math.PI / 2 - Math.PI / 8) / (2 * Math.PI) * 8);
		while (cDir < 0) {
			cDir += 8;
		}
		while (cDir > 7) {
			cDir -= 8;
		}
		return cDir;
	}

	public void draw(GL10 gl) {
		if (!show)
			return;

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

	public void loadGLTexture(GL10 gl, Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.joystick_inner);
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();
	}
}
