package com.snxun.book.ui.my.Remind;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.lodz.android.core.utils.NotificationUtils;
import com.snxun.book.R;
import com.snxun.book.ui.money.add.AddActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Receive the broadcast and start the activity that will show the alarm
 */
public class RemindReceiver extends BroadcastReceiver {

    /**
     * 通知组id
     */
    private static final String NOTIFI_GROUP_ID = "g0001";
    /**
     * 主频道id
     */
    private static final String NOTIFI_CHANNEL_MAIN_ID = "c0001";
    /**
     * 下载频道id
     */
    private static final String NOTIFI_CHANNEL_DOWNLOAD_ID = "c0002";

    @Override
    public void onReceive(Context context, Intent intent) {

        /* start another activity - MyAlarm to display the alarm */

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Intent intent1 = new Intent(context, AddActivity.class);
        // 延时意图
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);

        initNotificationChannel(context);
        showNotify(context, pIntent);

    }

    /**
     * 初始化通知通道
     */
    private void initNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //分组（可选）
            //groupId要唯一
            NotificationChannelGroup group = new NotificationChannelGroup(NOTIFI_GROUP_ID, "通知组");
            NotificationUtils.create(context).createNotificationChannelGroup(group);// 设置通知组

            List<NotificationChannel> channels = new ArrayList<>();
            channels.add(getMainChannel());
            channels.add(getDownloadChannel());
            NotificationUtils.create(context).createNotificationChannels(channels);// 设置频道
        }
    }

    /**
     * 获取下载通道
     */
    private NotificationChannel getDownloadChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFI_CHANNEL_DOWNLOAD_ID, "下载通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);// 开启指示灯，如果设备有的话。
            channel.setLightColor(Color.GREEN);// 设置指示灯颜色
            channel.setDescription("应用下载通知频道");// 通道描述
            channel.enableVibration(false);// 开启震动
            channel.setGroup(NOTIFI_GROUP_ID);
            channel.setBypassDnd(true);// 设置绕过免打扰模式
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setShowBadge(false);// 设置是否显示角标
            return channel;
        }
        return null;
    }

    /**
     * 获取主通道
     */
    private NotificationChannel getMainChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFI_CHANNEL_MAIN_ID, "主通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);// 开启指示灯，如果设备有的话。
            channel.setLightColor(Color.GREEN);// 设置指示灯颜色
            channel.setDescription("应用主通知频道");// 通道描述
            channel.enableVibration(true);// 开启震动
            channel.setVibrationPattern(new long[]{100, 200, 400, 300, 100});// 设置震动频率
            channel.setGroup(NOTIFI_GROUP_ID);
            channel.canBypassDnd();// 检测是否绕过免打扰模式
            channel.setBypassDnd(true);// 设置绕过免打扰模式
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.canShowBadge();// 检测是否显示角标
            channel.setShowBadge(true);// 设置是否显示角标
            return channel;
        }
        return null;
    }

    /**
     * 显示通知
     */
    private void showNotify(Context context, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFI_CHANNEL_MAIN_ID);// 获取构造器
        builder.setTicker("记账提醒");// 通知栏显示的文字
        builder.setContentTitle("记账提醒");// 通知栏通知的标题
        builder.setContentText("Money提醒您：快来记账啦！");// 通知栏通知的详细内容（只有一行）
        builder.setAutoCancel(true);// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
        builder.setSmallIcon(R.mipmap.ic_launcher);//通知上面的小图标（必传）
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);//通知默认的声音 震动 呼吸灯
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//设置优先级，级别高的排在前面
        builder.setContentIntent(pendingIntent);// 关联PendingIntent
        Notification notification = builder.build();//构建通知
        NotificationUtils.create(context).send(notification);
    }
}