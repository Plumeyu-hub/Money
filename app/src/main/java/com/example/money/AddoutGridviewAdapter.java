package com.example.money;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//自定义适配器（通过继承BaseAdapter）
public class AddoutGridviewAdapter extends BaseAdapter {
	Context context;// 声明适配器中引用的上下文
	// 将需要引用的图片和文字分别封装成数组
	private String[] iconName_out = { "餐厅", "食材", "外卖", "水果", "零食", "烟酒茶饮料",
			"住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋饰服", "日用品", "美容",
			"还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
			"公益捐赠", "宠物", "孩子", "长辈", "其他" };
	
	private int[] icon_outno = { R.drawable.restaurant_no, R.drawable.cook_no,
			R.drawable.takeaway_no, R.drawable.fruit_no, R.drawable.snacks_no,
			R.drawable.t_no, R.drawable.home_no, R.drawable.house_no,
			R.drawable.traffic_no, R.drawable.car_no, R.drawable.shopping_no,
			R.drawable.ex_no, R.drawable.communication_no,
			R.drawable.clothing_no, R.drawable.daily_no, R.drawable.beauty_no,
			R.drawable.repayment_no, R.drawable.investment_no,
			R.drawable.office_no, R.drawable.digital_no, R.drawable.learn_no,
			R.drawable.sport_no, R.drawable.recreation_no,
			R.drawable.medical_no, R.drawable.maintenance_no,
			R.drawable.travel_no, R.drawable.social_no, R.drawable.donate_no,
			R.drawable.pet_no, R.drawable.child_no, R.drawable.elder_no,
			R.drawable.aoothers_no };
	private int[] icon_outyes = { R.drawable.restaurant_yes,
			R.drawable.cook_yes, R.drawable.takeaway_yes, R.drawable.fruit_yes,
			R.drawable.snacks_yes, R.drawable.t_yes, R.drawable.home_yes,
			R.drawable.house_yes, R.drawable.traffic_yes, R.drawable.car_yes,
			R.drawable.shopping_yes, R.drawable.ex_yes,
			R.drawable.communication_yes, R.drawable.clothing_yes,
			R.drawable.daily_yes, R.drawable.beauty_yes,
			R.drawable.repayment_yes, R.drawable.investment_yes,
			R.drawable.office_yes, R.drawable.digital_yes,
			R.drawable.learn_yes, R.drawable.sport_yes,
			R.drawable.recreation_yes, R.drawable.medical_yes,
			R.drawable.maintenance_yes, R.drawable.travel_yes,
			R.drawable.social_yes, R.drawable.donate_yes, R.drawable.pet_yes,
			R.drawable.child_yes, R.drawable.elder_yes, R.drawable.aoothers_yes };
	private int clickTemp = -1;// 标识被选择的item
	private LayoutInflater layoutInflater;

	// 通过构造方法初始化上下文
	public AddoutGridviewAdapter(Context context) {
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