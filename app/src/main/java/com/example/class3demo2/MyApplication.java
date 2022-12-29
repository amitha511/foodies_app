package com.example.class3demo2;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    static private Context context;

    public static Context getAppContext() {
        return MyApplication.context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}