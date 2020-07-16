package com.snxun.book;

import android.app.Application;

/**
 * @author zhouL
 * @date 2020/7/16
 */
public class App extends Application {

    private static App sInstance;

    public static App get() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
