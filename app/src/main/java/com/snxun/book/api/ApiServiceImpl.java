package com.snxun.book.api;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lodz.android.core.utils.ReflectUtils;
import com.snxun.book.bean.base.ResponseBean;
import com.snxun.book.bean.budget.BudgetBean;
import com.snxun.book.bean.budget.BudgetRequestBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.RequestBody;

/**
 * 接口实现类
 * Created by zhouL on 2018/2/6.
 */

public class ApiServiceImpl implements ApiService{

    public static ApiServiceImpl get(){
        return new ApiServiceImpl();
    }

    private ApiServiceImpl() {}

    @Override
    public Observable<ResponseBean<BudgetBean>> getBudget(String date) {
        return Observable.create(new ObservableOnSubscribe<ResponseBean<BudgetBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ResponseBean<BudgetBean>> emitter) throws Exception {
                try {
                    Thread.sleep(2000);
                    ResponseBean<BudgetBean> responseBean = new ResponseBean<>();
                    responseBean.code = ResponseBean.SUCCESS;
                    responseBean.msg = "请求成功";
                    responseBean.data = new BudgetBean("5000", "测试数据1", "20200730", 10, 12,  false);
                    if (emitter.isDisposed()){
                        return;
                    }
                    emitter.onNext(responseBean);
                    emitter.onComplete();
                }catch (Exception e){
                    e.printStackTrace();
                    if (emitter.isDisposed()){
                        return;
                    }
                    emitter.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ResponseBean<BudgetBean>> postBudget(String date) {
        return Observable.create(new ObservableOnSubscribe<ResponseBean<BudgetBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ResponseBean<BudgetBean>> emitter) throws Exception {
                try {
                    Thread.sleep(1500);
                    ResponseBean<BudgetBean> responseBean = new ResponseBean<>();
                    responseBean.code = ResponseBean.SUCCESS;
                    responseBean.msg = "请求成功";
                    responseBean.data = new BudgetBean("2000", "测试数据2", "20200830", 15, 21,  true);
                    if (emitter.isDisposed()){
                        return;
                    }
                    emitter.onNext(responseBean);
                    emitter.onComplete();
                }catch (Exception e){
                    e.printStackTrace();
                    if (emitter.isDisposed()){
                        return;
                    }
                    emitter.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ResponseBean<List<BudgetBean>>> queryBudgetList(RequestBody requestBody) {
        return Observable.create(new ObservableOnSubscribe<ResponseBean<List<BudgetBean>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ResponseBean<List<BudgetBean>>> emitter) throws Exception {
                try {
                    Thread.sleep(100);
                    String json = "{\"code\":200,\"msg\":\"success\",\"data\":[]}";
                    ResponseBean<List<BudgetBean>> responseBean = JSON.parseObject(json, new TypeReference<ResponseBean<List<BudgetBean>>>(){});
                    String requestJson = getJsonByRequestBody(requestBody);
                    BudgetRequestBean requestBean = JSON.parseObject(requestJson, BudgetRequestBean.class);
                    responseBean.data = new ArrayList<>();
                    for (int i = 0; i < requestBean.num; i++) {
                        responseBean.data.add(new BudgetBean((i + 1) + "000", "测试数据" + (i + 1), "20200811", 5, 24,  true));
                    }
                    if (emitter.isDisposed()){
                        return;
                    }
                    emitter.onNext(responseBean);
                    emitter.onComplete();
                }catch (Exception e){
                    e.printStackTrace();
                    if (emitter.isDisposed()){
                        return;
                    }
                    emitter.onError(e);
                }
            }
        });
    }

    /** 将RequestBody的请求内容取出 */
    private String getJsonByRequestBody(RequestBody requestBody) {
        try {
            Class<?> c =  requestBody.getClass();
            List<String> list = ReflectUtils.getFieldName(c);
            for (String name : list) {
                Object o = ReflectUtils.getFieldValue(c, requestBody, name);
                if (o instanceof byte[]){
                    byte[] bytes = (byte[]) o;
                    return new String(bytes);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
