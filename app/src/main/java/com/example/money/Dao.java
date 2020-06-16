package com.example.money;

import java.util.List;

import android.content.ContentValues;

public interface Dao {
	public boolean insert(ContentValues cv);

	public boolean delete(String whereClause, String[] whereArgs);

	public boolean updata(ContentValues cv, String whereClause,
                          String[] whereArgs);

	public List<String> query(boolean distinct, String[] columns, String selection,
                              String[] selectionArgs, String groupBy, String having,
                              String orderBy, String num);
	
}
