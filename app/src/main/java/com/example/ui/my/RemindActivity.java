package com.example.ui.my;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.money.R;

import java.util.Calendar;

public class RemindActivity extends Activity {

	private TextView tv_remind_turn;
	private SharedPreferences sharedPreferences;
	private Boolean bool = false;
	private SharedPreferences.Editor editor;

	private Calendar calendar;// 时间对象
	private AlarmManager manager;// 定时服务
	private PendingIntent pIntent;// 延时意图

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind);
		tv_remind_turn = (TextView) findViewById(R.id.tv_remind_turn);

		//记住上次选择状态
		sharedPreferences = this.getSharedPreferences("userInfo", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		bool = sharedPreferences.getBoolean("remember", false);
		if (bool == false) {// off
			tv_remind_turn.setBackgroundResource(R.drawable.off);
		} else {// on
			tv_remind_turn.setBackgroundResource(R.drawable.open);
		}

		calendar = Calendar.getInstance();// 初始化，以当前系统时间填充
		manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

		// Intent i = new Intent(this, AddActivity.class);
		// pIntent = PendingIntent.getActivity(this, 0, i,
		// PendingIntent.FLAG_UPDATE_CURRENT);

		// Intent intent = new Intent();
		// intent.setAction("com.example.android_27.A");
		// pIntent = PendingIntent.getBroadcast(RemindActivity.this, 0x102,
		// intent, 0);
		pIntent = PendingIntent.getBroadcast(RemindActivity.this, 0,
				new Intent(this, MyReceiver.class), 0);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.remind_left:
			finish();
			break;
		case R.id.tv_remind_turn:
			if (bool == false) {// off
				setAlarm();
			} else {// on
				tv_remind_turn.setBackgroundResource(R.drawable.off);
				bool = false;
				editor.putBoolean("remember", bool);
				editor.commit();
				// 取消定时闹钟
				manager.cancel(pIntent);
			}
			break;
		default:
			break;
		}
	}

	// 设置定时闹钟
	public void setAlarm() {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);// 获取当前的小时数
		int min = calendar.get(Calendar.MINUTE);// 获取当前的分钟数

		new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker arg0, int nowHour,
							int nowMin) {
						// TODO Auto-generated method stub
						calendar.setTimeInMillis(System.currentTimeMillis());
						calendar.set(Calendar.HOUR_OF_DAY, nowHour);// 设置小时数
						calendar.set(Calendar.MINUTE, nowMin);// 设置分钟数
						calendar.set(Calendar.SECOND, 0);// 设置秒数

						// 设置一次闹钟
						// manager.set(AlarmManager.RTC_WAKEUP,
						// calendar.getTimeInMillis(), pIntent);
						// tv.setText("设置了一次闹钟，时间为"+nowHour+":"+nowMin);

						// 设置重复闹钟
						manager.setRepeating(AlarmManager.RTC_WAKEUP,
								calendar.getTimeInMillis(),
								10 * 1000, pIntent);

						tv_remind_turn.setBackgroundResource(R.drawable.open);
						bool = true;
						editor.putBoolean("remember", bool);
						editor.commit();
						// tv.setText("设置了重複闹钟，时间为" + nowHour + ":" + nowMin
						// + ",每次重复间隔为10s");

					}
				}, hour, min, true).show();

	}
}