package com.snxun.book.ui.money.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.ui.money.bean.DataBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 14043
 * @date 2020/7/22
 */
public class SearchRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DataBean> mListData;

    public SearchRvAdapter(Context context, List<DataBean> mListData) {
        this.mContext = context;
        this.mListData = mListData;
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private SearchRvAdapter.OnItemClickListener mOnItemClickListener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(SearchRvAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private SearchRvAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(SearchRvAdapter.OnItemLongClickListener onItemLongClickListener) {
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
        return new SearchRvAdapter.DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_list, parent, false));
    }

    /**
     * 绑定VIewHolder，加载数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataBean dataBean = mListData.get(position);
        if (dataBean == null || !(holder instanceof SearchRvAdapter.DataViewHolder)) {
            return;
        }
        showItem((SearchRvAdapter.DataViewHolder) holder, dataBean);

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

    private void showItem(SearchRvAdapter.DataViewHolder holder, DataBean dataBean) {
        holder.rvListIdTv.setText(Integer.toString(dataBean.getmId()));
        holder.rvListDateTv.setText(dataBean.getmDate());
        holder.rvListCategoryTv.setText(dataBean.getmCategory());
        holder.rvListMoneyTv.setText(dataBean.getmMoney());
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

        @BindView(R.id.rv_list_id_tv)
        TextView rvListIdTv;

        @BindView(R.id.rv_list_date_tv)
        TextView rvListDateTv;

        @BindView(R.id.rv_list_category_tv)
        TextView rvListCategoryTv;

        @BindView(R.id.rv_list_money_tv)
        TextView rvListMoneyTv;

        private DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
