package com.markbuikema.projectlow;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.markbuikema.projectlow.views.GameRenderer;

public class ContextProvider {

	private static final String TAG = "ProjectLow ContextProvider";
	
	private static ContextProvider instance;
	private Context context;

	private ContextProvider() {
	}
	
	public Context getContext() {
		return context;
	}
	
	public GameRenderer getRenderer() {
		GameActivity activity = (GameActivity) context;
		return (GameRenderer) activity.getRenderer();
	}
	
	public GLSurfaceView getView() {
		GameActivity activity = (GameActivity) context;
		return activity.getView();
	}

	public static ContextProvider getInstance() {
		if (instance == null) {
			instance = new ContextProvider();
		}
		return instance;
	}

	public void setContext(Context context) {
		Log.d(TAG,"Context set!");
		this.context = context;
	}
}
