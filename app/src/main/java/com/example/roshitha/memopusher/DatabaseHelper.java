package com.example.roshitha.memopusher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by roshitha on 11/22/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    Context mContext;

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "memo_table";
    private static final String COL1 = "id";
    private static final String COL2 = "memo";
    private static final String COL3 = "time";
    private static final String COL4 = "lat";
    private static final String COL5 = "lon";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("+ COL1 +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL2 +" VARCHAR(100), "+ COL3 +" DATETIME, "+ COL4 +" DOUBLE, "+ COL5 +" DOUBLE)";
        db.execSQL(createTable);
        Toast.makeText(mContext, "Table Created" + TABLE_NAME, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String memo, String time, double lat, double lon) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, memo);
        contentValues.put(COL3, time);
        contentValues.put(COL4, lat);
        contentValues.put(COL5, lon);

        Log.d(TAG, String.format("Add data: %s, %s, %f, %f to %s", memo, time, lat, lon, TABLE_NAME));
        long result = database.insert(TABLE_NAME, null, contentValues);

        // if insertion fails this returns -1

        if (result == -1)
            return false;
        return true;
    }

    public Cursor getData() {
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean deleteData(String id) {
        String[] args = {id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(TABLE_NAME, "id=?", args);

        if (result == -1)
            return false;
        return true;
    }
}
