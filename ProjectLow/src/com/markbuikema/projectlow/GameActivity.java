package com.markbuikema.projectlow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.markbuikema.projectlow.views.GameRenderer;

public class GameActivity extends Activity {
	
	GLSurfaceView view;
	GameRenderer renderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ContextProvider.getInstance().setContext(this);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		
		
		view = new GLSurfaceView(this);
		renderer = new GameRenderer(this);
		view.setRenderer(renderer);
		setContentView(view);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Point p = new Point();
		wm.getDefaultDisplay().getSize(p);
		Tools.SCREEN_WIDTH = p.x;
		Tools.SCREEN_HEIGHT = p.y;
		// Do not add any code before this line

		
		view.setOnTouchListener( new OnTouchListener() {

			@Override
			public boolean onTouch(View e, MotionEvent arg1) {
				
				renderer.setOffset(1, 1, true);
				
				return false;
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

}
