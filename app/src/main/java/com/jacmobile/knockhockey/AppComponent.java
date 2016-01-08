package com.jacmobile.knockhockey;

import com.jacmobile.knockhockey.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, })
public interface AppComponent
{
    void inject(MainActivity mainActivity);
}