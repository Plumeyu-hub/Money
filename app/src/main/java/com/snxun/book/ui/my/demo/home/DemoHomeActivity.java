package com.snxun.book.ui.my.demo.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.my.demo.tab.TabTestActivity;
import com.snxun.book.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 样例主页
 * @author zhouL
 * @date 2020/7/9
 */
public class DemoHomeActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, DemoHomeActivity.class);
        context.startActivity(starter);
    }

    /** 返回按钮 */
    @BindView(R.id.back_btn)
    TextView mBackBtn;
    /** tablayout样例按钮 */
    @BindView(R.id.tab_case_btn)
    Button mTabCaseBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_home;
    }

    @Override
    protected void findViews(){
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTabCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabTestActivity.start(DemoHomeActivity.this);
            }
        });
    }

}
