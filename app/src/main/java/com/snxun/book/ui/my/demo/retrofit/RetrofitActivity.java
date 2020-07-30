package com.snxun.book.ui.my.demo.retrofit;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lodz.android.component.rx.subscribe.observer.ProgressObserver;
import com.lodz.android.component.rx.utils.RxUtils;
import com.lodz.android.core.utils.ArrayUtils;
import com.snxun.book.R;
import com.snxun.book.api.ApiServiceImpl;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.bean.base.ResponseBean;
import com.snxun.book.bean.budget.BudgetBean;
import com.snxun.book.bean.budget.BudgetRequestBean;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Retrofit演示
 * @author zhouL
 * @date 2020/7/30
 */
public class RetrofitActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, RetrofitActivity.class);
        context.startActivity(starter);
    }

    /** 返回结果 */
    @BindView(R.id.result_tv)
    TextView mResultTv;
    /** post请求 */
    @BindView(R.id.post_btn)
    Button mPostBtn;
    /** get请求 */
    @BindView(R.id.get_btn)
    Button mGetBtn;
    /** RequestBody请求 */
    @BindView(R.id.request_body_btn)
    Button mRequestBodyBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResult();
                requestPost();
            }
        });

        mGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResult();
                requestGet();
            }
        });

        mRequestBodyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResult();
                requestCustom();
            }
        });
    }

    private void requestPost() {
        ApiServiceImpl.get()// 本地数据
//        ApiServiceManager.get().create(ApiService.class)// 网络数据
                .postBudget("")
                .compose(RxUtils.ioToMainObservable())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
//                .compose(this.<ResponseBean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new ProgressObserver<ResponseBean<BudgetBean>>() {
                    @Override
                    public void onPgNext(ResponseBean<BudgetBean> responseBean) {
                        BudgetBean bean = responseBean.data;
                        mResultTv.setText(String.valueOf(bean.getmMoney() + "\n" + bean.getmRemarks()));
                    }

                    @Override
                    public void onPgError(Throwable e, boolean isNetwork) {
                        mResultTv.setText("查询失败");
                    }

                    @Override
                    public void onPgCancel() {
                        super.onPgCancel();
                        mResultTv.setText("取消请求");
                    }
                }.create(getContext(), "正在查询", true));
    }

    private void requestGet() {
        ApiServiceImpl.get()
//        ApiServiceManager.get().create(ApiService.class)// 网络数据
                .getBudget("")
                .map(new Function<ResponseBean<BudgetBean>, String>() {
                    @Override
                    public String apply(ResponseBean<BudgetBean> responseBean) throws Exception {
                        if (responseBean.isSuccess()){
                            BudgetBean bean = responseBean.data;
                            return bean.getmMoney() + "\n" + bean.getmRemarks() + "\n" + "map Transform";
                        }
                        throw new IllegalArgumentException();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new ProgressObserver<String>() {
                    @Override
                    public void onPgNext(String value) {
                        mResultTv.setText(value);
                    }

                    @Override
                    public void onPgError(Throwable e, boolean isNetwork) {
                        mResultTv.setText("查询失败");
                    }
                }.create(getContext(), "正在查询", false));
    }

    private void requestCustom() {
        int num = new Random().nextInt(8) + 1;
        ApiServiceImpl.get()// 本地数据
//        ApiServiceManager.get().create(ApiService.class)// 网络数据
                .queryBudgetList(new BudgetRequestBean("", num).createRequestBody())
                .compose(RxUtils.ioToMainObservable())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new ProgressObserver<ResponseBean<List<BudgetBean>>>() {
                    @Override
                    public void onPgNext(ResponseBean<List<BudgetBean>> responseBean) {
                        List<BudgetBean> list = responseBean.data;
                        if (ArrayUtils.isEmpty(list)){
                            mResultTv.setText("list is null");
                            return;
                        }
                        StringBuilder result = new StringBuilder();
                        for (BudgetBean bean : list) {
                            result.append(bean.getmMoney()).append("\n").append(bean.getmRemarks()).append("\n\n");
                        }
                        mResultTv.setText(result.toString());
                    }

                    @Override
                    public void onPgError(Throwable e, boolean isNetwork) {
                        mResultTv.setText("查询失败");
                    }
                }.create(getContext(), "正在查询", false));
    }

    /** 清空结果 */
    private void clearResult(){
        mResultTv.setText("");
    }
}
