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

	public List<DataBean> getmList() {
		return mList;
	}

	public void setmList(List<DataBean> mList) {
		this.mList = mList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_account, null);

		}
		TextView id = (TextView) convertView.findViewById(R.id.list_id_tv);
		TextView date = (TextView) convertView
				.findViewById(R.id.list_date_tv);
		TextView category = (TextView) convertView
				.findViewById(R.id.list_category_tv);
		TextView money = (TextView) convertView
				.findViewById(R.id.list_money_tv);
		// LinearLayout ic_delete = (LinearLayout) convertView
		// .findViewById(R.id.lin_dataitem_delete);
		DataBean dataBean = mList.get(position);
		if (dataBean != null) {
			id.setText(Integer.toString(dataBean.getmId()));
			date.setText(dataBean.getmDate());
			category.setText(dataBean.getmCategory());
			money.setText(dataBean.getmMoney());

			// ic_delete.setOnClickListener(new OnClickListener() {
			//
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
			// db = context
			// .openOrCreateDatabase("data.db", position, null);
			// // 获取所点击项的_id
			// TextView tv = (TextView) arg2.findViewById(R.id.tv_dataitem_id);
			// int num = 0;
			// // 删除指定数据
			// // 删除数据，成功返回删除的数据的行数，失败返回0
			// num = db.ic_delete("user", "id=?", new String[] { 4 + "" });
			// if (num > 0) {
			// Toast.makeText(context, "删除成功" + num,
			// Toast.LENGTH_SHORT).show();
			// list.remove(position);
			// notifyDataSetChanged();
			// } else {
			// Toast.makeText(context, "删除失败" + num,
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// }
			// });
		}
		return convertView;
	}
}
