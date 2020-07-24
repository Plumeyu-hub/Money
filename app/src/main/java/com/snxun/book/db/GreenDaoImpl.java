package com.snxun.book.db;

import com.snxun.book.greendaolib.GreenDaoManager;
import com.snxun.book.greendaolib.dao.UserTableDao;
import com.snxun.book.greendaolib.table.UserTable;

import java.util.List;

/**
 * GreenDao实现类
 * @author zhouL
 * @date 2020/7/24
 */
public class GreenDaoImpl implements DbHelper{

    @Override
    public boolean saveUserInfo(String account, String pswd, String pswdQuestion, String pswdAnswer) {
        UserTable table = getUserInfo(account);
        if (table != null){// 查出数据说明该账号已经注册过，不允许再保存
            return false;
        }
        table = new UserTable();
        table.setAccount(account);
        table.setPswd(pswd);
        table.setPswdQuestion(pswdQuestion);
        table.setPswdAnswer(pswdAnswer);
        GreenDaoManager.get().getDaoSession().getUserTableDao().insert(table);
        return true;
    }

    @Override
    public UserTable getUserInfo(String account) {
        return GreenDaoManager.get().getDaoSession()
                .getUserTableDao()
                .queryBuilder()
                .where(UserTableDao.Properties.Account.eq(account))
                .unique();
    }

    @Override
    public List<UserTable> getAllUsersInfo() {
        return GreenDaoManager.get().getDaoSession()
                .getUserTableDao()
                .queryBuilder()
                .list();
    }
}
