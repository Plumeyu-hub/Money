package com.snxun.book.ui.money.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.SearchUpdateEvent;
import com.snxun.book.event.UpdateSearchEvent;
import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.ui.money.details.DetailsRvAdapter;
import com.snxun.book.ui.money.update.UpdateActivity;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {
    /**
     *数据源
     */
    private DataBean dataBean;
    /**
     * 当前点击跳转至Update的列表Id
     */
    private int mPosition;
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
     * 搜索框
     */
    @BindView(R.id.search_text_edit)
    EditText mSearchTextEdit;

    /**
     * RV列表
     */
    @BindView(R.id.search_rv)
    RecyclerView mRecyclerView;
    private DetailsRvAdapter mDetailsRvAdapter;
    /**
     * 定义了一个数组List，里面只能存放DataBean
     */
    private List<DataBean> mDetailsList;

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    private Cursor mCursor;// 游标对象，用来报错查询返回的结果集
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    /**
     * 注册EventBus
     */
    @Override
    protected void startCreate() {
        super.startCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initRecyclerView();
    }

    /**
     *设置Rv列表
     */
    private void initRecyclerView() {
        // 初始化数据列表
        mDetailsList = new ArrayList<>();
        mDetailsRvAdapter = new DetailsRvAdapter(getContext(), mDetailsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mDetailsRvAdapter);
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
        mSreachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchTextEdit.getText().toString().trim();
                if (TextUtils.isEmpty(searchText)) {
                    mDetailsList.clear();
                    // 设置空列表的时候，显示为一张图片
                    //mSearchLv.setEmptyView(findViewById(R.id.search_empty_lin));
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

        //列表点击事件
        mDetailsRvAdapter.setOnItemClickListener(new DetailsRvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                DataBean dataBean = mDetailsList.get(position);
                //EventBus的黏性postSticky
                EventBus.getDefault().postSticky(new SearchUpdateEvent(dataBean));
                UpdateActivity.start(getContext());
                mPosition = position;
            }
        });

        // 长按RVlist按删除
        mDetailsRvAdapter.setOnItemLongClickListener(new DetailsRvAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                // 获取所点击项的id
                int intId = mDetailsList.get(position).getmId();
                String stringId =String.valueOf(intId);
                // 获取所点击项的金额符号
                String stringMoney=mDetailsList.get(position).getmMoney();
                String symbolMoney=stringMoney.substring(0, 1);

                // 通过Dialog提示是否删除
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getContext());
                builder.setMessage("确定要删除吗？");
                // 确定按钮点击事件
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String userId = String.valueOf(mUserId);
                                // 删除指定数据
                                int num;
                                if (symbolMoney.equals("-")) {
                                    // 删除数据，成功返回删除的数据的行数，失败返回0
                                    num = mDb.delete("expenditure",
                                            "aoid=? and aouserid=?",
                                            new String[] { stringId + "", userId + "" });
                                    if (num > 0) {
                                        Toast.makeText(getContext(),
                                                "删除成功" + num,
                                                Toast.LENGTH_SHORT).show();
                                        // 删掉长按的item
                                        mDetailsList.remove(position);
                                        // 动态更新listview
                                        mDetailsRvAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "删除失败" + num,
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else if (symbolMoney.equals("+")) {
                                    // 删除数据，成功返回删除的数据的行数，失败返回0
                                    num = mDb.delete("income",
                                            "aiid=? and aiuserid=?",
                                            new String[] { stringId + "", userId + "" });
                                    if (num > 0) {
                                        Toast.makeText(getContext(),
                                                "删除成功" + num,
                                                Toast.LENGTH_SHORT).show();
                                        // 删掉长按的item
                                        mDetailsList.remove(position);
                                        // 动态更新listview
                                        mDetailsRvAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "删除失败" + num,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(),
                                            "删除失败", Toast.LENGTH_SHORT).show();
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
        mUserId = SpManager.get().getUserId();
    }

    public void setSearchData(int n, String str) {
        if (n == 1) {
            mDetailsList.clear();
            DataBean dataBean;
            String userId = String.valueOf(mUserId);
            mCursor = mDb.query(
                    "expenditure",
                    new String[]{"aoid", "aocategory", " aomoney", "aotime",
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
                    mDetailsList.add(dataBean);
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
                        mDetailsList.add(dataBean);
                    }
                }
            }
            Collections.sort(mDetailsList, new Comparator<DataBean>() {
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
            mDetailsRvAdapter.notifyDataSetChanged();
            // 设置空列表的时候，显示为一张图片
            //mSearchLv.setEmptyView(findViewById(R.id.search_empty_lin));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSearchEvent(UpdateSearchEvent event) {
        dataBean = event.getDataBean();
        //更改修改过的List
        mDetailsList.set(mPosition, dataBean);
        mDetailsRvAdapter.notifyDataSetChanged();
    }

    /**
     * 解注册EventBus
     */
    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().unregister(this);
    }


}
