package com.snxun.book.ui.my;

import android.content.Context;
import android.content.Intent;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.ButterKnife;


public class AboutActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.drawer_about);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }
}
