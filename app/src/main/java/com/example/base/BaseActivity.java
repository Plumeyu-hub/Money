package com.example.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Administrator
 * @date 2020/07/14
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

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
}
