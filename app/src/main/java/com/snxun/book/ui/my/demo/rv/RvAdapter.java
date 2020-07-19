package com.snxun.book.ui.my.demo.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RV列表适配器
 * @author zhouL
 * @date 2020/7/19
 */
public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<Integer> mDatas;

    public RvAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Integer color = getItem(position);
        if (color == null || !(holder instanceof DataViewHolder)) {
            return;
        }
        showItem((DataViewHolder) holder, color, position);
    }

    @SuppressLint("SetTextI18n")
    private void showItem(DataViewHolder holder, Integer color, int position) {
        holder.strTv.setText(position + ".  " + color);
        holder.strTv.setBackgroundColor(ContextCompat.getColor(mContext, color));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setData(List<Integer> list){
        mDatas = list;
    }

    private Integer getItem(int position){
        if (mDatas == null || mDatas.size() == 0){
            return null;
        }
        try {
            return mDatas.get(position);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    static class DataViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.str_tv)
        TextView strTv;

        private DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
