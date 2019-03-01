package com.yumore.frame.basic;

import android.app.Application;

import com.yumore.frame.helper.InitializeHelper;

/**
 * @author Nathaniel
 * @date 18-5-9-上午11:35
 */
public abstract class BaseApplication extends Application implements InitializeHelper {
    protected Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initialize(application);
    }
}