package com.snxun.book.ui.money.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.ui.money.bean.RvGrBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class RvGrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<RvGrBean> mListData;

    public RvGrAdapter(Context context, List<RvGrBean> mListData) {
        this.mContext = context;
        this.mListData = mListData;
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_item_add, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RvGrBean dataBean = mListData.get(position);
        if (dataBean == null || !(holder instanceof DataViewHolder)) {
            return;
        }
        showItem((DataViewHolder) holder, dataBean);

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

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    private void showItem(DataViewHolder holder, RvGrBean rvGrBean) {
        holder.iconItemIv.setImageResource(rvGrBean.getImgResId());
        holder.iconItemIv.setSelected(rvGrBean.getIsSelected());
        holder.textItemTv.setText(rvGrBean.getName());
        holder.textItemTv.setSelected(rvGrBean.getIsSelected());
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_item_iv)
        ImageView iconItemIv;
        @BindView(R.id.text_item_tv)
        TextView textItemTv;

        private DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
