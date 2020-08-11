package com.snxun.book.ui.my;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

import com.lodz.android.core.utils.ToastUtils;
import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.db.DbFactory;
import com.snxun.book.event.RefreshEvent;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;

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
    com.snxun.book.widget.ContentLayout mClearBtn;

    /**
     * 当前登录的用户ID
     */
    private String mAccount;

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
        showUserInfo();
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        mAccount = SpManager.get().getUserAccount();
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
                boolean isSaveSuccess = DbFactory.create().deleteAllBillInfo(mAccount);
                if (!isSaveSuccess) {
                    ToastUtils.showShort(getContext(), R.string.delete_no);
                    return;
                }
                ToastUtils.showShort(getContext(), R.string.delete_yes);
                boolean refresh = true;
                EventBus.getDefault().post(new RefreshEvent(refresh));
            }

        });
        builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // System.out.println("2222");
            }
        });
        // [3]展示对话框和toast一样 一定要记得show出来
        builder.show();

    }
}