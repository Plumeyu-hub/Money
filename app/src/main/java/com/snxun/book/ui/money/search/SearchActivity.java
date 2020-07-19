package com.snxun.book.ui.money.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.money.adapter.ListAdapter;
import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.ui.money.details.DetailsFragment;
import com.snxun.book.ui.money.update.UpdateActivity;
import com.snxun.book.utils.sp.SpManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    /**
     * 返回按钮
     */
    @BindView(R.id.search_back_btn)
    ImageView mSearchBackBtn;

    /**
     * 清除搜索框
     */
    @BindView(R.id.search_delete_btn)
    ImageView mSearchDeleteBtn;

    /**
     * 搜索按钮
     */
    @BindView(R.id.sreach_btn)
    TextView mSreachBtn;
    /**
     * 列表 第一步:listview控件实例化
     */
    @BindView(R.id.search_lv)
    ListView mSearchLv;
    /**
     * 搜索框
     */
    @BindView(R.id.search_text_edit)
    EditText mSearchTextEdit;

    private List<DataBean> mList;
    private ListAdapter mListAdapter;

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    private Cursor mCursor;// 游标对象，用来报错查询返回的结果集
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
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
        mSearchBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //查询数据
        mSearchBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchTextEdit.getText().toString().trim();
                if (TextUtils.isEmpty(searchText)) {
                    mList.clear();
                    // 设置空列表的时候，显示为一张图片
                    mSearchLv.setEmptyView(findViewById(R.id.search_empty_lin));
                } else {
                    // 展示关联的数据
                    setSearchData(1, searchText);
                }
            }
        });

        //清除输入框数据
        mSearchDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchTextEdit.setText("");
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
        showList();
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
        mUserId = SpManager.get().getUserId();
    }


    private void showList() {
        // 第二步:初始化数据
        mList = new ArrayList<>();
        // 实例化适配器
        mListAdapter = new ListAdapter(this, mList);
        // listview设置适配器
        mSearchLv.setAdapter(mListAdapter);
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                DataBean showdata = mList.get(position);
                UpdateActivity.startForResult(SearchActivity.this, showdata, position, DetailsFragment.DETAIL_UPDATE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null) {
            if (resultCode == 102) {
                DataBean dataBean = (DataBean) intent
                        .getSerializableExtra("updata");
                int position = intent.getIntExtra("position", 0);
                mList.set(position, dataBean);
                mListAdapter.notifyDataSetChanged();
            }
        }
    }


    public void setSearchData(int n, String str) {
        if (n == 1) {
            mList.clear();
            DataBean dataBean;
            String userId = String.valueOf(mUserId);
            mCursor = mDb.query(
                    "expenditure",
                    new String[]{"aoid", "aocategory", "aomoney", "aotime",
                            "aoaccount", "aoremarks", "aouserid"},
                    "aocategory like ? or aoaccount like ? or aoremarks like ? or aotime like ? or aomoney like ? and aouserid=?",
                    new String[]{"%" + str + "%", "%" + str + "%",
                            "%" + str + "%", "%" + str + "%", "%" + str + "%",
                            userId + ""}, null, null, null);
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    int aoid = mCursor.getInt(mCursor.getColumnIndex("aoid"));// 得到列名id对于的值
                    String aocategory = mCursor.getString(mCursor
                            .getColumnIndex("aocategory"));
                    String aomoney = "-"
                            + mCursor.getString(mCursor.getColumnIndex("aomoney"));
                    String aotime = mCursor.getString(mCursor.getColumnIndex("aotime"));
                    String aoaccount = mCursor.getString(mCursor
                            .getColumnIndex("aoaccount"));
                    String aoremarks = mCursor.getString(mCursor
                            .getColumnIndex("aoremarks"));
                    dataBean = new DataBean(aocategory, aomoney, aoaccount,
                            aoremarks, aotime, aoid, mUserId);
                    mList.add(dataBean);
                }
                mCursor = mDb.query(
                        "income",
                        new String[]{"aiid", "aicategory", "aimoney",
                                "aitime", "aiaccount", "airemarks", "aiuserid"},
                        "aicategory like ? or aiaccount like ? or airemarks like ? or aitime like ? or aimoney like ? and aiuserid=?",
                        new String[]{"%" + str + "%", "%" + str + "%",
                                "%" + str + "%", "%" + str + "%",
                                "%" + str + "%", userId + ""}, null, null, null);
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        int aiid = mCursor.getInt(mCursor.getColumnIndex("aiid"));// 得到列名id对于的值
                        String aicategory = mCursor.getString(mCursor
                                .getColumnIndex("aicategory"));
                        String aimoney = "+"
                                + mCursor.getString(mCursor.getColumnIndex("aimoney"));
                        String aitime = mCursor.getString(mCursor
                                .getColumnIndex("aitime"));
                        String aiaccount = mCursor.getString(mCursor
                                .getColumnIndex("aiaccount"));
                        String airemarks = mCursor.getString(mCursor
                                .getColumnIndex("airemarks"));
                        dataBean = new DataBean(aicategory, aimoney, aiaccount,
                                airemarks, aitime, aiid, mUserId);
                        mList.add(dataBean);
                    }
                }
            }
            Collections.sort(mList, new Comparator<DataBean>() {

                @Override
                public int compare(DataBean lhs, DataBean rhs) {
                    // TODO Auto-generated method stub
                    int a = Integer.parseInt(lhs.getmDate());
                    int b = Integer.parseInt(rhs.getmDate());
                    int diff = a - b;
                    if (diff > 0) {
                        return 1;
                    } else if (diff < 0) {
                        return -1;
                    }
                    return 0;// 相等为0
                }
            });
            mListAdapter.notifyDataSetChanged();
            // 设置空列表的时候，显示为一张图片
            mSearchLv.setEmptyView(findViewById(R.id.search_empty_lin));
        }
    }
}
