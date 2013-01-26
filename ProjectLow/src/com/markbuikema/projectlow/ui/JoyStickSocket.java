package com.markbuikema.projectlow.ui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.opengl.GLUtils;
import android.util.Log;

import com.markbuikema.projectlow.ContextProvider;
import com.markbuikema.projectlow.R;

public class JoyStickSocket {
	
	private final static String TAG = "ProjectLow JoyStickSocket";

	private JoyStick stick;
	
	private boolean pressed;

	private FloatBuffer vertBuffer;
	private float[] vertices = {
				-10.0f, -5.0f, 0.0f,
				-10.0f, -1.0f, 0.0f,
				-6.0f, -5.0f, 0.0f, 
				-6.0f, -1.0f, 0.0f };

	private FloatBuffer textureBuffer;
	private float[] texture = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };

	private ShortBuffer pBuff;
	private short[] pIndex = { 0, 1, 2, 3 };

	private int[] textures = new int[1];

	public JoyStickSocket() {

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
		
		stick = new JoyStick();

	}

	public boolean isPressed() {
		return pressed;
	}
	
	public JoyStick getStick() {
		return stick;
	}

	public void show(float x, float y) {

		if (x>0) return;
		
		float left = x-2.0f;
		float top = y-2.0f;
		float right = x+2.0f;
		float bottom = y+2.0f;
		
		vertices = new float[] {
					left,bottom,0.0f,
					left,top,0.0f,
					right,bottom,0.0f,
					right,top,0.0f
					
		};
		vertBuffer.put(vertices);
		vertBuffer.position(0);

		pressed = true;
		
		stick.show(x,y);
		
		
		
	}

	public void hide() {
		pressed = false;
		stick.disappear();
	}

	public void loadGLTexture(GL10 gl, Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.joystick);
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		stick.loadGLTexture(gl, context);
		 bitmap.recycle();
	}

	public void draw(GL10 gl) {
		if (!pressed)
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
		
		stick.draw(gl);
		
		
	}

	public void moveStick(float glCoordX, float glCoordY) {
		stick.moveTo(glCoordX, glCoordY);
	}
}
