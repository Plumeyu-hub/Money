package com.example.demo.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.tab.TabTestActivity;
import com.example.money.R;

/**
 * 样例主页
 * @author zhouL
 * @date 2020/7/9
 */
public class DemoHomeActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, DemoHomeActivity.class);
        context.startActivity(starter);
    }

    /** 返回按钮 */
    private TextView mBackBtn;
    /** tablayout样例按钮 */
    private Button mTabCaseBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_home);
        findViews();
        setListners();
    }

    private void findViews() {
        mBackBtn = findViewById(R.id.back_btn);
        mTabCaseBtn = findViewById(R.id.tab_case_btn);
    }

    private void setListners() {
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
