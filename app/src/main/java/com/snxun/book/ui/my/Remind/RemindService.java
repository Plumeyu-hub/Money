package com.snxun.book.ui.my.Remind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.snxun.book.event.RemindEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author wangshy
 * @date 2020/08/17
 */
public class RemindService extends Service {

    private static final int TIME_INTERVAL = 600000; // 10min 1000 * 60 * 10
    private long time;

    public RemindService() {
    }

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        //System.out.println("onCreate invoke");
        super.onCreate();
    }


    /**
     * Service被开始后调用
     * 每次通过startService()方法启动Service时都会被回调
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 在你需要订阅的类里去注册EventBus
        EventBus.getDefault().register(this);
        PendingIntent pIntent = PendingIntent.getBroadcast(RemindService.this, 0,
                new Intent(this, RemindReceiver.class), 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                time,
                TIME_INTERVAL, pIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RemindEvent(RemindEvent event) {
        time = event.getTime();
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //System.out.println("onDestroy invoke");
        EventBus.getDefault().unregister(this);
    }

}
