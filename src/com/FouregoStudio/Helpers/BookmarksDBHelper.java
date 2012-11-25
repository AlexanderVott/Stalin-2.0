package com.FouregoStudio.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookmarksDBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "stalin_complete_set_of_works.sqlite";
	public static final int DB_VER = 7;
	
	//table name
	public static final String DB_TABLE_NAME = "bookmarks";
		
	public static final String KEY_ID = "_id";
	public static final int COLUMN_ID = 0;
	public static final String KEY_VOLUME = "volume";
	public static final int COLUMN_VOLUME = 1;
	public static final String KEY_SECTION = "section";
	public static final int COLUMN_SECTION = 2;
	public static final String KEY_CHAPTER = "chapter";
	public static final int COLUMN_CHAPTER = 3;
	public static final String KEY_TITLE = "title";
	public static final int COLUMN_TITLE = 4;
	public static final String KEY_COMMNENT = "comment";
	public static final int COLUMN_COMMENT = 5;
	public static final String KEY_SCROLLTO = "scrollto";
	public static final int COLUMN_SCROLLTO = 6;
	public static final String KEY_POS_IN_SCROLL_LIST = "posinlist";
	public static final int COLUMN_POS_IN_SCROLL_LIST = 7;
	
	public static final String DB_QUERY_CREATE = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME + " ( " + 
			KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_VOLUME + " INTEGER, " +
			KEY_SECTION + " INTEGER, " +
			KEY_CHAPTER + " INTEGER, " +
			KEY_TITLE + " TEXT NOT NULL, " +
			KEY_COMMNENT + " TEXT," +
			KEY_SCROLLTO + " INTEGER," +
			KEY_POS_IN_SCROLL_LIST + " INTEGER" +
			")";
	
	public BookmarksDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_QUERY_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// по логике надо организовать миграцию данных с одной версии на другую, 
		// но пока сделаем вот так - просто выбрасываем старую базу
//		if (oldVersion != newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
			onCreate(db);
//		}
	}

}
