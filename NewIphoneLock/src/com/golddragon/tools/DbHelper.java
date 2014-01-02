package com.golddragon.tools;

import com.golddragon.constant.ConstantsUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DbHelper extends SQLiteOpenHelper {

	private static final String TAG = "";

	public DbHelper(Context context) {
		super(context, ConstantsUtil.TABLE, null, ConstantsUtil.VERSION);
	}

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + ConstantsUtil.TABLE + "("
				+ ConstantsUtil.ID
				+ " integer primary key autoincrement not null,"
				+ ConstantsUtil.FLAG + " integer not null);");

		db.execSQL("insert into lockscreen (flag) values (1)"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ConstantsUtil.TABLE);
		onCreate(db);
	}

	/*public void add(int flag) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ConstantsUtil.FLAG, flag);
		db.insert(ConstantsUtil.TABLE, "", values);
	}*/

}
