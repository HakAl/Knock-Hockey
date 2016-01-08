package com.jacmobile.knockhockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

import com.jacmobile.knockhockey.utils.LinkedShaders;
import com.jacmobile.knockhockey.view.GLRenderer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.jacmobile.knockhockey.utils.GLUtils.readTextFileFromResource;

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

    @Provides @Singleton public LinkedShaders provideShaderObjects()
    {
        return new LinkedShaders(readTextFileFromResource(app, R.raw.vertex_shader),
                readTextFileFromResource(app, R.raw.fragment_shader));
    }

    @Provides @Singleton public GLRenderer provideGLRenderer(LinkedShaders linkedShaders)
    {
        return new GLRenderer(linkedShaders);
    }
}