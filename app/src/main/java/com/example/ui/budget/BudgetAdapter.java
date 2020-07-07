package com.example.ui.budget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bean.BudgetBean;
import com.example.money.R;

import java.util.List;

public class BudgetAdapter extends BaseAdapter {
	private Context context;
	private List<BudgetBean> list;

	public BudgetAdapter(Context context, List<BudgetBean> list) {
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

	public List<BudgetBean> getList() {
		return list;
	}

	public void setList(List<BudgetBean> list) {
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.budgetitem, null);

		}

		TextView id = (TextView) convertView.findViewById(R.id.tv_bitem_id);
		TextView daytime = (TextView) convertView
				.findViewById(R.id.tv_bitem_daytime);
		TextView money = (TextView) convertView
				.findViewById(R.id.tv_bitem_money);
		TextView remarks = (TextView) convertView
				.findViewById(R.id.tv_bitem_remarks);
		BudgetBean bdata = list.get(position);

		if (bdata != null) {
			id.setText(Integer.toString(bdata.getId()));
			daytime.setText(bdata.getDaytime());
			remarks.setText(bdata.getRemarks());
			money.setText(bdata.getMoney());
		}
		String cid = list.get(position).getColorid();
		if ("1".equals(cid)) {
			money.setTextColor(Color.parseColor("#0A8F0A"));// 字体红色
			// convertView.setBackgroundColor(Color.parseColor("#ffffff"));//背景色
		} else if ("0".equals(cid)) {
			money.setTextColor(Color.parseColor("#991111"));// 字体绿色
		}else{
			
		}
		return convertView;
	}

}
