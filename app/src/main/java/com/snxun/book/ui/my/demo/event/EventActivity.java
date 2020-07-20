package com.snxun.book.ui.my.demo.event;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.DemoNotifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 事件接收Activity
 * @author zhouL
 * @date 2020/7/20
 */
public class EventActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, EventActivity.class);
        context.startActivity(starter);
    }

    /** 结果展示 */
    @BindView(R.id.result_tv)
    TextView mResultTv;
    /** 结果展示 */
    @BindView(R.id.go_btn)
    Button mGoBtn;

    @Override
    protected void startCreate() {
        super.startCreate();
        // 在你需要订阅的类里去注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_event;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputActivity.start(getContext());
            }
        });
    }

    // 订阅事件方法，threadMode = ThreadMode.MAIN表示在主线程订阅事件，一般都是订阅在主线程。onDemoNotifyEvent是方法名，
    // 方法名不限定，只要能表述清楚方法在做什么即可。方法入参一般是一个数据类（类似Bean），以Event结尾。
    // 切忌直接放一个List<String>或者直接丢一个String，后期代码跟起来会很痛苦。一定要用一个类包装起来，即使这个类里面只有一个变量
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDemoNotifyEvent(DemoNotifyEvent event) {
        if (!TextUtils.isEmpty(event.getText())) {
            mResultTv.setText(event.getText());
        }
    }

    @Override
    public void finish() {
        super.finish();
        // 在关闭页面的去解注册EventBus，一般Activity在finish里解注册，一般fragment在onDestroyView里解注册
        EventBus.getDefault().unregister(this);
    }
}
