package com.example.money;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.bean.TabMainBean;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity{
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    //private String[] title={"明细","图表"};
    //private int[] selImg={R.drawable.ic_details_sel,R.drawable.ic_graph_sel};
    //private int[] norImg={R.drawable.ic_details_nor,R.drawable.ic_graph_nor};
    private static final TabMainBean[] TITLES_BEANS = new TabMainBean[]{new TabMainBean(TabMainBean.TAB_MAIN_DETAILS_ID, "明细"),
            new TabMainBean(TabMainBean.TAB_MAIN_GRAPH_ID, "图标")};

    /**
     * 翻页器
     */
    private ViewPager mViewPager;
    /**
     * Tab栏目
     */
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initData();



    }

    private void initData() {
        initMainViewPager();
        initMainTabLayout();

    }

    private void findViews() {
        mViewPager = findViewById(R.id.main_vp);
        mTabLayout = findViewById(R.id.main_tab);

    }

    @SuppressLint("InflateParams")
    private void initMainTabLayout() {
        mTabLayout.removeAllTabs();
        for (int i = 0; i < TITLES_BEANS.length; i++) {
            View tabMaimView = LayoutInflater.from(getContext()).inflate(R.layout.view_tab_main, null);
            ImageView tabMainImg = tabMaimView.findViewById(R.id.tab_main_img);
            TextView tabMainTv = tabMaimView.findViewById(R.id.tab_main_tv);

            TabMainBean bean = TITLES_BEANS[i];

            tabMainImg.setImageResource(bean.getId() == TabMainBean.TAB_MAIN_DETAILS_ID ? R.drawable.selector_ic_tab_main_details : R.drawable.selector_ic_tab_main_graph);
            tabMainTv.setText(bean.getTitle());

            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabMaimView), i == 0);
        }
    }

    private void initMainViewPager() {
        mViewPager.setOffscreenPageLimit(TITLES_BEANS.length);
        mViewPager.setAdapter(new MainActivity.MainAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private static class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DetailsFragment.newInstance(TITLES_BEANS[position].getTitle());
                case 1:
                    return GraphFragment.newInstance(TITLES_BEANS[position].getTitle());
            }
            return null;
        }

        @Override
        public int getCount() {
            return TITLES_BEANS.length;
        }
    }

    private Context getContext() {
        return this;
    }


}
