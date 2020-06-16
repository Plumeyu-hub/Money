package com.example.money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends Activity {
	private SQLiteDatabase db;// 数据库对象
	private Cursor cs;// 游标对象，用来报错查询返回的结果集

	EditText et_search_text;

	private ListView search_lv;
	private List<AddData> list;
	private addListAdapter adapter;

	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}

		et_search_text = (EditText) findViewById(R.id.et_search_text);

		// lv
		// 第一步:listview控件实例化
		search_lv = (ListView) findViewById(R.id.search_lv);
		// 第二步:初始化数据
		list = new ArrayList<AddData>();
		// 实例化适配器
		adapter = new addListAdapter(this, list);
		// listview设置适配器
		search_lv.setAdapter(adapter);
		search_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				AddData showdata = list.get(position);
				Intent i = new Intent(SearchActivity.this,
						DatainformationActivity.class);
				i.putExtra("showdata", showdata);
				i.putExtra("position", position);
				startActivityForResult(i, 0);
			}
		});

		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent != null) {
			if (resultCode == 102) {
				AddData updata = (AddData) intent
						.getSerializableExtra("updata");
				int position = intent.getIntExtra("position", 0);
				list.set(position, updata);
				adapter.notifyDataSetChanged();
			}

		}
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.search_left:
			finish();
			break;
		case R.id.tv_sreach_yes:
			String search_text = et_search_text.getText().toString().trim();
			if (TextUtils.isEmpty(search_text)) {
				list.clear();
				// 设置空列表的时候，显示为一张图片
				search_lv.setEmptyView(findViewById(R.id.lin_search_empty));
			} else {
				// 展示关联的数据
				setAdapter(1, search_text);
			}
			break;
		case R.id.tv_search_delete:
			et_search_text.setText("");
			break;
		default:
			break;
		}
	}

	public void setAdapter(int n, String str) {
		if (n == 1) {
			list.clear();
			AddData data;
			String uid = String.valueOf(userid);
			cs = db.query(
					"expenditure",
					new String[] { "aoid", "aocategory", "aomoney", "aotime",
							"aoaccount", "aoremarks", "aouserid" },
					"aocategory like ? or aoaccount like ? or aoremarks like ? or aotime like ? or aomoney like ? and aouserid=?",
					new String[] { "%" + str + "%", "%" + str + "%",
							"%" + str + "%", "%" + str + "%", "%" + str + "%",
							uid + "" }, null, null, null);
			if (cs != null) {
				while (cs.moveToNext()) {
					int aoid = cs.getInt(cs.getColumnIndex("aoid"));// 得到列名id对于的值
					String aocategory = cs.getString(cs
							.getColumnIndex("aocategory"));
					String aomoney = "-"
							+ cs.getString(cs.getColumnIndex("aomoney"));
					String aotime = cs.getString(cs.getColumnIndex("aotime"));
					String aoaccount = cs.getString(cs
							.getColumnIndex("aoaccount"));
					String aoremarks = cs.getString(cs
							.getColumnIndex("aoremarks"));
					data = new AddData(aocategory, aomoney, aoaccount,
							aoremarks, aotime, aoid, userid);
					list.add(data);
				}
				cs = db.query(
						"income",
						new String[] { "aiid", "aicategory", "aimoney",
								"aitime", "aiaccount", "airemarks", "aiuserid" },
						"aicategory like ? or aiaccount like ? or airemarks like ? or aitime like ? or aimoney like ? and aiuserid=?",
						new String[] { "%" + str + "%", "%" + str + "%",
								"%" + str + "%", "%" + str + "%",
								"%" + str + "%", uid + "" }, null, null, null);
				if (cs != null) {
					while (cs.moveToNext()) {
						int aiid = cs.getInt(cs.getColumnIndex("aiid"));// 得到列名id对于的值
						String aicategory = cs.getString(cs
								.getColumnIndex("aicategory"));
						String aimoney = "+"
								+ cs.getString(cs.getColumnIndex("aimoney"));
						String aitime = cs.getString(cs
								.getColumnIndex("aitime"));
						String aiaccount = cs.getString(cs
								.getColumnIndex("aiaccount"));
						String airemarks = cs.getString(cs
								.getColumnIndex("airemarks"));
						data = new AddData(aicategory, aimoney, aiaccount,
								airemarks, aitime, aiid, userid);
						list.add(data);
					}
				}
			}
			Collections.sort(list, new Comparator<AddData>() {

				@Override
				public int compare(AddData lhs, AddData rhs) {
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
			adapter.notifyDataSetChanged();
			// 设置空列表的时候，显示为一张图片
			search_lv.setEmptyView(findViewById(R.id.lin_search_empty));
		}
	}
}
