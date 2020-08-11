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

    private SpManager() {
    }

    /**
     * 设置用户account
     *
     * @param userAccount 用户id
     */
    public void setUserAccount(String userAccount) {
        SharedPreferencesUtils.putString(SpConfig.USER_ACCOUNT, userAccount);
    }

    /**
     * 获取用户account
     */
    public String getUserAccount() {
        return SharedPreferencesUtils.getString(SpConfig.USER_ACCOUNT, "");
    }

    /**
     * 设置用户登录状态
     *
     * @param UserStutas 用户登录状态
     */
    public void setUserStatus(Boolean UserStutas) {
        SharedPreferencesUtils.putBoolean(SpConfig.USER_STATUS, UserStutas);
    }

    /**
     * 获取用户登录状态
     */
    public Boolean getUserStatus() {
        return SharedPreferencesUtils.getBoolean(SpConfig.USER_STATUS, false);
    }

    /**
     * 删除sp存储
     */
    public void deleteSp() {
        SharedPreferencesUtils.clear();
    }

    /**
     * 设置定时记账的状态
     *
     * @param RemindStutas 用户定时记账的状态
     */
    public void setRemindStatus(Boolean RemindStutas) {
        SharedPreferencesUtils.putBoolean(SpConfig.REMIND_STATUS, RemindStutas);
    }

    /**
     * 获取定时记账的状态
     */
    public Boolean getRemindStatus() {
        return SharedPreferencesUtils.getBoolean(SpConfig.REMIND_STATUS, false);
    }
}
