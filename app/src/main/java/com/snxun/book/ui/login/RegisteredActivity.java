package com.snxun.book.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lodz.android.core.utils.ToastUtils;
import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.db.DbFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisteredActivity extends BaseActivity {

	/**
	 * 返回按钮
	 */
	@BindView(R.id.registered_back_btn)
	ImageView mRegisteredBackBtn;
	/**
	 * 用户名
	 */
	@BindView(R.id.registered_username_edit)
	EditText mRegisteredUsernameEdit;
	/**
	 *密码
	 */
	@BindView(R.id.registered_password_edit)
	EditText mRegisteredPasswordEdit;
	/**
	 *确认密码
	 */
	@BindView(R.id.registered_password_two_edit)
	EditText mRegisteredPasswordTwoEdit;
	/**
	 *密保问题
	 */
	@BindView(R.id.registered_problem_edit)
	EditText mRegisteredProblemEdit;
	/**
	 *密保答案
	 */
	@BindView(R.id.registered_answer_edit)
	EditText mRegisteredAnswerEdit;
	/**
	 *注册按钮
	 */
	@BindView(R.id.registered_btn)
	ImageView mRegisteredBtn;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registered;
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
		mRegisteredBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		mRegisteredBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String account = mRegisteredUsernameEdit.getText().toString().trim();
				String pswd = mRegisteredPasswordEdit.getText().toString().trim();
				String pswdConfirm = mRegisteredPasswordTwoEdit.getText().toString().trim();
				String question = mRegisteredProblemEdit.getText().toString().trim();
				String answer = mRegisteredAnswerEdit.getText().toString().trim();
				if (TextUtils.isEmpty(account)) {
					ToastUtils.showShort(getContext(), R.string.registered_username_hint);
					return;
				}
				if (TextUtils.isEmpty(pswd)) {
					ToastUtils.showShort(getContext(), R.string.registered_pswd_hint);
					return;
				}
				if (TextUtils.isEmpty(pswdConfirm)) {
					ToastUtils.showShort(getContext(), R.string.registered_pswd_confirm_hint);
					return;
				}
				if (!pswd.equals(pswdConfirm)){
					ToastUtils.showShort(getContext(), R.string.registered_pswd_inconsistent);
					return;
				}
				if (TextUtils.isEmpty(question)) {
					ToastUtils.showShort(getContext(), R.string.registered_problem_hint);
					return;
				}
				if (TextUtils.isEmpty(answer)) {
					ToastUtils.showShort(getContext(), R.string.registered_answer_hint);
					return;
				}
				registered(account, pswd, question, answer);
			}
		});
	}

	/**
	 * 注册账号
	 * @param account 账号
	 * @param pswd 密码
	 * @param question 密保问题
	 * @param answer 密保答案
	 */
	private void registered(String account, String pswd, String question, String answer) {
		boolean isSaveSuccess = DbFactory.create().saveUserInfo(account, pswd, question, answer);
		if (!isSaveSuccess){
			ToastUtils.showShort(getContext(), R.string.registered_account_repeated);
			return;
		}
		ToastUtils.showShort(getContext(), R.string.registered_success);
		finish();
	}

}