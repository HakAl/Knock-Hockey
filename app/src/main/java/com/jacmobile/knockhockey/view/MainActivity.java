package com.jacmobile.knockhockey.view;

import android.app.Activity;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.jacmobile.knockhockey.App;
import com.jacmobile.knockhockey.R;
import com.jacmobile.knockhockey.utils.GLUtils;

import javax.inject.Inject;

public class MainActivity extends Activity
{
    @Inject GLRenderer glRenderer;
    @Inject ConfigurationInfo configurationInfo;

    private boolean redererSet = false;
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        if (GLUtils.supportsGLEs2(configurationInfo)) {
            this.glSurfaceView = new GLSurfaceView(this);
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(glRenderer);
            this.redererSet = true;
            setContentView(glSurfaceView);
        } else {
            setContentView(R.layout.error_layout);
        }
    }

    @Override protected void onResume()
    {
        super.onResume();
        if (redererSet) {
            glSurfaceView.onResume();
        }
    }

    @Override protected void onPause()
    {
        super.onPause();
        if (redererSet) {
            glSurfaceView.onPause();
        }
    }
}
