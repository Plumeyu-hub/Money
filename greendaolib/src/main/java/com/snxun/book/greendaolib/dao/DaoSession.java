package com.snxun.book.greendaolib.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.greendaolib.table.UserTable;

import com.snxun.book.greendaolib.dao.BillTableDao;
import com.snxun.book.greendaolib.dao.UserTableDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig billTableDaoConfig;
    private final DaoConfig userTableDaoConfig;

    private final BillTableDao billTableDao;
    private final UserTableDao userTableDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        billTableDaoConfig = daoConfigMap.get(BillTableDao.class).clone();
        billTableDaoConfig.initIdentityScope(type);

        userTableDaoConfig = daoConfigMap.get(UserTableDao.class).clone();
        userTableDaoConfig.initIdentityScope(type);

        billTableDao = new BillTableDao(billTableDaoConfig, this);
        userTableDao = new UserTableDao(userTableDaoConfig, this);

        registerDao(BillTable.class, billTableDao);
        registerDao(UserTable.class, userTableDao);
    }
    
    public void clear() {
        billTableDaoConfig.clearIdentityScope();
        userTableDaoConfig.clearIdentityScope();
    }

    public BillTableDao getBillTableDao() {
        return billTableDao;
    }

    public UserTableDao getUserTableDao() {
        return userTableDao;
    }

}
