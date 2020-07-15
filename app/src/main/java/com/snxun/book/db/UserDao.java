  package com.snxun.book.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

  public class UserDao implements Dao {
	Context context;
	MyOpenhelper myopenhelper;
	SQLiteDatabase db;
	Cursor cs;
	String tablename ;

	public UserDao(Context context,String tablename) {
		this.context = context;
		myopenhelper = new MyOpenhelper(context);
		this.tablename=tablename;
	}

	@Override
	public boolean insert(ContentValues cv) {
		// TODO Auto-generated method stub
		boolean b = false;
		try {
			db = myopenhelper.getWritableDatabase();
			int num = (int) db.insert(tablename, null, cv);
			if (num > 0) {
				b = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}
		}

		return b;
	}

	@Override
	public boolean delete(String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean b = false;

		try {
			db = myopenhelper.getWritableDatabase();
			int num = db.delete(tablename, whereClause, whereArgs);
			if (num > 0) {
				b = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}
		}

		return b;
	}

	@Override
	public boolean updata(ContentValues cv, String whereClause,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean b = false;
		try {
			db = myopenhelper.getWritableDatabase();
			int num = db.update(tablename, cv, whereClause, whereArgs);
			if (num > 0) {
				b = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}
		}
		return b;
	}

	@Override
	public List<String> query(boolean distinct,String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy,String num) {
		// TODO Auto-generated method stub
		List<String> list = null;
		try {
			db = myopenhelper.getReadableDatabase();
			cs = db.query(distinct,tablename, columns, selection, selectionArgs,
					groupBy, having, orderBy,num);
			if (cs != null) {
				list = new ArrayList<String>();
				while (cs.moveToNext()) {
					String name = cs.getString(cs.getColumnIndex("name"));
					list.add(name);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}
		}

		return list;
	}
}
