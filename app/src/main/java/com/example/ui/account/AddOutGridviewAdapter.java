package com.example.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.money.R;

//自定义适配器（通过继承BaseAdapter）
public class AddOutGridviewAdapter extends BaseAdapter {
	Context context;// 声明适配器中引用的上下文
	// 将需要引用的图片和文字分别封装成数组
	private String[] iconName_out = { "餐厅", "食材", "外卖", "水果", "零食", "烟酒茶饮料",
			"住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋饰服", "日用品", "美容",
			"还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
			"公益捐赠", "宠物", "孩子", "长辈", "其他" };
	
	private int[] icon_outno = { R.drawable.restaurant_no, R.drawable.ic_cook_nor,
			R.drawable.takeaway_no, R.drawable.fruit_no, R.drawable.snacks_no,
			R.drawable.t_no, R.drawable.home_no, R.drawable.house_no,
			R.drawable.traffic_no, R.drawable.ic_car_nor, R.drawable.shopping_no,
			R.drawable.ic_ex_nor, R.drawable.ic_communication_nor,
			R.drawable.ic_clothing_nor, R.drawable.ic_daily_nor, R.drawable.ic_beauty_nor,
			R.drawable.repayment_no, R.drawable.investment_no,
			R.drawable.office_no, R.drawable.ic_digital_nor, R.drawable.learn_no,
			R.drawable.sport_no, R.drawable.recreation_no,
			R.drawable.medical_no, R.drawable.maintenance_no,
			R.drawable.travel_no, R.drawable.social_no, R.drawable.ic_donate_nor,
			R.drawable.pet_no, R.drawable.ic_child_nor, R.drawable.ic_elder_nor,
			R.drawable.ic_aoother_nor};
	private int[] icon_outyes = { R.drawable.restaurant_yes,
			R.drawable.ic_cook_sel, R.drawable.takeaway_yes, R.drawable.fruit_yes,
			R.drawable.snacks_yes, R.drawable.t_yes, R.drawable.home_yes,
			R.drawable.house_yes, R.drawable.traffic_yes, R.drawable.ic_car_sel,
			R.drawable.shopping_yes, R.drawable.ic_ex_sel,
			R.drawable.ic_communication_sel, R.drawable.ic_clothing_sel,
			R.drawable.ic_daily_sel, R.drawable.ic_beauty_sel,
			R.drawable.repayment_yes, R.drawable.investment_yes,
			R.drawable.office_yes, R.drawable.ic_digital_sel,
			R.drawable.learn_yes, R.drawable.sport_yes,
			R.drawable.recreation_yes, R.drawable.medical_yes,
			R.drawable.maintenance_yes, R.drawable.travel_yes,
			R.drawable.social_yes, R.drawable.ic_donate_sel, R.drawable.pet_yes,
			R.drawable.ic_child_sel, R.drawable.ic_elder_sel, R.drawable.ic_aoother_sel};
	private int clickTemp = -1;// 标识被选择的item
	private LayoutInflater layoutInflater;

	// 通过构造方法初始化上下文
	public AddOutGridviewAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return iconName_out.length;// images也可以
	}

	@Override
	public Object getItem(int position) {
		return iconName_out[position];// images也可以
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 通过布局填充器LayoutInflater填充网格单元格内的布局
		View v = layoutInflater.inflate(R.layout.additem, null);
		// 使用findViewById分别找到单元格内布局的图片以及文字
		ImageView iv = (ImageView) v.findViewById(R.id.iv_additem);
		TextView tv = (TextView) v.findViewById(R.id.tv_additem);
		// 引用数组内元素设置布局内图片以及文字的内容
		iv.setImageResource(icon_outno[position]);
		tv.setText(iconName_out[position]);

		if (clickTemp == position) { // 根据点击的Item当前状态设置背景
			iv.setImageResource(icon_outyes[position]);
			tv.setTextColor(context.getResources().getColor(R.color.addoutyes));
		}
		return v;
	}

	public void setSeclection(int posiTion) {
		clickTemp = posiTion;
	}
}