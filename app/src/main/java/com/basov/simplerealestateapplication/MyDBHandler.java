/*
 * Copyright (c) 2016. All Rights Reserved.
 */

package com.basov.simplerealestateapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {

    // define database variables
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "basov.db";
    public static final String TABLE_REAL_ESTATE = "real_estate";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ROOMS = "rooms";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_ZIP = "zip";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_LAT = "lat";

    // define sqlite database variable
    private SQLiteDatabase database;

    // constructor
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // responsible for creating a table for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        database = db;
        String query = "CREATE TABLE " + TABLE_REAL_ESTATE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_ROOMS + " INTEGER NOT NULL, " +
                COLUMN_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_CITY + " TEXT NOT NULL, " +
                COLUMN_STATE + " TEXT NOT NULL, " +
                COLUMN_ZIP + " TEXT NOT NULL, " +
                COLUMN_LNG + " TEXT NOT NULL, " +
                COLUMN_LAT + " TEXT NOT NULL" + ");";
        database.execSQL(query);



    }

    // responsible for making updates to an existing table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // delete the entire table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REAL_ESTATE);
        // recreate the table with the new properties
        onCreate(db);
    }

    public MyDBHandler open() throws SQLException {
        database = getWritableDatabase();   // get reference to the database
        return this;
    }

    public void deleteAll(){
        SQLiteDatabase db =  getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_REAL_ESTATE);
    }

    public void close() {
        database.close();
    }

    // Add new row to the database
    public long addRealEstate(RealEstate realEstate) {
        // content values is built into Android that allows you to add several values in one statement
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, realEstate.get_name());
        values.put(COLUMN_ROOMS, realEstate.get_rooms());
        values.put(COLUMN_ADDRESS, realEstate.get_address());
        values.put(COLUMN_CITY, realEstate.get_city());
        values.put(COLUMN_STATE, realEstate.get_state());
        values.put(COLUMN_ZIP, realEstate.get_zip());
        values.put(COLUMN_LNG, realEstate.get_lng());
        values.put(COLUMN_LAT, realEstate.get_lat());

        if(database == null){
            open();
        }
        long res = database.insert(TABLE_REAL_ESTATE, null, values);

        // once your are done with database, then close it out to give memory back
        close();

        return res;
    }

    // Delete a product from the database
    public void deleteRealEstate (Integer id) {

        // get reference to the database
        SQLiteDatabase db = getWritableDatabase();
        // delete the row with matching id
        db.execSQL("DELETE FROM " + TABLE_REAL_ESTATE + " WHERE " + COLUMN_ID + "=\"" + id + "\";");
    }

    public Cursor readEntry(String rooms) {

        String whereClause = null;
        String[] whereArgs = null;


        if(rooms.equals("*") == false){
            whereClause = "rooms = ?";
            whereArgs = new String[] {
                    rooms
            };
        }


        String[] allColumns = new String[] {
                COLUMN_ID, COLUMN_NAME, COLUMN_ROOMS, COLUMN_ADDRESS, COLUMN_CITY, COLUMN_STATE, COLUMN_ZIP, COLUMN_LNG, COLUMN_LAT
        };

        Cursor c = database.query(TABLE_REAL_ESTATE, allColumns, whereClause, whereArgs, null,
                null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }

    public void createTestData(){

        RealEstate realEstate1 = new RealEstate();
        realEstate1.set_name("Property 1");
        realEstate1.set_rooms("1");
        realEstate1.set_address("8888 Balboa Ave");
        realEstate1.set_city("San diego");
        realEstate1.set_state("CA");
        realEstate1.set_zip("92123");
        realEstate1.set_lat("32.825145");
        realEstate1.set_lng("-117.1397909");

        addRealEstate(realEstate1);

        RealEstate realEstate2 = new RealEstate();
        realEstate2.set_name("Property 2");
        realEstate2.set_rooms("2");
        realEstate2.set_address("8888 Balboa Ave");
        realEstate2.set_city("San diego");
        realEstate2.set_state("CA");
        realEstate2.set_zip("92123");
        realEstate2.set_lat("32.822145");
        realEstate2.set_lng("-117.1394909");

        addRealEstate(realEstate2);


        RealEstate realEstate3 = new RealEstate();
        realEstate3.set_name("Property 3");
        realEstate3.set_rooms("3");
        realEstate3.set_address("8888 Balboa Ave");
        realEstate3.set_city("San diego");
        realEstate3.set_state("CA");
        realEstate3.set_zip("92123");
        realEstate3.set_lat("32.829145");
        realEstate3.set_lng("-117.1399909");

        addRealEstate(realEstate3);
    }
}
