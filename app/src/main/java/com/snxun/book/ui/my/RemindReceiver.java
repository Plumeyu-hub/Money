package com.snxun.book.ui.my;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.snxun.book.R;
import com.snxun.book.ui.money.add.AddActivity;

/**
 * Receive the broadcast and start the activity that will show the alarm
 */
public class RemindReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		/* start another activity - MyAlarm to display the alarm */

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.mipmap.ic_launcher);
		Intent intent1 = new Intent(context, AddActivity.class);
		// 延时意图
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);

		// 设置状态栏中的小图片，尺寸一般建议在24×24
		// 这里也可以设置大图标
		// 设置显示的提示文字
		// 设置显示的标题
		// 消息的详细内容
		// 关联PendingIntent
		// 在TextView的右方显示的数字，可以在外部定义一个变量，点击累加setNumber(count),这时显示的和
		// 消息
		Notification notify = new Notification.Builder(context)
				.setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24
				.setLargeIcon(bitmap) // 这里也可以设置大图标
				.setTicker("记账提醒") // 设置显示的提示文字
				.setContentTitle("记账提醒") // 设置显示的标题
				.setContentText("Money提醒您：快来记账啦！") // 消息的详细内容
				.setContentIntent(pIntent) // 关联PendingIntent
				.setNumber(1) // 在TextView的右方显示的数字，可以在外部定义一个变量，点击累加setNumber(count),这时显示的和
				.getNotification(); // 需要注意build()是在API
		// level16及之后增加的，在API11中可以使用getNotificatin()来
		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		// 通知栏服务
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, notify);
		bitmap.recycle(); // 回收bitmap

		// 实例化通知
		// NotificationCompat.Builder builder = new NotificationCompat.Builder(
		// context);
		// builder.setDefaults(NotificationCompat.DEFAULT_ALL);
		// builder.setContentTitle("记账提醒");// 预览标题
		// builder.setSmallIcon(R.drawable.ic_logo);
		// builder.setContentText("Money提醒您：快来记账啦！");
		// Intent intent1 = new Intent(context, AddActivity.class);
		// pIntent = PendingIntent.getActivity(context, 0, intent1,
		// PendingIntent.FLAG_UPDATE_CURRENT);

		// builder.setContentIntent(pendingIntent);
		// Notification notification = builder.build();
		// 发送通知
		// manager.notify(0x103, notification);

	}
}