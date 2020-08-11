package com.snxun.book.utils.sp;

/**
 * SharedPreferences配置信息
 * Created by zhouL on 2016/12/26.
 */
public class SpConfig {

    private SpConfig() {}

    /** sp文件名称 */
    public static final String SP_NAME = "sp_setting";

//---------------------------- 存储的Key -----------------------------------

    /** 用户account */
    public static final String USER_ACCOUNT = "user_account";
    /** 用户登录状态 */
    public static final String USER_STATUS = "user_stutas";
    /** 用户定时记账的状态 */
    public static final String REMIND_STATUS =  "remind_stutas";
}
