package com.snxun.book.ui.my.demo.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.snxun.book.R;
import com.google.android.material.tabs.TabLayout;
import com.snxun.book.bean.demo.TabBean;

/**
 * TabLayout+ViewPager展示类
 * @author zhouL
 * @date 2020/7/9
 */
public class TabTestActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, TabTestActivity.class);
        context.startActivity(starter);
    }

    private static final TabBean[] TITLES_BEANS = new TabBean[]{new TabBean(TabBean.TAB_DETAILS_ID, "明细"),
            new TabBean(TabBean.TAB_GRAPH_ID, "图标")};

    /** 翻页器 */
    private ViewPager mViewPager;
    /** Tab栏目 */
    private TabLayout mTabLayout;
    /** 返回按钮 */
    private TextView mBackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_test);
        findViews();
        setListners();
        initData();
    }

    private void findViews() {
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mBackBtn = findViewById(R.id.back_btn);
    }

    private void setListners() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        initViewPager();
        initTabLayout();
    }

    @SuppressLint("InflateParams")
    private void initTabLayout() {
        mTabLayout.removeAllTabs();
        for (int i = 0; i < TITLES_BEANS.length; i++) {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.view_tab, null);
            ImageView iconImg = tabView.findViewById(R.id.ic_img);
            TextView textTv = tabView.findViewById(R.id.text_tv);

            TabBean bean = TITLES_BEANS[i];

            iconImg.setImageResource(bean.getId() == TabBean.TAB_DETAILS_ID ? R.drawable.selector_ic_tab_details : R.drawable.selector_ic_tab_graph);
            textTv.setText(bean.getTitle());

            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView), i == 0);
        }
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(TITLES_BEANS.length);
        mViewPager.setAdapter(new MainTabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private static class MainTabAdapter extends FragmentPagerAdapter {

        public MainTabAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return TabFragment.newInstance(TITLES_BEANS[position].getTitle());
                case 1:
                    return TabFragment.newInstance(TITLES_BEANS[position].getTitle());
                default:
                    return TabFragment.newInstance("");
            }
        }

        @Override
        public int getCount() {
            return TITLES_BEANS.length;
        }
    }

    private Context getContext(){
        return this;
    }
}
