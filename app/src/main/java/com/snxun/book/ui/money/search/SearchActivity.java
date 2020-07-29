package com.snxun.book.ui.money.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.db.DbFactory;
import com.snxun.book.event.SearchUpdateEvent;
import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.ui.money.adapter.RvListAdapter;
import com.snxun.book.ui.money.update.UpdateActivity;
import com.snxun.book.utils.ToastUtils;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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
     * 搜索框
     */
    @BindView(R.id.search_text_edit)
    EditText mSearchTextEdit;

    /**
     * RV列表
     */
    @BindView(R.id.search_rv)
    RecyclerView mRecyclerView;
    private RvListAdapter mRvListAdapter;
    /**
     * 定义了一个数组List，里面只能存放BillTable
     */
    private List<BillTable> mSearchList;
    /**
     * 当前登录的用户ID
     */
    private String mAccount;
    /**
     * 当前点击跳转至Update的列表Id
     */
    private int mPosition;

    /**
     * 注册EventBus
     */
    //    @Override
    //    protected void startCreate() {
    //        super.startCreate();
    //        EventBus.getDefault().register(this);
    //    }
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
     * 设置Rv列表
     */
    private void initRecyclerView() {
        // 初始化数据列表
        mSearchList = new ArrayList<>();
        mRvListAdapter = new RvListAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRvListAdapter);
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
                    mSearchList.clear();
                    // 设置空列表的时候，显示为一张图片
                    //mSearchLv.setEmptyView(findViewById(R.id.search_empty_lin));
                } else {
                    // 展示关联的数据
                    refreshList(searchText);
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
        mRvListAdapter.setOnItemClickListener(new RvListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                BillTable billTable = mSearchList.get(position);
                //EventBus的黏性postSticky
                EventBus.getDefault().postSticky(new SearchUpdateEvent(billTable));
                UpdateActivity.start(getContext());
                mPosition = position;
            }
        });


        // 长按RVlist按删除
        mRvListAdapter.setOnItemLongClickListener(new RvListAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                deleteListItem(position);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showUserInfo();
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        mAccount = SpManager.get().getUserAccount();
    }

    public void refreshList(String searchText) {
        mSearchList = DbFactory.create().getSearchBillInfo(mAccount, searchText);
        mRvListAdapter.setData(mSearchList);
        mRvListAdapter.notifyDataSetChanged();
        // 设置空列表的时候，显示为一张图片
        //mDetailsList.setEmptyView(getActivity().findViewById(R.id.details_empty_lin));
    }

    /**
     * 删除账单
     */
    public void deleteListItem(int position) {
        // 获取所点击项的id
        Long id = mSearchList.get(position).getId();
        //创建dialog
        showDeleteTipsDialog(id);
    }

    /**
     * 显示删除提示弹框
     *
     * @param id 数据库编号
     */
    private void showDeleteTipsDialog(Long id) {
        // 通过Dialog提示是否删除
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_text);
        // 确定按钮点击事件
        builder.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除指定数据
                boolean isSaveSuccess = DbFactory.create().deleteBillInfo(id);
                if (!isSaveSuccess) {
                    ToastUtils.showShort(getContext(), R.string.delete_no);
                    return;
                }
                ToastUtils.showShort(getContext(), R.string.delete_yes);
                String searchText = mSearchTextEdit.getText().toString().trim();
                refreshList(searchText);
            }
        });

        // 取消按钮点击事件
        builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onUpdateSearchEvent(UpdateSearchEvent event) {
    //        dataBean = event.getDataBean();
    //        //更改修改过的List
    //        mSearchList.set(mPosition, dataBean);
    //        mRvListAdapter.notifyDataSetChanged();
    //    }

    //    /**
    //     * 解注册EventBus
    //     */
    //    @Override
    //    public void finish() {
    //        super.finish();
    //        EventBus.getDefault().unregister(this);
    //    }
}