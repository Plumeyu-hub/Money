package com.example.ui.budget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.BudgetBean;
import com.example.money.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BudgetActivity extends Activity {
	private SQLiteDatabase db;// 数据库对象
	private Cursor cs, cs1;// 游标对象，用来报错查询返回的结果集

	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;
	// list
	private ListView blv;
	private List<BudgetBean> blist;
	private BudgetAdapter badapter;

	TextView tv_bitem_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);
		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}

		tv_bitem_money = (TextView) findViewById(R.id.budgetitemmoney_tv);

		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

		// lv
		// 第一步:listview控件实例化
		blv = (ListView) findViewById(R.id.budget_lv);
		// 第二步:初始化数据
		blist = new ArrayList<BudgetBean>();
		// 实例化适配器
		badapter = new BudgetAdapter(this, blist);
		// listview设置适配器
		blv.setAdapter(badapter);
		blv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				BudgetBean bData = blist.get(position);
			}
		});
		// 长按删除
		blv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// 获取所点击项的_id
				TextView tv_id = (TextView) arg1.findViewById(R.id.budgetitemid_tv);
				final String id = tv_id.getText().toString();

				// 通过Dialog提示是否删除
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BudgetActivity.this);
				builder.setMessage("确定要删除吗？");
				// 确定按钮点击事件
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String uid = String.valueOf(userid);
								// 删除指定数据
								int num;
								// 删除数据，成功返回删除的数据的行数，失败返回0
								num = db.delete("budget", "id=? and userid=?",
										new String[] { id + "", uid + "" });
								if (num > 0) {
									Toast.makeText(BudgetActivity.this,
											"删除成功" + num, Toast.LENGTH_SHORT)
											.show();
									// 删掉长按的item
									blist.remove(arg2);
									// 动态更新listview
									badapter.notifyDataSetChanged();
								} else {
									Toast.makeText(BudgetActivity.this,
											"删除失败" + num, Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
				// 取消按钮点击事件
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

				return true;
			}
		});
	}

	public void refresh() {
		blist.clear();
		String uid = String.valueOf(userid);
		BudgetBean bData;
		String btime = null, bmoney = null;
		String cid = "";
		cs = db.query("budget", new String[] { "id", "money", "time",
				"remarks", "userid" }, "userid=?", new String[] { uid + "" },
				null, null, "time ASC");
		if (cs != null) {

			while (cs.moveToNext()) {
				int bid = cs.getInt(cs.getColumnIndex("id"));// 得到列名id对于的值
				bmoney = cs.getString(cs.getColumnIndex("money"));
				btime = cs.getString(cs.getColumnIndex("time"));
				String bremarks = cs.getString(cs.getColumnIndex("remarks"));
				int buserid = cs.getInt(cs.getColumnIndex("userid"));

				float bsum = 0;
				cs1 = db.rawQuery(
						"select sum(aomoney) from expenditure where aotime=? and aouserid=?",
						new String[] { btime + "", uid + "" });

				if (cs != null) {
					if (cs.moveToFirst()) {
						do {
							bsum = cs.getFloat(0);

						} while (cs.moveToNext());
					}
					System.out.println(bsum);

					float fmoney = Float.parseFloat(bmoney);
					if (bsum > fmoney) {
						cid = "1";
					} else {
						cid = "0";
					}
				}

				bData = new BudgetBean(bmoney, bremarks, btime, bid, buserid,
						cid);
				blist.add(bData);
			}
		}
		Collections.sort(blist, new Comparator<BudgetBean>() {
			@Override
			public int compare(BudgetBean lhs, BudgetBean rhs) {
				// TODO Auto-generated method stub
				int a = Integer.parseInt(lhs.getDaytime());
				int b = Integer.parseInt(rhs.getDaytime());
				int diff = a - b;
				if (diff > 0) {
					return 1;
				} else if (diff < 0) {
					return -1;
				}
				return 0;// 相等为0
			}
		});

		badapter.notifyDataSetChanged();

	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.budgetleft_tv:
			finish();
			break;
		case R.id.budgetadd_tv:
			Intent i1 = new Intent(BudgetActivity.this, AddBudgetActivity.class);
			startActivity(i1);
			break;
		case R.id.budgetrefresh_tv:
			refresh();
			break;
		default:
			break;
		}
	}
}