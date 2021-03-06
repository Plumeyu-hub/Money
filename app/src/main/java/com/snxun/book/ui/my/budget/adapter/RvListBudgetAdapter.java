package com.snxun.book.ui.my.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.bean.budget.BudgetBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class RvListBudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<BudgetBean> mListData;

    public RvListBudgetAdapter(Context context, List<BudgetBean> mListData) {
        this.mContext = context;
        this.mListData = mListData;
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private RvListBudgetAdapter.OnItemClickListener mOnItemClickListener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(RvListBudgetAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private RvListBudgetAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(RvListBudgetAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 创建VIewHolder，导入布局，实例化itemView
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rv_list_budget, parent, false));
    }

    /**
     * 绑定VIewHolder，加载数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BudgetBean budgetBean = mListData.get(position);
        if (budgetBean == null || !(holder instanceof DataViewHolder)) {
            return;
        }
        showItem((DataViewHolder) holder, budgetBean);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onClick(position);
                }
                return true;
            }
        });
    }

    private void showItem(DataViewHolder holder, BudgetBean budgetBean) {
        holder.mBudgetIdTv.setText(Integer.toString(budgetBean.getmId()));
        holder.mBudgetDateTv.setText(budgetBean.getmDate());
        holder.mBudgetRemarksTv.setText(budgetBean.getmRemarks());
        holder.mBudgetMoneyTv.setText(budgetBean.getmMoney());
        holder.mBudgetMoneyTv.setSelected(budgetBean.getIsSelected());
    }

    /**
     * 数据源的数量，item的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.budget_id_tv)
        TextView mBudgetIdTv;

        @BindView(R.id.budget_date_tv)
        TextView mBudgetDateTv;

        @BindView(R.id.budget_money_tv)
        TextView mBudgetMoneyTv;

        @BindView(R.id.budget_remarks_tv)
        TextView mBudgetRemarksTv;

        private DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
