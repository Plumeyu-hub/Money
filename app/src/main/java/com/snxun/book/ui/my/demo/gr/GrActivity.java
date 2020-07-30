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
import com.snxun.book.ui.money.adapter.RvGrAdapter;
import com.snxun.book.ui.money.add.RvGrBean;

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
     *Gr
     */
    @BindView(R.id.gr_rv)
    RecyclerView mGrRv;
    private RvGrAdapter mRvGrAdapter;//自定义适配器，继承RecyclerView.Adapter
    private List<RvGrBean> mGrDataList;

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
        // 初始化数据列表
        mGrDataList = new ArrayList<>();

        String[] iconName = {"餐厅", "食材", "外卖", "水果", "零食", "烟酒茶水",
                "住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋服饰品", "日用品", "美容",
                "还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
                "公益捐赠", "宠物", "孩子", "长辈", "其他"};
        int[] iconRes = {R.drawable.selector_ic_tab_add_restaurant, R.drawable.selector_ic_tab_add_cook,
                R.drawable.selector_ic_tab_add_takeaway, R.drawable.selector_ic_tab_add_fruit, R.drawable.selector_ic_tab_add_snacks,
                R.drawable.selector_ic_tab_add_wine, R.drawable.selector_ic_tab_add_home, R.drawable.selector_ic_tab_add_house,
                R.drawable.selector_ic_tab_add_traffic, R.drawable.selector_ic_tab_add_car, R.drawable.selector_ic_tab_add_shopping,
                R.drawable.selector_ic_tab_add_post, R.drawable.selector_ic_tab_add_communication,
                R.drawable.selector_ic_tab_add_clothing, R.drawable.selector_ic_tab_add_daily, R.drawable.selector_ic_tab_add_beauty,
                R.drawable.selector_ic_tab_add_repayment, R.drawable.selector_ic_tab_add_investment,
                R.drawable.selector_ic_tab_add_office, R.drawable.selector_ic_tab_add_digital, R.drawable.selector_ic_tab_add_learn,
                R.drawable.selector_ic_tab_add_sport, R.drawable.selector_ic_tab_add_recreation,
                R.drawable.selector_ic_tab_add_medical, R.drawable.selector_ic_tab_add_maintenance,
                R.drawable.selector_ic_tab_add_travel, R.drawable.selector_ic_tab_add_social, R.drawable.selector_ic_tab_add_donate,
                R.drawable.selector_ic_tab_add_pet, R.drawable.selector_ic_tab_add_child, R.drawable.selector_ic_tab_add_elder,
                R.drawable.selector_ic_tab_add_other};
        for(int i = 0; i<iconName.length; i++){
            mGrDataList.add(new RvGrBean(iconName[i], iconRes[i], false));
        }

        mRvGrAdapter = new RvGrAdapter(getContext(), mGrDataList);
        //第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mGrRv.setLayoutManager(layoutManager);
        mGrRv.setHasFixedSize(true);
        mGrRv.setAdapter(mRvGrAdapter);
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

        mRvGrAdapter.setOnItemClickListener(new RvGrAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < mGrDataList.size(); i++) {
                    mGrDataList.get(i).setIsSelected( i == position);
                }
                mRvGrAdapter.notifyDataSetChanged();
            }
        });

        mRvGrAdapter.setOnItemLongClickListener(new RvGrAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
