package com.snxun.book.greendaolib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.snxun.book.greendaolib.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by zhouL on 2018/5/9.
 */
public class UpgradeDevOpenHelper extends DaoMaster.DevOpenHelper {

    public UpgradeDevOpenHelper(Context context, String name) {
        super(context, name);
    }

    public UpgradeDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database database, int oldVersion, int newVersion) {
        Log.i(GreenDaoManager.TAG, "Upgrading schema from version " + oldVersion + " to " + newVersion);
        if (oldVersion < newVersion) {//升级
            switch (oldVersion) {
                case 1:
                    version1to2(database);
                case 2:
                    version2to3(database);
                    break;
                default:
                    break;
            }
        }
    }

    /** 版本1升级到版本2 */
    private void version1to2(Database database) {
        // 增加一个实体表
//        ClubDao.createTable(db, false);
        // 修改Note表
//        db.execSQL("ALTER TABLE 'User' ADD 'auther' String");
    }

    /** 版本2升级到版本3 */
    private void version2to3(Database database) {
        //do something
    }
}
