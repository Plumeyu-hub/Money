package com.snxun.book.ui.my.demo.retrofit;

import android.content.Context;
import android.content.Intent;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Retrofit演示
 * @author zhouL
 * @date 2020/7/30
 */
public class RetrofitActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, RetrofitActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }
}
