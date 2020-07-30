package com.snxun.book.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;


/**
 * @author Wangshy
 * @date 2020/07/14
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置内容视图
        setContentView(getLayoutId());

        startCreate();
        //组件
        findViews();
        //监听
        setListeners();
        //初始化数据
        initData();
        endCreate();

    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected void startCreate() {}

    protected abstract void findViews();

    protected void setListeners() {}

    protected void initData() {}

    protected void endCreate() {}

    protected Context getContext() {
        return this;
    }
}
