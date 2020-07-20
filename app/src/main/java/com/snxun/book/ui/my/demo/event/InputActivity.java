package com.snxun.book.ui.my.demo.event;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.DemoNotifyEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 输入回调页面
 * @author zhouL
 * @date 2020/7/20
 */
public class InputActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, InputActivity.class);
        context.startActivity(starter);
    }

    /** 输入框 */
    @BindView(R.id.input_edt)
    EditText mInputEdt;
    /** 发送事件按钮 */
    @BindView(R.id.send_btn)
    Button mSendBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mInputEdt.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(getContext(), R.string.demo_input_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                // 在需要的位置通过EventBus的post方法，把Event的数据对象发送出去，有注册订阅的类就会收到这个事件，摆脱了ActivityResult的限制。
                EventBus.getDefault().post(new DemoNotifyEvent(str));
                finish();
            }
        });
    }
}
