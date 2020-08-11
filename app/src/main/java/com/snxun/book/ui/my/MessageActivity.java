package com.snxun.book.ui.my;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		initTitleLayout();
	}

	private void initTitleLayout() {
		showTitleBar();
		getTitleLayout().setTitleName(R.string.drawer_mes);
	}

	@Override
	protected void clickBackBtn() {
		super.clickBackBtn();
		finish();
	}

}
