package com.snxun.book.ui.my.budget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.snxun.book.ui.my.budget.bean.BudgetBean;
import com.snxun.book.R;

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
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_budget, null);

		}

		TextView id = (TextView) convertView.findViewById(R.id.budgetitemid_tv);
		TextView daytime = (TextView) convertView
				.findViewById(R.id.budgetitemdaytime_tv);
		TextView money = (TextView) convertView
				.findViewById(R.id.budgetitemmoney_tv);
		TextView remarks = (TextView) convertView
				.findViewById(R.id.budgetitemremarks_tv);
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
