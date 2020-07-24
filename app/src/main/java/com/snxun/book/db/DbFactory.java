package com.snxun.book.db;

/**
 * 数据工厂
 * Created by zhouL on 2018/5/9.
 */
public class DbFactory {

    private DbFactory() {
    }

    public static DbHelper create(){
        return new GreenDaoImpl();
    }
}
