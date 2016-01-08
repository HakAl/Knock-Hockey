package com.jacmobile.knockhockey.utils;

import android.util.Log;

import com.jacmobile.knockhockey.BuildConfig;

public class Logger
{
    public static final void log(String s)
    {
        if (BuildConfig.DEBUG) Log.wtf("Logger", s);
    }
}
