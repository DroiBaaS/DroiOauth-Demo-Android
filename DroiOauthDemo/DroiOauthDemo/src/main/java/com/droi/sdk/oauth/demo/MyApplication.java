package com.droi.sdk.oauth.demo;

import android.app.Application;
import com.droi.sdk.oauth.DroiOauth;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        DroiOauth.initialize(getApplicationContext());
    }

}
