package com.snxun.book.api;

import com.snxun.book.bean.base.ResponseBean;
import com.snxun.book.bean.budget.BudgetBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 接口
 * Created by zhouL on 2018/2/6.
 */

public interface ApiService {

    /** get方式获取预算信息 */
    @GET("queryBudget")
    Observable<ResponseBean<BudgetBean>> getBudget(@Query("date") String date);

    /** post方式获取预算信息 */
    @FormUrlEncoded
    @POST("queryBudget")
    Observable<ResponseBean<BudgetBean>> postBudget(@Field("date") String date);

    /** 自定义方式获取预算列表 */
    @POST("queryBudget")
    Observable<ResponseBean<List<BudgetBean>>> queryBudgetList(@Body RequestBody requestBody);

}
