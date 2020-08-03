package com.snxun.book.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.snxun.book.R;
import com.snxun.book.widget.TitleLayout;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;


/**
 * @author Wangshy
 * @date 2020/07/14
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    /** 顶部标题布局 */
    private TitleLayout mTitleLayout;
    /** 内容布局 */
    private ViewGroup mContentLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startCreate();

        //设置内容视图
        setContentView(R.layout.activity_base_layout);
        initViews();
        setContainerView();

        //组件
        findViews();
        //监听
        setListeners();
        //初始化数据
        initData();
        endCreate();

    }

    private void initViews() {
        mTitleLayout = findViewById(R.id.title_layout);
        mContentLayout = findViewById(R.id.content_layout);
    }

    private void setContainerView() {
        View view = LayoutInflater.from(this).inflate(getLayoutId(), null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentLayout.addView(view, layoutParams);
    }

    /** 点击标题栏的返回按钮 */
    protected void clickBackBtn() {}

    @LayoutRes
    protected abstract int getLayoutId();

    protected void startCreate() {}

    protected abstract void findViews();

    protected void setListeners() {
        getTitleLayout().setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackBtn();
            }
        });
    }

    protected void initData() {}

    protected void endCreate() {}

    protected Context getContext() {
        return this;
    }

    /** 隐藏标题栏 */
    protected void goneTitleBar(){
        getTitleLayout().setVisibility(View.GONE);
    }

    /** 显示标题栏 */
    protected void showTitleBar(){
        getTitleLayout().setVisibility(View.VISIBLE);
    }

    /** 获取顶部标题栏控件 */
    protected TitleLayout getTitleLayout(){
        return mTitleLayout;
    }
}
