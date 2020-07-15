package com.snxun.book.ui.my.demo.tab;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.snxun.book.R;
/**
 * 栏目的Fragment页
 * @author zhouL
 * @date 2020/7/9
 */
public class TabFragment extends Fragment {

    private static final String EXTRA_TEXT = "extra_text";

    public static TabFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mTextTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initData();
    }

    private void findViews(View view) {
        mTextTv = view.findViewById(R.id.text_tv);
    }

    private void initData() {
        String text = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            text = bundle.getString(EXTRA_TEXT);
        }
        mTextTv.setText(TextUtils.isEmpty(text) ? "无数据" : text);
    }
}
