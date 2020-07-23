package com.snxun.book.ui.money.add;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.SubmitRemarkEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubmitRemarkActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, SubmitRemarkActivity.class);
        context.startActivity(starter);
    }

    /**
     * 返回按钮
     */
    @BindView(R.id.submit_remark_back_btn)
    ImageView mSubmitRemarkBackBtn;
    /**
     * 确定备注按钮
     */
    @BindView(R.id.submit_remark_btn)
    ImageView mSubmitRemarkBtn;
    /**
     * 备注编辑框
     */
    @BindView(R.id.add_in_submit_remark_edit)
    EditText mSubmitRemarkEdit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_submit_remark;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mSubmitRemarkBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 把编辑框的内容返回上个界面
        mSubmitRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String remark = mSubmitRemarkEdit.getText().toString().trim();
                if (TextUtils.isEmpty(remark)) {
                    Toast.makeText(getContext(), "编辑框内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventBus.getDefault().post(new SubmitRemarkEvent(remark));
                finish();
            }
        });
    }

}

