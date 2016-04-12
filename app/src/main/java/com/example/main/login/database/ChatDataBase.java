package com.example.main.login.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDataBase extends SQLiteOpenHelper {
	private static String DATABASE = "chatrecord_db"; // ��ݿ���
	private static String INFOTABLE = ""; // ����
	private static String ID = "id"; // ������ID
	private static String CONTENT = "content"; // ��������

	public ChatDataBase(Context context, String infotable) {
		super(context, DATABASE, null, 1);
		INFOTABLE = "r"+infotable;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + INFOTABLE + "( " + ID + " int, " + CONTENT
				+ " text )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
