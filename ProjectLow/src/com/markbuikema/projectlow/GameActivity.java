package com.markbuikema.projectlow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.markbuikema.projectlow.ui.JoyStick;
import com.markbuikema.projectlow.views.GameRenderer;

public class GameActivity extends Activity {

	private static final String TAG = "ProjectLow GameActivity";

	GLSurfaceView view;
	GameRenderer renderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ContextProvider.getInstance().setContext(this);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		renderer = new GameRenderer(this);
		view = new GLSurfaceView(this);
		view.setRenderer(renderer);
		setContentView(view);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Point p = new Point();
		wm.getDefaultDisplay().getSize(p);
		Tools.SCREEN_WIDTH = p.x;
		Tools.SCREEN_HEIGHT = p.y;
		// Do not add any code before this line

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent e) {

				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (e.getX() >= Tools.SCREEN_WIDTH / 2)
						return true;

					float x = Tools.glCoordX(e.getX());
					float y = Tools.glCoordY(e.getY());

					renderer.getJoyStick().show(x, y);
					Log.d(TAG, "Now showing joystick (" + x + "," + y + ")");
					break;
				case MotionEvent.ACTION_UP:
					renderer.getJoyStick().hide();
					Log.d(TAG, "Now hiding joystick");
					break;

				case MotionEvent.ACTION_MOVE:
					if (renderer.getJoyStick().isPressed())
						renderer.getJoyStick().moveStick(Tools.glCoordX(e.getX()), Tools.glCoordY(e.getY()));

				}

				return true;
			}

		});

	}

	public GLSurfaceView getView() {
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}

	public Renderer getRenderer() {
		return renderer;
	}

}
