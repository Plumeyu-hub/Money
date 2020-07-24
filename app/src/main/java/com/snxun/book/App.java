package com.snxun.book;

import android.app.Application;
import android.content.Context;

import com.snxun.book.greendaolib.GreenDaoManager;

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
        initDb(getApplicationContext());
    }

    /** 初始化数据库 */
    private void initDb(Context context) {
        GreenDaoManager.get().init(context);
    }
}
