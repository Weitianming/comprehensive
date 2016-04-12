package com.example.main.login.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDataBase extends SQLiteOpenHelper {
	private static String DATABASE = "info_db";
	private static String INFOTABLE = "InfoTable";
	private static String NAME = "name";
	private static String PWD = "pwd";

	public LoginDataBase(Context context) {
		super(context, DATABASE, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + INFOTABLE + "( " + NAME + " text, " + PWD
				+ " text )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
