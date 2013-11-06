package com.aurynn.fantail.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aurynn.fantail.model.Settings;

/**
 * Created by aurynn on 10/06/13.
 */
public class SettingsDAO {

    private SQLiteDatabase database;
    private SettingsDB dbHelper;
    private String[] allColumns = { SettingsDB.COLUMN_ID,

            SettingsDB.COLUMN_DEFAULT_API
    };
//    private Context ctx;

    public SettingsDAO(Context ctx) {
        // Connect to our wonderful database jobbie.
        dbHelper = new SettingsDB(ctx);
//        this.ctx = ctx;
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public Settings getSettings() {
//        Map<String, String> settings = new HashMap<String, String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SettingsDB.TABLE_NAME +
                "", null);
        cursor.moveToFirst();
        Settings settings = new Settings();
//        settings.setClientID( "" );
        if (!cursor.isAfterLast()) {
            settings.setClientID( cursor.getString(cursor.getColumnIndex(SettingsDB.COLUMN_DEFAULT_API) ) );
            settings.setColumnId( cursor.getString( cursor.getColumnIndex(SettingsDB.COLUMN_ID) ) );
        }
        return settings;

    }

    public Boolean save(Settings model) {
        Log.d("Saving", "OH YEAH");
        // Really this ought to happen in a background thread somewhere.
        ContentValues values = new ContentValues();

        values.put(SettingsDB.COLUMN_DEFAULT_API, model.getClientId());

        Log.d("Inserting so much!", "aw yeah");
        // .update?
        if ( model.getColumnId() == null ) {
            Log.d("Insert", "attempting to insert");
            long insertId = database.insert(
                    SettingsDB.TABLE_NAME,
                    "null",
                    values
            );
            model.setColumnId(new Long(insertId).toString());
        }
        else {
            Log.d("Model ID", model.getColumnId().toString());
            Log.d("Update", "attempting to update");
            long insertId = database.update(SettingsDB.TABLE_NAME, values,
                    null, null); // Update them all, since there should only ever be one.
        }

        Log.d("Time", "to return");
        return Boolean.TRUE;
    }
}
