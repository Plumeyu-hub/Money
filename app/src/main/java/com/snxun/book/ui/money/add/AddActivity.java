package com.snxun.book.ui.money.add;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.money.main.bean.TabMainBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddActivity extends BaseActivity {
    private static final TabMainBean[] TITLES_BEANS = new TabMainBean[]{new TabMainBean(TabMainBean.TAB_MAIN_DETAILS_ID, "支出"),
            new TabMainBean(TabMainBean.TAB_MAIN_GRAPH_ID, "收入")};

    /**
     * 返回
     */
    @BindView(R.id.add_back_fab)
    FloatingActionButton mAddBackFab;
    /**
     * 翻页器
     */
    @BindView(R.id.add_vp)
    ViewPager mAddVp;
    /**
     * Tab栏目
     */
    @BindView(R.id.add_tab)
    TabLayout mAddTab;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mAddBackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initMainViewPager();
        initMainTabLayout();
    }

    @SuppressLint("InflateParams")
    private void initMainTabLayout() {
        mAddTab.removeAllTabs();
        for (int i = 0; i < TITLES_BEANS.length; i++) {
            View tabMaimView = LayoutInflater.from(getContext()).inflate(R.layout.view_tab_main, null);
            ImageView tabMainImg = tabMaimView.findViewById(R.id.tab_main_img);
            TextView tabMainTv = tabMaimView.findViewById(R.id.tab_main_tv);

            TabMainBean bean = TITLES_BEANS[i];

            tabMainImg.setImageResource(bean.getId() == TabMainBean.TAB_MAIN_DETAILS_ID ? R.drawable.ic_add_out : R.drawable.ic_add_in);
            tabMainTv.setText(bean.getTitle());

            mAddTab.addTab(mAddTab.newTab().setCustomView(tabMaimView), i == 0);
        }
    }

    private void initMainViewPager() {
        mAddVp.setOffscreenPageLimit(TITLES_BEANS.length);
        mAddVp.setAdapter(new AddActivity.FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mAddTab.setupWithViewPager(mAddVp);
    }


    private static class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AddOutFragment.newInstance(TITLES_BEANS[position].getTitle());
                case 1:
                    return AddInFragment.newInstance(TITLES_BEANS[position].getTitle());
            }
            return null;
        }

        @Override
        public int getCount() {
            return TITLES_BEANS.length;
        }
    }
}
