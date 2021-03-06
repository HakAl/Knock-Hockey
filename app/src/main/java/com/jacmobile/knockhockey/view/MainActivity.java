package com.jacmobile.knockhockey.view;

import android.app.Activity;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.jacmobile.knockhockey.App;
import com.jacmobile.knockhockey.R;
import com.jacmobile.knockhockey.opengl.GLUtils;

import javax.inject.Inject;

public class MainActivity extends Activity
{
    @Inject ConfigurationInfo configurationInfo;
    @Inject GLRenderer glRenderer;
    private GLSurfaceView glSurfaceView;
    private boolean redererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        if (GLUtils.supportsGLES2(configurationInfo)) {
            this.glSurfaceView = new GLSurfaceView(this);
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(glRenderer);
            glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(final View v, final MotionEvent event)
                {
                    if (event != null) {
                        final float normalX = (event.getX() / (float) v.getWidth()) * 2 - 1;
                        final float normalY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                glSurfaceView.queueEvent(new Runnable() {
                                    @Override public void run()
                                    {
                                        glRenderer.handleTouch(normalX, normalY);
                                    }
                                });
                                break;
                            case MotionEvent.ACTION_MOVE:
                                glSurfaceView.queueEvent(new Runnable() {
                                    @Override public void run()
                                    {
                                        glRenderer.handleDrag(normalX, normalY);
                                    }
                                });
                                break;
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
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
