package com.snxun.book.ui.my.demo.rv;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView样例
 * @author zhouL
 * @date 2020/7/19
 */
public class RvActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, RvActivity.class);
        context.startActivity(starter);
    }

    /** 返回按钮 */
    @BindView(R.id.back_btn)
    TextView mBackBtn;

    /** 列表 */
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RvAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rv;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new RvAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter.setData(getTestList());
        mAdapter.notifyDataSetChanged();
    }

    private List<Integer> getTestList() {
        int[] colors = new int[]{R.color.color_FF6B6A, R.color.color_60B8FF};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int index = new Random().nextInt(2);
            list.add(colors[index]);
        }
        return list;
    }
}
