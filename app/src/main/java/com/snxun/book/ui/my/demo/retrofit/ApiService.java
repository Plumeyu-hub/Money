package com.snxun.book.ui.my.demo.retrofit;

import com.snxun.book.bean.base.ResponseBean;
import com.snxun.book.bean.budget.BudgetBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
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

    /** get方式获取景点数据 */
    @GET("spot")
    Observable<ResponseBean<BudgetBean>> getSpot(@Query("id") String id, @Query("output") String output);

    /** post方式获取景点数据 */
    @FormUrlEncoded
    @POST("spot")
    Observable<ResponseBean<BudgetBean>> postSpot(@Field("id") String id, @Field("output") String output);

    /** 自定义方式获取景点数据 */
    @POST("spot")
    Observable<ResponseBean<List<BudgetBean>>> querySpot(@Body RequestBody requestBody);

}
