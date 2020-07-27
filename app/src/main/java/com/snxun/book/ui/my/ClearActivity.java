package com.snxun.book.ui.my;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.utils.sp.SpManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClearActivity extends BaseActivity {
    /**
     * 返回按钮
     */
    @BindView(R.id.clear_back_btn)
    ImageView mClearBackBtn;
    /**
     * 数据清除按钮
     */
    @BindView(R.id.clear_btn)
    ImageView mClearBtn;

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    /**
     * 当前登录的用户ID
     */
    private String mUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clear;
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
        mClearBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //数据清除
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showClearDialog();
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


    /**
     * 弹出一个普通对话框
     */
    private void showClearDialog() {
        // [1]构造对话框的实例
        Builder builder = new Builder(this);
        builder.setTitle(R.string.app_tip);
        builder.setMessage(R.string.clear_text_dialog);
        // [2]设置确定和取消按钮
        builder.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除指定数据
                int outNum, inNum;
                String userid = String.valueOf(mUserId);
                // 删除数据，成功返回删除的数据的行数，失败返回0
                outNum = mDb.delete("expenditure", "aouserid=?",
                        new String[]{userid + ""});
                inNum = mDb.delete("income", "aiuserid=?", new String[]{userid
                        + ""});
                if ((outNum > 0) && (inNum > 0)) {
                    Toast.makeText(ClearActivity.this, R.string.delete_yes,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ClearActivity.this, R.string.delete_no,
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // System.out.println("2222");
            }
        });
        // [3]展示对话框 和toast一样 一定要记得show出来
        builder.show();

    }
}