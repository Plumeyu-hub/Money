package com.snxun.book.ui.my;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.ButterKnife;

public class HelpActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.drawer_help);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

}