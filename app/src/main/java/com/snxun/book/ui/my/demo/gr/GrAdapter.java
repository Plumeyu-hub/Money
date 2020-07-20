package com.snxun.book.ui.my.demo.gr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.ui.my.demo.rv.RvAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class GrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<String> mDatas;

    public GrAdapter(Context context) {
        this.mContext = context;
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
        return new DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.gr_img, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String s = getItem(position);
        if (s == null || !(holder instanceof RvAdapter.DataViewHolder)) {
            return;
        }
        showItem((DataViewHolder) holder, s, position);

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

    private String getItem(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return null;
        }
        try {
            return mDatas.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setData(List<String> list) {
        mDatas = list;
    }

    private void showItem(DataViewHolder holder, String s, int position) {
        holder.grText.setText(s);
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gr_text)
        TextView grText;

        private DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
