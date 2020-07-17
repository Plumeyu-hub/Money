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
     * 设置用户id
     * @param userId 用户id
     */
    public void setUserId(int userId){
        SharedPreferencesUtils.putInt(SpConfig.USER_ID, userId);
    }

    /** 获取用户id */
    public int getUserId(){
        return SharedPreferencesUtils.getInt(SpConfig.USER_ID, 0);
    }

    /**
     * 设置用户登录状态
     * @param UserStutas 用户登录状态
     */
     public void setUserStatus(Boolean UserStutas){
        SharedPreferencesUtils.putBoolean(SpConfig.USER_STATUS, UserStutas);
    }

    /** 获取用户账号 */
    public Boolean getUserStatus(){
        return SharedPreferencesUtils.getBoolean(SpConfig.USER_STATUS, false);
    }
}
