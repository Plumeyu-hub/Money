package com.snxun.book.utils.sp;

/**
 * SharedPreferences管理类
 * Created by zhouL on 2016/12/26.
 */
public class SpManager {

    private static SpManager mInstance = new SpManager();

    public static SpManager get() {
        return mInstance;
    }

    private SpManager() {}

    /**
     * 设置用户account
     * @param userAccount 用户id
     */
    public void setUserAccount(String userAccount){
        SharedPreferencesUtils.putString(SpConfig.USER_ACCOUNT, userAccount);
    }

    /** 获取用户account */
    public String getUserAccount(){
        return SharedPreferencesUtils.getString(SpConfig.USER_ACCOUNT,"" );
    }

    /**
     * 设置用户登录状态
     * @param UserStutas 用户登录状态
     */
     public void setUserStatus(Boolean UserStutas){
        SharedPreferencesUtils.putBoolean(SpConfig.USER_STATUS, UserStutas);
    }

    /** 获取用户登录状态 */
    public Boolean getUserStatus(){
        return SharedPreferencesUtils.getBoolean(SpConfig.USER_STATUS, false);
    }
}
