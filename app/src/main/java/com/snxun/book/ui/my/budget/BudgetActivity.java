package com.snxun.book.ui.my.budget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.my.budget.adapter.RvListBudgetAdapter;
import com.snxun.book.bean.budget.BudgetBean;
import com.snxun.book.utils.sp.SpManager;

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
    /**
     * 当前登录的用户ID
     */
    private String mUserId;

    /**
     * RV列表
     */
    @BindView(R.id.budget_rv)
    RecyclerView mBudgetRv;
    private RvListBudgetAdapter mRvListBudgetAdapter;
    /**
     * 定义了一个数组List，里面只能存放BudgetBean
     */
    private List<BudgetBean> mBudgetList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_budget;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initRecyclerView();
    }

    /**
     * 设置Rv列表
     */
    private void initRecyclerView() {
        // 初始化数据列表
        mBudgetList = new ArrayList<>();
        mRvListBudgetAdapter = new RvListBudgetAdapter(getContext(), mBudgetList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBudgetRv.setLayoutManager(layoutManager);
        mBudgetRv.setHasFixedSize(true);
        mBudgetRv.setAdapter(mRvListBudgetAdapter);
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
                AddBudgetActivity.start(getContext());
            }
        });

        // 长按RVlist按删除
        mRvListBudgetAdapter.setOnItemLongClickListener(new RvListBudgetAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                deleteBudget(position);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        mUserId = SpManager.get().getUserAccount();
    }

    public void refreshBudget() {
        mBudgetList.clear();
        Cursor cs;// 游标对象，用来报错查询返回的结果集
        String userId = String.valueOf(mUserId);
        BudgetBean budgetBean;
        String btime = null, bmoney = null;
        Boolean cid = false;
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

//                Cursor csa;
//                float bsum = 0;
//                csa = mDb.rawQuery(
//                        "select sum(aomoney) from expenditure where aotime=? and aouserid=?",
//                        new String[]{btime + "", userId + ""});
//
//                if (csa != null) {
//                    if (cs.moveToFirst()) {
//                        do {
//                            bsum = cs.getFloat(0);
//                        } while (cs.moveToNext());
//                    }
//                    float fmoney = Float.parseFloat(bmoney);
//                    if (bsum > fmoney) {
//                        cid = true;
//                    }
//                }

//                for (int i = 0; i < mGrDataList.size(); i++) {
//                    mBudgetList.get(i).setIsSelected(i == bid);
//                }
                budgetBean = new BudgetBean(bmoney, bremarks, btime, bid, buserid,
                        cid);
                mBudgetList.add(budgetBean);
            }
        }
        mRvListBudgetAdapter.notifyDataSetChanged();
    }

    public void deleteBudget(int position) {
        // 获取所点击项的id
        int id = mBudgetList.get(position).getmId();
        // 通过Dialog提示是否删除
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getContext());
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
                            Toast.makeText(getContext(),
                                    R
                                            .string.delete_yes+ num, Toast.LENGTH_SHORT)
                                    .show();
                            // TODO: 2020/07/24 string文件错乱
//                            Toast.makeText(getContext(),
//                                    R.string.delete_text+ num, Toast.LENGTH_SHORT)
//                                    .show();
                            // 删掉长按的item
                            mBudgetList.remove(position);
                            // 动态更新listview
                            mRvListBudgetAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(),
                                    R
                                            .string.delete_no + num, Toast.LENGTH_SHORT)
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
    }
}
