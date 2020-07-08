package com.example.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bean.AddBean;
import com.example.money.R;

import java.util.List;

public class AddListAdapter extends BaseAdapter {
	private Context context;
	private List<AddBean> list;

	// SQLiteDatabase db;// 数据库对象

	public AddListAdapter(Context context, List<AddBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public List<AddBean> getList() {
		return list;
	}

	public void setList(List<AddBean> list) {
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_data, null);

		}
		TextView id = (TextView) convertView.findViewById(R.id.dataitemid_tv);
		TextView daytime = (TextView) convertView
				.findViewById(R.id.dataitemdaytime_tv);
		TextView category = (TextView) convertView
				.findViewById(R.id.tv_dataitemcategory);
		TextView money = (TextView) convertView
				.findViewById(R.id.dataitemmoney_tv);
		// LinearLayout ic_delete = (LinearLayout) convertView
		// .findViewById(R.id.lin_dataitem_delete);
		AddBean aidata = list.get(position);
		if (aidata != null) {
			id.setText(Integer.toString(aidata.getId()));
			daytime.setText(aidata.getDaytime());
			category.setText(aidata.getCategory());
			money.setText(aidata.getMoney());

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
