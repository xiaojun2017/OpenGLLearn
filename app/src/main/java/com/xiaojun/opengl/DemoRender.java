package com.xiaojun.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DemoRender implements GLSurfaceView.Renderer {
    private static final String TAG = "DemoRender";

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1,0,0,0);
        Log.i(TAG, "onSurfaceCreated.");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Log.i(TAG, "onSurfaceChanged.");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        Log.i(TAG, "onDrawFrame");
    }
}
