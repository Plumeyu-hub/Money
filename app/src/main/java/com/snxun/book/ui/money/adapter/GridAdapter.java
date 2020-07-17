package com.snxun.book.ui.money.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.snxun.book.R;

//自定义适配器（通过继承BaseAdapter）
public class GridAdapter extends BaseAdapter {
	Context mContext;// 声明适配器中引用的上下文
	// 将需要引用的图片和文字分别封装成数组
	private String[] mIconText = { "餐厅", "食材", "外卖", "水果", "零食", "烟酒茶饮料",
			"住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋饰服", "日用品", "美容",
			"还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
			"公益捐赠", "宠物", "孩子", "长辈", "其他" };
	
	private int[] mIconNor = { R.drawable.ic_restaurant_nor, R.drawable.ic_cook_nor,
			R.drawable.ic_takeaway_nor, R.drawable.ic_fruit_nor, R.drawable.ic_snacks_nor,
			R.drawable.ic_wine_nor, R.drawable.ic_home_nor, R.drawable.ic_house_nor,
			R.drawable.ic_traffic_nor, R.drawable.ic_car_nor, R.drawable.ic_shopping_nor,
			R.drawable.ic_post_nor, R.drawable.ic_communication_nor,
			R.drawable.ic_clothing_nor, R.drawable.ic_daily_nor, R.drawable.ic_beauty_nor,
			R.drawable.ic_repayment_nor, R.drawable.ic_investment_nor,
			R.drawable.ic_office_nor, R.drawable.ic_digital_nor, R.drawable.ic_learn_nor,
			R.drawable.ic_sport_nor, R.drawable.ic_recreation_nor,
			R.drawable.ic_medical_nor, R.drawable.ic_maintenance_nor,
			R.drawable.ic_travel_nor, R.drawable.ic_social_nor, R.drawable.ic_donate_nor,
			R.drawable.ic_pet_nor, R.drawable.ic_child_nor, R.drawable.ic_elder_nor,
			R.drawable.ic_other_nor};
	private int[] mIconSel = { R.drawable.ic_restaurant_sel,
			R.drawable.ic_cook_sel, R.drawable.ic_takeaway_sel, R.drawable.ic_fruit_sel,
			R.drawable.ic_snacks_sel, R.drawable.ic_wine_sel, R.drawable.ic_home_sel,
			R.drawable.ic_house_sel, R.drawable.ic_traffic_sel, R.drawable.ic_car_sel,
			R.drawable.ic_shopping_sel, R.drawable.ic_post_sel,
			R.drawable.ic_communication_sel, R.drawable.ic_clothing_sel,
			R.drawable.ic_daily_sel, R.drawable.ic_beauty_sel,
			R.drawable.ic_repayment_sel, R.drawable.ic_investment_sel,
			R.drawable.ic_office_sel, R.drawable.ic_digital_sel,
			R.drawable.ic_learn_sel, R.drawable.ic_sport_sel,
			R.drawable.ic_recreation_sel, R.drawable.ic_medical_sel,
			R.drawable.ic_maintenance_sel, R.drawable.ic_travel_sel,
			R.drawable.ic_social_sel, R.drawable.ic_donate_sel, R.drawable.ic_pet_sel,
			R.drawable.ic_child_sel, R.drawable.ic_elder_sel, R.drawable.ic_other_out_sel};
	private int mClickTemp = -1;// 标识被选择的item
	private LayoutInflater mLayoutInflater;

	// 通过构造方法初始化上下文
	public GridAdapter(Context mContext) {
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mIconText.length;// images也可以
	}

	@Override
	public Object getItem(int position) {
		return mIconText[position];// images也可以
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 通过布局填充器LayoutInflater填充网格单元格内的布局
		View v = mLayoutInflater.inflate(R.layout.grid_item_add_account, null);
		// 使用findViewById分别找到单元格内布局的图片以及文字
		ImageView iv = (ImageView) v.findViewById(R.id.icon_item_iv);
		TextView tv = (TextView) v.findViewById(R.id.text_item_tv);
		// 引用数组内元素设置布局内图片以及文字的内容
		iv.setImageResource(mIconNor[position]);
		tv.setText(mIconText[position]);

		if (mClickTemp == position) { // 根据点击的Item当前状态设置背景
			iv.setImageResource(mIconSel[position]);
			tv.setTextColor(mContext.getResources().getColor(R.color.color_FF6B6A));
		}
		return v;
	}

	public void setSeclection(int posiTion) {
		mClickTemp = posiTion;
	}
}