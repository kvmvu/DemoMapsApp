package com.example.eric.demomapsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.eric.demomapsapp.model.Ticket;

import java.util.ArrayList;
import java.util.List;

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

    private static final String TABLE_TICKET = "ticket";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "ticket_name";
    private static final String KEY_PRICE = "ticket_price";

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERNAME + " TEXT, "
            + COLUMN_PASSWORD + " TEXT); ";

    public static final String CREATE_TABLE_TICKET = "CREATE TABLE " + TABLE_TICKET + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_PRICE + " TEXT);";



    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_TICKET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
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

    public void addTicket(Ticket ticket){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ticket.get_ticket_name());
        values.put(KEY_PRICE, ticket.get_ticket_price());

        db.insert(TABLE_TICKET, null, values);
        db.close();
    }

    public Ticket getTicket(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TICKET, new String[]{KEY_ID, KEY_NAME, KEY_PRICE}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Ticket ticket = new Ticket(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return ticket;
    }

    public List<Ticket> getAllTickets(){
        List<Ticket> ticketList = new ArrayList<Ticket>();

        String selectQuery = "SELECT * FROM " + TABLE_TICKET;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                Ticket ticket = new Ticket();
                ticket.set_id(Integer.parseInt(cursor.getString(0)));
                ticket.set_ticket_name(cursor.getString(1));
                ticket.set_ticket_price(cursor.getString(2));

                ticketList.add(ticket);
            }while (cursor.moveToNext());
        }

        return ticketList;
    }

    public int getTicketCount(){
        String countQuery = "SELECT * FROM " + TABLE_TICKET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        return cursor.getCount();
    }

    public int updateTicket(Ticket ticket){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ticket.get_ticket_name());
        values.put(KEY_PRICE, ticket.get_ticket_price());

        return db.update(TABLE_TICKET, values, KEY_ID + " = ?", new String[]{String.valueOf(ticket.get_id())});
    }

    public void deleteTicket(Ticket ticket){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TICKET, KEY_ID + " = ?",
                new String[] { String.valueOf(ticket.get_id()) });
        db.close();
    }
}
