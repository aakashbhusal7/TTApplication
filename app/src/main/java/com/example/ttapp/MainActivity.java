package com.example.ttapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
