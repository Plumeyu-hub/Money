package com.snxun.book.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodz.android.core.utils.ToastUtils;
import com.lodz.android.core.utils.UiHandler;
import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.db.DbFactory;
import com.snxun.book.greendaolib.table.UserTable;
import com.snxun.book.ui.money.main.MainActivity;
import com.snxun.book.utils.sp.SpManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    /**
     * 用户名输入框
     */
    @BindView(R.id.login_username_edit)
    EditText mLoginUsernameEdit;
    /**
     * 密码输入框
     */
    @BindView(R.id.login_password_edit)
    EditText mLoginPasswordEdit;
    /**
     * 忘记密码按钮
     */
    @BindView(R.id.login_forget_password_btn)
    TextView mLoginForgetPasswordBtn;
    /**
     * 登录按钮
     */
    @BindView(R.id.login_btn)
    ImageView mLoginBtn;
    /**
     * 注册按钮
     */
    @BindView(R.id.login_registered_btn)
    ImageView mLoginRegisteredBtn;
    /**
     * 登录进度条
     */
    private ProgressDialog mLoginDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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

        //登录按钮
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = mLoginUsernameEdit.getText().toString().trim();
                String password = mLoginPasswordEdit.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    ToastUtils.showShort(getContext(), R.string.registered_username_hint);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showShort(getContext(), R.string.registered_pswd_hint);
                    return;
                }

                login(account, password);
            }
        });

        //注册按钮
        mLoginRegisteredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RegisteredActivity.class));
            }
        });

        //忘记密码按钮
        mLoginForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RetrievePasswordActivity.class));
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        autoLogin();
    }

    /** 自动登录 */
    private void autoLogin() {
        // 判断登录状态
        if (!SpManager.get().getUserStatus()) {
            return;
        }
        if (mLoginDialog == null) {
            mLoginDialog = new ProgressDialog(this);// 创建了圆形进度条对话框
            mLoginDialog.setMessage("登录中。。。");
            mLoginDialog.setCancelable(false);// 设置点击返回按钮无效
        }
        mLoginDialog.show();

        UiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoginDialog.dismiss();
                startActivity(new Intent(getContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }

    /**
     * 登录
     * @param account 账号
     * @param password 密码
     */
    public void login(String account, String password) {
        UserTable user = DbFactory.create().getUserInfo(account);
        if (user == null){
            ToastUtils.showShort(getContext(), R.string.retrieve_password_unregistered);
            return;
        }
        if (!password.equals(user.getPswd())){
            ToastUtils.showShort(getContext(), R.string.login_pswd_incorrect);
            return;
        }
        ToastUtils.showShort(getContext(), R.string.login_success);
        //存储用户的登录成功的状态
        SpManager.get().setUserStatus(true);
        //存储用户的登录Id
        SpManager.get().setUserAccount(account);
        MainActivity.start(getContext());
        finish();
    }
}