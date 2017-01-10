package com.example.eric.demomapsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Eric on 9/25/2016.
 */

public class DbHelper extends SQLiteOpenHelper{
    public static final String TAG = DbHelper.class.getSimpleName();
    public static final String DB_NAME = "demomapsapp.db";
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT "
            + COLUMN_USERNAME + " TEXT "
            + COLUMN_PASSWORD + " TEXT); ";



    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public void addUser(String username, String pass){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, pass);

        long id = sqLiteDatabase.insert(USER_TABLE, null, contentValues);
        sqLiteDatabase.close();

        Log.d(TAG, "user inserted" + id);
    }

    public boolean getUser(String email, String pass){
        String selectQuery = "select * from " + USER_TABLE + " where " + COLUMN_USERNAME + " = " + " '"
                + email + "' " + " and " + COLUMN_PASSWORD + " = " + " '" + pass + "' ;";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            return true;
        }
        cursor.close();
        sqLiteDatabase.close();

        return false;
    }
}
