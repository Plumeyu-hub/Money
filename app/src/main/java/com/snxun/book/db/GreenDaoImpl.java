package com.snxun.book.db;

import android.database.Cursor;

import com.snxun.book.greendaolib.GreenDaoManager;
import com.snxun.book.greendaolib.dao.BillTableDao;
import com.snxun.book.greendaolib.dao.UserTableDao;
import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.greendaolib.table.UserTable;
import com.snxun.book.ui.money.update.UpdateBean;

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
    public long saveBillInfo(String category, Long money, String date, String mode, String remark, int symbol, String account) {
        try {
            BillTable table = new BillTable();
            table.setCategory(category);
            table.setMoney(money);
            table.setDate(date);
            table.setMode(mode);
            table.setRemark(remark);
            table.setSymbol(symbol);
            table.setAccount(account);
            return GreenDaoManager.get().getDaoSession().getBillTableDao().insert(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 按用户名和日期查找用户保存的账单信息(列表)
     * 并按时间顺序升序
     *
     * @param account 账号
     * @param date    查询的日期
     * @return
     */
    @Override
    public List<BillTable> getAllAccountBillInfo(String account, String date) {
        return GreenDaoManager.get().getDaoSession()
                .getBillTableDao()
                .queryBuilder()
                .where(BillTableDao.Properties.Account.eq(account), BillTableDao.Properties.Date.like(date + "%"))
                .orderAsc(BillTableDao.Properties.Date)
                .list();
    }

    /**
     * 删除指定账单id的账单
     *
     * @param id 账单id
     * @return
     */
    @Override
    public boolean deleteBillInfo(Long id) {
        GreenDaoManager.get().getDaoSession()
                .getBillTableDao()
                .queryBuilder()
                .where(BillTableDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
        return true;
    }

    /**
     * 搜索账单信息
     *
     * @param account
     * @param condition
     * @return
     */
    @Override
    public List<BillTable> getSearchBillInfo(String account, String condition) {
        return GreenDaoManager.get().getDaoSession()
                .getBillTableDao()
                .queryBuilder()
                .where(BillTableDao.Properties.Account.eq(account))
                .whereOr(BillTableDao.Properties.Category.like("%" + condition + "%"),
                        BillTableDao.Properties.Money.like("%" + condition + "%"),
                        BillTableDao.Properties.Date.like("%" + condition + "%"),
                        BillTableDao.Properties.Mode.like("%" + condition + "%"),
                        BillTableDao.Properties.Remark.like("%" + condition + "%"))
                .orderAsc(BillTableDao.Properties.Date)
                .list();
    }


    /**
     * 修改指定账单id的指定数据
     *
     * @param id         账单id
     * @param updateBean 修改的数据
     * @return
     */
    @Override
    public boolean updateBillInfo(Long id, UpdateBean updateBean) {
        BillTable billTable = GreenDaoManager.get().getDaoSession()
                .getBillTableDao()
                .queryBuilder()
                .where(BillTableDao.Properties.Id.eq(id))
                .build()
                .unique();
        if (billTable == null) {
            return false;
        }
        billTable.setMoney(updateBean.getMoney());
        billTable.setDate(updateBean.getDate());
        billTable.setMode(updateBean.getMode());
        billTable.setRemark(updateBean.getRemark());
        GreenDaoManager.get().getDaoSession().getBillTableDao().updateInTx(billTable);
        return true;
    }

    /**
     * 删除指定用户名的账单
     *
     * @param account 用户名
     * @return
     */
    @Override
    public boolean deleteAllBillInfo(String account) {
        GreenDaoManager.get().getDaoSession()
                .getBillTableDao()
                .queryBuilder()
                .where(BillTableDao.Properties.Account.eq(account))
                .buildDelete().executeDeleteWithoutDetachingEntities();
        return true;
    }

    /**
     * 求和
     *
     * @param date    日期
     * @param account 用户名
     * @param symbol  符号
     * @return
     */
    @Override
    public String sumBillInfo(String date, String account, int symbol) {
        Cursor cs = GreenDaoManager.get().getDaoSession()
                //.getBillTableDao()
                .getDatabase().rawQuery("select sum(money) from BILL_TABLE where date like ? and account=? and symbol=?",
                        new String[]{date + "%", account + "", String.valueOf(symbol)});
        Long sumLong = null;
        String sum;
        if (cs != null) {
            if (cs.moveToFirst()) {
                do {
                    sumLong = cs.getLong(0);
                } while (cs.moveToNext());
            }
            sum = String.format("%.2f", Float.valueOf(sumLong));
            return sum;
        } else {
            return "0.00";
        }
    }

    /**
     * 制定用户所有的账单数据
     *
     * @param account 用户名
     * @return
     */
    @Override
    public Cursor allBillInfo(String account) {
        return GreenDaoManager.get().getDaoSession()
                .getDatabase().rawQuery("select * from BILL_TABLE where account=?",
                        new String[]{account + ""});
    }


}
