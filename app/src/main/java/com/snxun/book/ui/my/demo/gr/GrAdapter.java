package com.snxun.book.ui.my.demo.gr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class GrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<GrDataBean> mListData;

    public GrAdapter(Context context, List<GrDataBean> mListData) {
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

//    //先声明一个int成员变量
//    private int mClickTemp= -1;// 标识被选择的item

    //其次定义一个方法用来绑定当前参数值的方法
    //此方法是在调用此适配器的地方调用的，此适配器内不会被调用到
//    public void setSeclection(int posiTion) {
//        this.mClickTemp = posiTion;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_item_add_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GrDataBean dataBean = mListData.get(position);
        if (dataBean == null || !(holder instanceof DataViewHolder)) {
            return;
        }
        showItem((DataViewHolder) holder, dataBean,position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                    //一定要刷新适配器 当条目发生改变这是必须的
                    notifyDataSetChanged();
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

    private void showItem(DataViewHolder holder, GrDataBean grDataBean,int position) {
        holder.iconItemIv.setImageResource(grDataBean.getImgResId());
        holder.iconItemIv.setSelected(grDataBean.getIsSelected());
        holder.textItemTv.setText(grDataBean.getName());
        holder.textItemTv.setSelected(grDataBean.getIsSelected());
        //在recyclerView的onBindViewHolder重写方法中判断当前position是否是选中的position
        //如果是就设置背景，不是就设置另一种颜色的背景
//        if (mClickTemp == position) {
//            holder.textItemTv.setTextColor(mContext.getResources().getColor(R.color.color_FF6B6A));
//            // TODO: 2020/07/21
//            mListData.set(position,grDataBean.setIsSelected(true));
//        } else {
//            holder.textItemTv.setTextColor(mContext.getResources().getColor(R.color.color_707070));
//
//        }

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
