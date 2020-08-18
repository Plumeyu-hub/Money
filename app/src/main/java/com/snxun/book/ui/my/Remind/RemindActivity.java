package com.snxun.book.ui.my.Remind;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.TimePicker;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.RemindEvent;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;

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

        intent = new Intent(getContext(), RemindService.class);
    }

    // 设置定时闹钟
    public void setAlarm() {
        // 实例化一个Calendar类，并取当前时间
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker arg0, int nowHour,
                                          int nowMin) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();//获取日期对象
                        c.setTimeInMillis(System.currentTimeMillis());        //设置Calendar对象
                        c.set(Calendar.HOUR_OF_DAY, nowHour);//设置闹钟小时数
                        c.set(Calendar.MINUTE, nowMin);//设置闹钟的分钟数
                        c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                        c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数

                        // 当前时间
                        Calendar currentTime = Calendar.getInstance();
                        long nowTime = currentTime.getTimeInMillis();
                        long setTime = c.getTimeInMillis();
                        //如果设置的闹钟时间比当前时间还小，你不能将闹钟定在过去，所以令其往前加一天
                        if (setTime <= nowTime) {
                            EventBus.getDefault().postSticky(new RemindEvent(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000));
                        } else {
                            EventBus.getDefault().postSticky(new RemindEvent(setTime));
                        }
                        //后台服务改为前台服务:启动前台服务
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        } else {
                            startService(intent);
                        }

                        mRemindBtn.setIcon(R.drawable.ic_switch_sel);
                        //存储定时记账的状态
                        SpManager.get().setRemindStatus(true);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }
}