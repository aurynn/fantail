package com.aurynn.fantail.sql;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aurynn on 9/06/13.
 */
public class SettingsDB extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "settings";
    public static final String COLUMN_ID = "_id";
    //    public static final String COLUMN_URL = "url";
//    public static final String COLUMN_ADDED = "added";
//    public static final String COLUMN_ADDED_STRING = "added_string";
//    public static final String COLUMN_ADDED_UPLOADED = "uploaded";
    public static final String COLUMN_DEFAULT_API = "clientid";

    private static final String DATABASE_NAME = "settings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + "clientid text" +
            ");";

    public SettingsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d("DB oldversion", Integer.toString(oldVersion));
        Log.d("DB oldversion", Integer.toString(newVersion));
    }
}
