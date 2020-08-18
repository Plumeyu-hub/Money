package com.snxun.book.ui.my.Remind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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

    private static final int TIME_INTERVAL = 60000; // 2min 1000 * 60 * 2
    //AlarmManager.INTERVAL_DAY
    private long mTime;
    private AlarmManager alarmManager;
    private PendingIntent pIntent;

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
        super.onCreate();
        Log.d("Service", "onCreate");
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
        Log.d("Service", "onStartCommand");
        // 在你需要订阅的类里去注册EventBus
        EventBus.getDefault().register(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        pIntent = PendingIntent.getBroadcast(RemindService.this, 0,
                new Intent(this, RemindReceiver.class), 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                mTime,
                TIME_INTERVAL, pIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void RemindEvent(RemindEvent event) {
        mTime = event.getTime();
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "onDestroy");
        alarmManager.cancel(pIntent);
        EventBus.getDefault().unregister(this);
    }

}
