package com.snxun.book.ui.my.demo.gr;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class GrActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, GrActivity.class);
        context.startActivity(starter);
    }

    /**
     * 返回按钮
     */
    @BindView(R.id.back_btn)
    TextView mBackBtn;

    /**
     *
     */
    @BindView(R.id.gr_rv)
    RecyclerView mGrRv;
    private GrAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gr;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new GrAdapter(getContext());
        //第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),4, LinearLayoutManager.VERTICAL,false);
        mGrRv.setLayoutManager(layoutManager);
        mGrRv.setHasFixedSize(true);
        mGrRv.setAdapter(mAdapter);
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

        mAdapter.setOnItemClickListener(new GrAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnItemLongClickListener(new GrAdapter.OnItemLongClickListener() {
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

    private List<String> getTestList() {
        String[] mIconText = { "餐厅", "食材", "外卖", "水果", "零食", "烟酒茶饮料",
                "住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋饰服", "日用品", "美容",
                "还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
                "公益捐赠", "宠物", "孩子", "长辈", "其他" };

        List<String> list = new ArrayList<>();
        for (int i = 0; i < mIconText.length; i++) {
            list.add(mIconText[i]);
        }
        return list;
    }
}
