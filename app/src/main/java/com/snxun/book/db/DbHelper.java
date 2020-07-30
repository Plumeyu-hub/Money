package com.snxun.book.db;

import android.database.Cursor;

import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.greendaolib.table.UserTable;
import com.snxun.book.ui.money.update.UpdateBean;

import java.util.List;

/**
 * 数据库业务接口
 *
 * @author zhouL
 * @date 2020/7/24
 */
public interface DbHelper {

    /**
     * 保存用户信息（插入成功返回true，失败返回false）
     *
     * @param account      账号
     * @param pswd         密码
     * @param pswdQuestion 密保问题
     * @param pswdAnswer   密保答案
     */
    boolean saveUserInfo(String account, String pswd, String pswdQuestion, String pswdAnswer);

    /**
     * 根据账号获取用户信息
     *
     * @param account 账号
     */
    UserTable getUserInfo(String account);

    /**
     * 获取所有用户信息
     */
    List<UserTable> getAllUsersInfo();


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
    long saveBillInfo(String category, Long money, String date, String mode, String remark, int symbol, String account);

    /**
     * 根据账号获取用户保存的账单信息(列表)
     * 并按时间顺序升序
     *
     * @param account 账号
     */
    List<BillTable> getAllAccountBillInfo(String account, String date);

    /**
     * 删除指定账单id的账单
     *
     * @param id 账单id
     * @return
     */
    boolean deleteBillInfo(Long id);

    /**
     * 搜索账单信息
     *
     * @param account
     * @param condition
     * @return
     */
    List<BillTable> getSearchBillInfo(String account, String condition);

    /**
     * 修改指定账单id的指定数据
     *
     * @param id         账单id
     * @param updateBean 修改的数据
     * @return
     */
    public boolean updateBillInfo(Long id, UpdateBean updateBean);

    /**
     * 删除指定用户名的账单
     *
     * @param account 用户名
     * @return
     */
    public boolean deleteAllBillInfo(String account);

    /**
     * 求和
     *
     * @param date    日期
     * @param account 用户名
     * @param symbol  符号
     * @return
     */
    public String sumBillInfo(String date, String account, int symbol);

    /**
     * 制定用户所有的账单数据
     *
     * @param account 用户名
     * @return
     */
    public Cursor allBillInfo(String account);
}
