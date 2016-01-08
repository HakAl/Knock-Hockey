package com.jacmobile.knockhockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;

import com.jacmobile.knockhockey.view.GLRenderer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module public class AppModule
{
    private final App app;

    public AppModule(App app)
    {
        this.app = app;
    }

    @Provides @Singleton public ActivityManager provideActivityManager()
    {
        return (ActivityManager) this.app.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Provides @Singleton public ConfigurationInfo provideConfigurationInfo(ActivityManager am)
    {
        return am.getDeviceConfigurationInfo();
    }

    @Provides @Singleton public GLRenderer provideGLRenderer()
    {
        return new GLRenderer(app);
    }
}