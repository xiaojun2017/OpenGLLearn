package com.xiaojun.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "xiaojun";
    private GLSurfaceView mGLSurfaceView;
    private boolean supportEs2;
    private boolean renderSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        supportEs2 = activityManager.getDeviceConfigurationInfo().reqGlEsVersion > 0x20000;
        if (supportEs2) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setRenderer(new DemoRender());
            renderSet = true;
            Log.i(TAG, "support es2.");
        } else {
            Log.e(TAG, "not support es2...");
        }
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renderSet) {
            mGLSurfaceView.onResume();
        }
    }
}
