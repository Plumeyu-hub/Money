package com.snxun.book.db;

import com.snxun.book.greendaolib.GreenDaoManager;
import com.snxun.book.greendaolib.dao.BillTableDao;
import com.snxun.book.greendaolib.dao.UserTableDao;
import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.greendaolib.table.UserTable;

import java.util.Date;
import java.util.List;

/**
 * GreenDao实现类
 *
 * @author zhouL
 * @date 2020/7/24
 */
public class GreenDaoImpl implements DbHelper {

    /**
     * 保存用户信息（插入成功返回true，失败返回false）
     */
    @Override
    public boolean saveUserInfo(String account, String pswd, String pswdQuestion, String pswdAnswer) {
        UserTable table = getUserInfo(account);
        if (table != null) {// 查出数据说明该账号已经注册过，不允许再保存
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

    /**
     * 根据账号获取用户信息
     */
    @Override
    public UserTable getUserInfo(String account) {
        return GreenDaoManager.get().getDaoSession()
                .getUserTableDao()
                .queryBuilder()
                .where(UserTableDao.Properties.Account.eq(account))
                .unique();
    }

    /**
     * 获取所有用户信息
     */
    @Override
    public List<UserTable> getAllUsersInfo() {
        return GreenDaoManager.get().getDaoSession()
                .getUserTableDao()
                .queryBuilder()
                .list();
    }

    /**
     * 保存账单信息（插入成功返回true，失败返回false）
     *
     * @param category 类别
     * @param money    金额
     * @param date     日期
     * @param mode     (金额扣除或增加的)账户或方式
     * @param remark   备注
     * @param symbol   符号（0为支出，1为收入）
     * @param account  用户账号
     * @return
     */
    @Override
    public boolean saveBillInfo(String category, Long money, Date date, String mode, String remark, int symbol, String account) {
        BillTable table = new BillTable();
        table.setCategory(category);
        table.setMoney(money);
        table.setDate(date);
        table.setMode(mode);
        table.setRemark(remark);
        table.setSymbol(symbol);
        table.setAccount(account);
        GreenDaoManager.get().getDaoSession().getBillTableDao().insert(table);
        return true;
    }

    /**
     * 根据账号获取用户保存的账单信息(列表)
     * 按用户名和日期查找
     * 并按时间顺序升序
     *
     * @param account 账号
     */
    @Override
    public List<BillTable> getAllAccountBillInfo(String account, Date date, Date detea) {
        return GreenDaoManager.get().getDaoSession()
                .getBillTableDao()
                .queryBuilder()
                .where(BillTableDao.Properties.Account.eq(account), BillTableDao.Properties.Date.between(date, detea))
                .orderAsc(BillTableDao.Properties.Date)
                .list();
    }

}
