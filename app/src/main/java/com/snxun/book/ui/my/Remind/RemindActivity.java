package com.snxun.book.ui.my.Remind;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.RemindEvent;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemindActivity extends BaseActivity {

    /**
     * 定时按钮
     */
    @BindView(R.id.remind_btn)
    com.snxun.book.widget.ContentLayout mRemindBtn;

    private Calendar calendar;// 时间对象
    private Intent intent;

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
                    stopService(intent);
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
        intent = new Intent();
        intent.setClass(this, RemindService.class);
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
                        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
                        //当前时间
                        long systemTime = System.currentTimeMillis();
                        calendar.setTimeInMillis(systemTime);
                        // 这里时区需要设置一下，不然会有8个小时的时间差
                        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                        calendar.set(Calendar.HOUR_OF_DAY, nowHour);// 设置小时数
                        calendar.set(Calendar.MINUTE, nowMin);// 设置分钟数
                        //calendar.set(Calendar.SECOND, 0);
                        //calendar.set(Calendar.MILLISECOND, 0);
                        // 选择的定时时间
                        long selectTime = calendar.getTimeInMillis();
                        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                        if(systemTime > selectTime) {
                            Toast.makeText(getContext(),"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            selectTime = calendar.getTimeInMillis();
                        }
                        // 计算现在时间到设定时间的时间差
                        long time = selectTime - systemTime;
                        firstTime += time;

                        EventBus.getDefault().post(new RemindEvent(firstTime));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        } else {
                            startService(intent);
                        }

                        mRemindBtn.setIcon(R.drawable.ic_switch_sel);
                        //存储定时记账的状态
                        SpManager.get().setRemindStatus(true);
                    }
                }, hour, min, true).show();

    }
}