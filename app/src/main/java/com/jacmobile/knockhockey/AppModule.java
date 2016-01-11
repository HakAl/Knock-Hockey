package com.jacmobile.knockhockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

import com.jacmobile.knockhockey.opengl.ColorShaderProgram;
import com.jacmobile.knockhockey.opengl.LinkedShaders;
import com.jacmobile.knockhockey.opengl.ShaderProgram;
import com.jacmobile.knockhockey.opengl.TextureShaderProgram;
import com.jacmobile.knockhockey.view.GLRenderer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.jacmobile.knockhockey.opengl.GLUtils.readTextFileFromResource;

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

    @Provides @Singleton public LinkedShaders provideLinkedShaders()
    {
        return new LinkedShaders(readTextFileFromResource(app, R.raw.texture_vertex_shader),
                readTextFileFromResource(app, R.raw.texture_fragment_shader));
    }

    @Provides @Singleton public GLRenderer provideGLRenderer(LinkedShaders linkedShaders)
    {
        return new GLRenderer(app, linkedShaders);
    }
}