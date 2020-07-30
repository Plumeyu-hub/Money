package com.snxun.book.ui.my.demo.retrofit;

import com.snxun.book.bean.base.ResponseBean;
import com.snxun.book.bean.budget.BudgetBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;

/**
 * 接口
 * Created by zhouL on 2018/2/6.
 */

public class ApiServiceImpl implements ApiService{


    @Override
    public Observable<ResponseBean<BudgetBean>> getSpot(String id, String output) {
        return null;
    }

    @Override
    public Observable<ResponseBean<BudgetBean>> postSpot(String id, String output) {
        return null;
    }

    @Override
    public Observable<ResponseBean<List<BudgetBean>>> querySpot(RequestBody requestBody) {
        return null;
    }
}
