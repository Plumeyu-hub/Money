package com.snxun.book.ui.my;

import android.view.View;
import android.widget.ImageView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {
	/**
	 * 返回按钮
	 */
	@BindView(R.id.message_back_btn)
	ImageView mMessageBackBtn;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message;
	}


	@Override
	protected void findViews() {
		ButterKnife.bind(this);
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		mMessageBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

}
