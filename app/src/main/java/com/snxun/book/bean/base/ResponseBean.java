package com.snxun.book.bean.base;

/**
 * 数据包裹层
 * Created by zhouL on 2016/11/22.
 */
public class ResponseBean<T>  {

    /** 失败 */
    public static final int Fail = 500;
    /** 成功 */
    public static final int SUCCESS = 200;

    /** 本次接口请求返回的状态 */
    public int code = Fail;
    /** 对code的简单描述 */
    public String msg = "";
    /** 数据对象 */
    public T data;

    public boolean isSuccess() {
        return code == SUCCESS;
    }

}
