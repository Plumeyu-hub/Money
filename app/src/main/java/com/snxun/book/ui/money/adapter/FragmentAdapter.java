package com.snxun.book.ui.money.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author 14043
 * @date 2020/7/12
 */
class FragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager mManager;

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
