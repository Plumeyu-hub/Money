package com.snxun.book.ui.money.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.R;

import java.util.List;

public class ListAdapter extends BaseAdapter {
	private Context mContext;
	private List<DataBean> mList;

	public ListAdapter(Context mContext, List<DataBean> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_account, null);

		}
		TextView id = (TextView) convertView.findViewById(R.id.rv_list_id_tv);
		TextView date = (TextView) convertView
				.findViewById(R.id.rv_list_date_tv);
		TextView category = (TextView) convertView
				.findViewById(R.id.rv_list_category_tv);
		TextView money = (TextView) convertView
				.findViewById(R.id.rv_list_money_tv);
		// LinearLayout ic_delete = (LinearLayout) convertView
		// .findViewById(R.id.lin_dataitem_delete);
		DataBean dataBean = mList.get(position);
		if (dataBean != null) {
			id.setText(Integer.toString(dataBean.getmId()));
			date.setText(dataBean.getmDate());
			category.setText(dataBean.getmCategory());
			money.setText(dataBean.getmMoney());
		}
		return convertView;
	}
}
