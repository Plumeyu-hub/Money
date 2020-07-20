package com.snxun.book.ui.my.demo.rv;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
 *
 * @author zhouL
 * @date 2020/7/19
 */
public class RvActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, RvActivity.class);
        context.startActivity(starter);
    }

    /**
     * 返回按钮
     */
    @BindView(R.id.back_btn)
    TextView mBackBtn;

    /**
     * 列表
     */
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
        //LinearLayoutManager: 线性布局管理器 GridLayoutManager: 表格布局管理器 StaggeredGridLayoutManager: 瀑布流布局管理器
        //LinearLayoutManager(Context context),Context context ：上下文，初始化时，构造方法内部加载资源用
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //方向，垂直(RecyclerView.VERTICAL)和水平(RecyclerView.HORIZONTA )，默认为垂直.
        //layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //当我们确定Item的改变不会影响RecyclerView的宽高的时候可以设置setHasFixedSize(true)，
        // 并通过Adapter的增删改插方法去刷新RecyclerView，而不是通过notifyDataSetChanged()。
        // （其实可以直接设置为true，当需要改变宽高的时候就用notifyDataSetChanged()去整体刷新一下）
        //setHasFixedSize 的作用就是确保尺寸是通过用户输入从而确保RecyclerView的尺寸是一个常数。RecyclerView的Item宽或者高不会变。
        // 每一个Item添加或者删除都不会变。如果你没有设置setHasFixedSized没有设置的代价将会是非常昂贵的。
        // 因为RecyclerView会需要而外计算每个item的size,所以如当不是瀑布流时，设置这个可以避免重复的增删造成而外的浪费资源.
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

        mAdapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnItemLongClickListener(new RvAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
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
