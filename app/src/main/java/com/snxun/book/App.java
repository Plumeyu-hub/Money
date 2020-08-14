package com.snxun.book;

import android.app.Application;
import android.content.Context;

import com.lodz.android.component.event.ActivityFinishEvent;
import com.snxun.book.greendaolib.GreenDaoManager;

import org.greenrobot.eventbus.EventBus;

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

    /**
     * 初始化数据库
     */
    private void initDb(Context context) {
        GreenDaoManager.get().init(context);
    }

    /**
     * 退出app
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * 关闭所有Activity
     */
    public void finishActivities() {
        // 发送关闭事件
        EventBus.getDefault().post(new ActivityFinishEvent());
    }
}
