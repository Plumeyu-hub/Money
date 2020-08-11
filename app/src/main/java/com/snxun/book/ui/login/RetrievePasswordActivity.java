package com.snxun.book.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.lodz.android.core.utils.ToastUtils;
import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.db.DbFactory;
import com.snxun.book.greendaolib.table.UserTable;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 找回密码
 */
public class RetrievePasswordActivity extends BaseActivity {

	/**
	 * 用户名
	 */
	@BindView(R.id.retrieve_password_username_edit)
	EditText mRetrievePasswordUsernameEdit;
	/**
	 * 密保问题
	 */
	@BindView(R.id.retrieve_password_problem_edit)
	EditText mRetrievePasswordProblemEdit;
	/**
	 * 密保答案
	 */
	@BindView(R.id.retrieve_password_answer_edit)
	EditText mRetrievePasswordAnswerEdit;
	/**
	 * 确定按钮
	 */
	@BindView(R.id.retrieve_password_btn)
	TextView mRetrievePasswordBtn;
	/**
	 * 返回密码的tv
	 */
	@BindView(R.id.retrieve_password_tv)
	TextView mRetrievePasswordTv;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_retrieve_password;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		initTitleLayout();
	}

	private void initTitleLayout() {
		showTitleBar();
		getTitleLayout().setTitleName(R.string.retrieve_password_title);
	}

	@Override
	protected void clickBackBtn() {
		super.clickBackBtn();
		finish();
	}

	/**
	 * 设置监听
	 */
	@Override
	protected void setListeners() {
		super.setListeners();

		//找回密码
		mRetrievePasswordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String account = mRetrievePasswordUsernameEdit.getText().toString().trim();
				String question = mRetrievePasswordProblemEdit.getText().toString().trim();
				String answer = mRetrievePasswordAnswerEdit.getText().toString().trim();
				if (TextUtils.isEmpty(account)) {
					ToastUtils.showShort(getContext(), R.string.registered_username_hint);
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
				retrievePswd(account, question, answer);
			}
		});
	}

	/**
	 * 找回妈咪
	 * @param account 账号
	 * @param question 密保问题
	 * @param answer 密保答案
	 */
	private void retrievePswd(String account, String question, String answer) {
		UserTable user = DbFactory.create().getUserInfo(account);
		if (user == null){
			ToastUtils.showShort(getContext(), R.string.retrieve_password_unregistered);
			return;
		}
		if (!question.equals(user.getPswdQuestion())){
			ToastUtils.showShort(getContext(), R.string.retrieve_password_question_error);
			return;
		}
		if (!answer.equals(user.getPswdAnswer())){
			ToastUtils.showShort(getContext(), R.string.retrieve_password_answer_error);
			return;
		}
		ToastUtils.showShort(getContext(), R.string.retrieve_password_success);
		mRetrievePasswordTv.setText(getString(R.string.retrieve_password_display, user.getPswd()));
	}
}