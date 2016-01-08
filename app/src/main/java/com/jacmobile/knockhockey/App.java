package com.jacmobile.knockhockey;

import android.app.Application;

public class App extends Application
{
    private AppComponent appComponent;

    @Override public void onCreate()
    {
        super.onCreate();
        this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent()
    {
        return appComponent;
    }
}