package com.snxun.book.ui.my;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.view.View;
import android.widget.TimePicker;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.utils.sp.SpManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemindActivity extends BaseActivity {

    /**
     * 定时按钮
     */
    @BindView(R.id.remind_btn)
    com.snxun.book.widget.ContentLayout mRemindBtn;

    private Calendar calendar;// 时间对象
    private AlarmManager manager;// 定时服务
    private PendingIntent pIntent;// 延时意图

    @Override
    protected int getLayoutId() {
        return R.layout.activity_remind;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.drawer_remind);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 判断定时记账的状态
                if (!SpManager.get().getRemindStatus()) {// ic_switch_nor
                    setAlarm();
                } else {// on
                    mRemindBtn.setIcon(R.drawable.ic_switch_nor);
                    //存储定时记账的状态
                    SpManager.get().setRemindStatus(false);
                    // 取消定时闹钟
                    manager.cancel(pIntent);
                }

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        if (!SpManager.get().getRemindStatus()) {// ic_switch_nor
            mRemindBtn.setIcon(R.drawable.ic_switch_nor);
            //mRemindBtn.setBackgroundResource(R.drawable.ic_switch_nor);
        } else {// on
            mRemindBtn.setIcon(R.drawable.ic_switch_sel);
            //mRemindBtn.setBackgroundResource(R.drawable.ic_switch_sel);
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
                new Intent(this, RemindReceiver.class), 0);
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
                        // manager.ic_set(AlarmManager.RTC_WAKEUP,
                        // calendar.getTimeInMillis(), pIntent);
                        // tv.setText("设置了一次闹钟，时间为"+nowHour+":"+nowMin);

                        // 设置重复闹钟
                        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                10 * 1000, pIntent);

                        mRemindBtn.setIcon(R.drawable.ic_switch_sel);
                        //存储定时记账的状态
                        SpManager.get().setRemindStatus(true);
                        // tv.setText("设置了重複闹钟，时间为" + nowHour + ":" + nowMin
                        // + ",每次重复间隔为10s");

                    }
                }, hour, min, true).show();

    }
}