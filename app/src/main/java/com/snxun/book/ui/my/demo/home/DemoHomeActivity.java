package com.snxun.book.ui.my.demo.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.my.demo.event.EventActivity;
import com.snxun.book.ui.my.demo.gr.GrActivity;
import com.snxun.book.ui.my.demo.retrofit.RetrofitActivity;
import com.snxun.book.ui.my.demo.rv.RvActivity;
import com.snxun.book.ui.my.demo.tab.TabTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 样例主页
 * @author zhouL
 * @date 2020/7/9
 */
public class DemoHomeActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, DemoHomeActivity.class);
        context.startActivity(starter);
    }

    /** tablayout样例按钮 */
    @BindView(R.id.tab_case_btn)
    Button mTabCaseBtn;
    /** RecyclerView样例按钮 */
    @BindView(R.id.rv_case_btn)
    Button mRvCaseBtn;
    /** RV的GR样例按钮 */
    @BindView(R.id.gr_case_btn)
    Button mGrCaseBtn;
    /** Eventbus样例按钮 */
    @BindView(R.id.eventbus_case_btn)
    Button mEventbusCaseBtn;
    /** Retrofit样例按钮 */
    @BindView(R.id.retrofit_btn)
    Button mRetrofitBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_home;
    }

    @Override
    protected void findViews(){
        ButterKnife.bind(this);
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.demo_home_title);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mTabCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabTestActivity.start(getContext());
            }
        });

        mRvCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RvActivity.start(getContext());
            }
        });

        mGrCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GrActivity.start(getContext());
            }
        });

        mEventbusCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventActivity.start(getContext());
            }
        });

        mRetrofitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitActivity.start(getContext());
            }
        });
    }

}
