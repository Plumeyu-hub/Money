package com.snxun.book.ui.my;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalInfoActivity extends BaseActivity {

	/**
	 * 返回按钮
	 */
	@BindView(R.id.persoal_info_back_btn)
	ImageView mPersoalInfoBackBtn;
	/**
	 * 修改密码
	 */
	@BindView(R.id.modify_pw_btn)
	ImageView mModifyPwBtn;
	/**
	 * 退出账户
	 */
	@BindView(R.id.exit_user_btn)
	ImageView mExitUserBtn;

	/**
	 * sp
	 */
	private SharedPreferences.Editor editorLogin, editorUser;
	private Boolean boolLogin;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_personal_info;
	}


	@Override
	protected void findViews() {
		ButterKnife.bind(this);
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		mPersoalInfoBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		mModifyPwBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(PersonalInfoActivity.this, ModifyPasswordActivity.class));
			}
		});
		mExitUserBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNormalDialog();
			}
		});
	}

	@Override
	protected void initData() {
		super.initData();

		SharedPreferences sp_login = this.getSharedPreferences("userLogin", MODE_PRIVATE);
		editorLogin = sp_login.edit();
		editorLogin.apply();

		SharedPreferences sp_user = this.getSharedPreferences("user", MODE_PRIVATE);
		editorUser = sp_user.edit();
		editorUser.apply();
	}

	// 弹出一个普通对话框
	private void showNormalDialog() {
		// [1]构造对话框的实例
		Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("是否退出此账号？");
		// [2]设置确定和取消按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				boolLogin = false;
				editorLogin.putBoolean("isLoad", boolLogin);
				editorLogin.commit();

				editorUser.clear();
				editorUser.commit();

				startActivity(new Intent(PersonalInfoActivity.this, LoginActivity.class));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		// [3]展示对话框 和toast一样 一定要记得show出来
		builder.show();

	}
}