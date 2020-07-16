package com.snxun.book.ui.my.budget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.login.LoginActivity;
import com.snxun.book.ui.my.budget.bean.BudgetBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BudgetActivity extends BaseActivity {
    /**
     * 返回按钮
     */
    @BindView(R.id.budget_back_btn)
    ImageView mBudgetBackBtn;
    /**
     * 预算刷新按钮
     */
    @BindView(R.id.budget_refresh_btn)
    ImageView mBudgetRefreshBtn;
    /**
     * 新增预算按钮
     */
    @BindView(R.id.budget_add_btn)
    ImageView mBudgetAddBtn;
    /**
     * 数据库
     */
    private SQLiteDatabase mDb;

    private Cursor cs;// 游标对象，用来报错查询返回的结果集
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    /**
     * list
     */
    @BindView(R.id.budget_lv)// 第一步:listview控件实例化
    ListView mBudgetLv;
    private List<BudgetBean> mBudgetList;
    private BudgetAdapter mBudgetListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_budget;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListeners() {
        super.setListeners();

        //返回
        mBudgetBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //预算刷新
        mBudgetRefreshBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshBudget();
            }
        });

        //新增预算
        mBudgetAddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
				startActivity(new Intent(BudgetActivity.this, AddBudgetActivity.class));
            }
        });

        // 长按删除
        mBudgetLv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                // 获取所点击项的id
                TextView idTv = (TextView) arg1.findViewById(R.id.budget_id_tv);
                final String id = idTv.getText().toString();
                // 通过Dialog提示是否删除
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        BudgetActivity.this);
                builder.setMessage(R
						.string.delete_text);
                // 确定按钮点击事件
                builder.setPositiveButton(R
								.string.app_yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String userId = String.valueOf(mUserId);
                                // 删除指定数据
                                int num;
                                // 删除数据，成功返回删除的数据的行数，失败返回0
                                num = mDb.delete("budget", "id=? and userid=?",
                                        new String[]{id + "", userId + ""});
                                if (num > 0) {
                                    Toast.makeText(BudgetActivity.this,
											R
													.string.delete_yes + num, Toast.LENGTH_SHORT)
                                            .show();
                                    // 删掉长按的item
                                    mBudgetList.remove(arg2);
                                    // 动态更新listview
                                    mBudgetListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(BudgetActivity.this,
											R
													.string.delete_no + num, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
                // 取消按钮点击事件
                builder.setNegativeButton(R
								.string.app_no,
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

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
        initListView();
    }

    /**
     * 初始化预算信息列表
     */
    private void initListView() {
        // 第二步:初始化数据
        mBudgetList = new ArrayList<>();
        // 第三步:实例化适配器
        mBudgetListAdapter = new BudgetAdapter(this, mBudgetList);
        // 第四步:listview设置适配器
        mBudgetLv.setAdapter(mBudgetListAdapter);
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    /**
     * 显示用户信息，获取当前登录的用户名
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SP_USER_CODE, MODE_PRIVATE);
        if (sharedPreferences != null) {// 判断文件是否存在
            mUserId = sharedPreferences.getInt(LoginActivity.SP_USERID_CODE, 0);
        }
    }

    public void refreshBudget() {
        mBudgetList.clear();
        String userId = String.valueOf(mUserId);
        BudgetBean budgetBean;
        String btime = null, bmoney = null;
        String cid = "";
        cs = mDb.query("budget", new String[]{"id", "money", "time",
                        "remarks", "userid"}, "userid=?", new String[]{userId + ""},
                null, null, "time ASC");
        if (cs != null) {

            while (cs.moveToNext()) {
                int bid = cs.getInt(cs.getColumnIndex("id"));// 得到列名id对于的值
                bmoney = cs.getString(cs.getColumnIndex("money"));
                btime = cs.getString(cs.getColumnIndex("time"));
                String bremarks = cs.getString(cs.getColumnIndex("remarks"));
                int buserid = cs.getInt(cs.getColumnIndex("userid"));

                float bsum = 0;
                cs = mDb.rawQuery(
                        "select sum(aomoney) from expenditure where aotime=? and aouserid=?",
                        new String[]{btime + "", userId + ""});

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

                budgetBean = new BudgetBean(bmoney, bremarks, btime, bid, buserid,
                        cid);
                mBudgetList.add(budgetBean);
            }
        }
//        Collections.sort(mBudgetList, new Comparator<BudgetBean>() {
//            @Override
//            public int compare(BudgetBean lhs, BudgetBean rhs) {
//                int a = Integer.parseInt(lhs.getDate());
//                int b = Integer.parseInt(rhs.getDate());
//                int diff = a - b;
//                if (diff > 0) {
//                    return 1;
//                } else if (diff < 0) {
//                    return -1;
//                }
//                return 0;// 相等为0
//            }
//        });
        mBudgetListAdapter.notifyDataSetChanged();
    }
}
