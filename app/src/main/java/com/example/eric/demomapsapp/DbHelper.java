package com.example.eric.demomapsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.eric.demomapsapp.model.Ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 9/25/2016.
 */

public class DbHelper extends SQLiteOpenHelper{
    public static final String TAG = DbHelper.class.getSimpleName();
    public static final String DB_NAME = "demomapsapp.db";
    public static final int DB_VERSION = 3;

    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "emailaddress";
    //public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_UID = "uniqueID";
    public static final String COLUMN_CREATEDAT = "created_at";


    private static final String TABLE_TICKET = "ticket";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "ticket_name";
    private static final String KEY_PRICE = "ticket_price";
    private static final String KEY_DESCRIPTION = "description";

    private static final String TABLE_EVENTS = "events";
    private static final String EVENT_ID = "eventID";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_DESCRIPTION = "event";
    private static final String EVENT_DATEFROM = "date_from";
    private static final String EVENT_DATETO = "date_to";
    private static final String EVENT_TIMEFROM = "time_from";
    private static final String EVENT_TIMETO = "time_to";
    private static final String EVENT_LOCATION = "location";
    private static final String EVENT_LATITUDE = "lat";
    private static final String EVENT_LONGITUDE = "lng";

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_UID + " TEXT "
            + COLUMN_USERNAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT); ";
      //      + COLUMN_PASSWORD + " TEXT); ";

    public static final String CREATE_TABLE_TICKET = "CREATE TABLE " + TABLE_TICKET + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_PRICE + " TEXT);";

    public static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + " ( "
            + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT_NAME + " TEXT, "
            + EVENT_DESCRIPTION + " TEXT, "
            + EVENT_DATEFROM + " DATE, "
            + EVENT_DATETO + " DATE, "
            + EVENT_TIMEFROM + " TIME, "
            + EVENT_TIMETO + " TIME, "
            + EVENT_LOCATION + " TEXT, "
            + EVENT_LATITUDE + " FLOAT, "
            + EVENT_LONGITUDE + " FLOAT); ";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_TICKET);
        sqLiteDatabase.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(sqLiteDatabase);
    }

    public void addUser(String username, String email, String uuid, String created_at){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UID, uuid);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_CREATEDAT, created_at);

        long id = sqLiteDatabase.insert(USER_TABLE, null, contentValues);
        sqLiteDatabase.close();

        Log.d(TAG, "user inserted" + id);
    }

    //this method fetches users from sqlite db
    public HashMap<String, String>getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("uniqueID", cursor.getString(1));
            user.put("username", cursor.getString(2));
            user.put("emailaddress", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        //return user
        Log.d(TAG, "Fetching from sqlite" + user.toString());
        return user;
    }

    /*
    public boolean getUser(String email, String uuid){
        String selectQuery = "select * from " + USER_TABLE + " where " + COLUMN_USERNAME + " = " + " '"
                + username + "' " + " and " + COLUMN_PASSWORD + " = " + " '" + pass + "' ;";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            return true;
        }
        cursor.close();
        sqLiteDatabase.close();

        return false;
    }*/

    public void addTicket(Ticket ticket){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ticket.get_ticket_name());
        values.put(KEY_PRICE, ticket.get_ticket_price());
        values.put(KEY_DESCRIPTION, ticket.get_ticket_description());

        db.insert(TABLE_TICKET, null, values);
        db.close();
    }

    public Ticket getTicket(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TICKET, new String[]{KEY_ID, KEY_NAME, KEY_PRICE, KEY_DESCRIPTION}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Ticket ticket = new Ticket(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
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
                ticket.set_ticket_price(cursor.getString(3));
                ticket.set_ticket_description(cursor.getString(2));

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
        values.put(KEY_DESCRIPTION, ticket.get_ticket_description());

        return db.update(TABLE_TICKET, values, KEY_ID + " = ?", new String[]{String.valueOf(ticket.get_id())});
    }

    public void deleteTicket(Ticket ticket){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TICKET, KEY_ID + " = ?",
                new String[] { String.valueOf(ticket.get_id()) });
        db.close();
    }

    public void addEvent(String eventName, String description, String eventLocation,
                         String eventLat, String eventLng,  String dateFrom,
                         String dateTo, String timeFrom, String timeTo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, eventName);
        values.put(EVENT_DESCRIPTION, description);
        values.put(EVENT_LOCATION, eventLocation);
        values.put(EVENT_LATITUDE, eventLat);
        values.put(EVENT_LONGITUDE, eventLng);
        values.put(EVENT_DATEFROM, dateFrom);
        values.put(EVENT_DATETO, dateTo);
        values.put(EVENT_TIMEFROM, timeFrom);
        values.put(EVENT_TIMETO, timeTo);

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }
}
