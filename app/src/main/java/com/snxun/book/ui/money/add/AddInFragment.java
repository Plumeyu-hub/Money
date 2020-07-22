package com.snxun.book.ui.money.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseFragment;
import com.snxun.book.ui.my.demo.gr.GrAdapter;
import com.snxun.book.ui.my.demo.gr.GrDataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/22
 */
public class AddInFragment extends BaseFragment {
    //mYourEditText.setInputType(EditorInfo.TYPE_NULL)

    /**
     * Gr
     */
    @BindView(R.id.add_in_gv)
    RecyclerView mAddInGv;
    private GrAdapter mGrAdapter;//自定义适配器，继承RecyclerView.Adapter
    private List<GrDataBean> mGrDataList;

    private int mLast;
    private int[] iconRes = {R.drawable.ic_wages_nor, R.drawable.ic_bonus_nor,
            R.drawable.ic_part_nor, R.drawable.ic_mis_nor, R.drawable.ic_collect_nor,
            R.drawable.ic_borrow_nor, R.drawable.ic_sell_nor, R.drawable.ic_financial_nor,
            R.drawable.ic_gifts_nor, R.drawable.ic_other_nor};

    private static final String EXTRA_TEXT = "extra_text";

    public static AddInFragment newInstance(String from) {
        AddInFragment addInFragment = new AddInFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEXT, from);
        addInFragment.setArguments(bundle);
        return addInFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_in;
    }

    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 初始化数据列表
        mGrDataList = new ArrayList<>();

        String[] iconName = {"工资", "奖金", "兼职收入", "意外所得", "收债", "借入",
                "投资回收", "投资收益", "礼金", "其他"};

        for (int i = 0; i < iconName.length; i++) {
            mGrDataList.add(new GrDataBean(iconName[i], iconRes[i], false));
        }

        mGrAdapter = new GrAdapter(getContext(), mGrDataList);
        //第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mAddInGv.setLayoutManager(layoutManager);
        mAddInGv.setHasFixedSize(true);
        mAddInGv.setAdapter(mGrAdapter);
    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);
        int[] iconResSel = {R.drawable.ic_wages_sel, R.drawable.ic_bonus_sel,
                R.drawable.ic_part_sel, R.drawable.ic_mis_sel, R.drawable.ic_collect_sel,
                R.drawable.ic_borrow_sel, R.drawable.ic_sell_sel,
                R.drawable.ic_financial_sel, R.drawable.ic_gifts_sel,
                R.drawable.ic_other_in_sel};

        mGrAdapter.setOnItemClickListener(new GrAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                mGrDataList.get(mLast).setImgResId(iconRes[mLast]);
                mGrDataList.get(mLast).setIsSelected(false);
                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                mGrDataList.get(position).setImgResId(iconResSel[position]);
                mGrDataList.get(position).setIsSelected(true);
                mLast = position;
                mGrAdapter.notifyDataSetChanged();
            }
        });

    }
}
