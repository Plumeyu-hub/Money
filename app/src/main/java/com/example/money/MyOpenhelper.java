package com.example.money;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
 * 数据库管理和维护类
 */
public class MyOpenhelper extends SQLiteOpenHelper {

	private static String name = "data.db";// 数据库文件名称
	// 如果你的数据库创立了，然后之后又要重新添加表，那么就把这个版本号重新修改，修改之后他就会再次创建
	private static int version = 11;// 数据库版本号

	public MyOpenhelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	// 只有创建数据库文件的时候才执行
	// //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase sdb) {
		// TODO Auto-generated method stub
		sdb.execSQL("create table if not exists excategory (excid integer primary key,excname text)");
		sdb.execSQL("create table if not exists incategory (incid integer primary key,incname text)");
	}

	// 只有oldVersion和NewVersion不一样的时候才调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// 可以修改或者删除重建数据库
		// sdb.execSQL("drop table if exists user");
		// sdb.execSQL("drop table if exists search");
		// onCreate(sdb);
	}

}
