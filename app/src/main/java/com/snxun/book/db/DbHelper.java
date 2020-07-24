package com.snxun.book.db;

import com.snxun.book.greendaolib.table.UserTable;

import java.util.List;

/**
 * 数据库业务接口
 * @author zhouL
 * @date 2020/7/24
 */
public interface DbHelper {

    /**
     * 保存用户信息（插入成功返回true，失败返回false）
     * @param account 账号
     * @param pswd 密码
     * @param pswdQuestion 密保问题
     * @param pswdAnswer 密保答案
     */
    boolean saveUserInfo(String account, String pswd, String pswdQuestion, String pswdAnswer);

    /**
     * 根据账号获取用户信息
     * @param account 账号
     */
    UserTable getUserInfo(String account);

    /** 获取所有用户信息 */
    List<UserTable> getAllUsersInfo();
}
