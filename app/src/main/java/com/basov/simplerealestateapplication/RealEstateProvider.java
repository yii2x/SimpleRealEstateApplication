package com.basov.simplerealestateapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/*
Real estate content provider
 */

public class RealEstateProvider extends ContentProvider {
    // fields for my content provider
    static final String PROVIDER_NAME = "com.basov.simplerealestateapplication";
    static final String URL = "content://" + PROVIDER_NAME + "/properties";
    static final Uri CONTENT_URI = Uri.parse(URL);

    // integer values used in content URI
    static final int PROPERTIES = 1;
    static final int PROPERTY_ID = 2;

    // map content URI "patterns" to the integer values that were set above
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "properties", PROPERTIES);
        uriMatcher.addURI(PROVIDER_NAME, "property/#", PROPERTY_ID);
    }

    // database handler
    DBHelper dbHelper;

    // project map for a query
    private static HashMap<String, String> birthMap;

    // fields for the database
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ROOMS = "rooms";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_ZIP = "zip";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_LAT = "lat";

    // database declarations
    private SQLiteDatabase database;
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "basov.db";
    public static final String TABLE_NAME = "real_estate";
    static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ROOMS + " INTEGER NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_CITY + " TEXT NOT NULL, " +
                    COLUMN_STATE + " TEXT NOT NULL, " +
                    COLUMN_ZIP + " TEXT NOT NULL, " +
                    COLUMN_LNG + " TEXT NOT NULL, " +
                    COLUMN_LAT + " TEXT NOT NULL" + ");";

    // class that creates and manages the provider's database
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // create database on first time and on upgrade
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        // drop and recreate database table
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                              int oldVersion, int newVersion) {

            Log.i(DBHelper.class.getName(),
                    "Upgrading database from version " + oldVersion +
                    " to " + newVersion + ".  Old data will be destroyed");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public RealEstateProvider() {
    }

    //delete real estate property
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case PROPERTIES:
                count = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case PROPERTY_ID:
                // textUtils.isEmpty checks whether the user entered anything in the field.
                // getLastPathSegment gets the last decoded segment in the path...so if the
                // last segment is ID, it will return the ID
                String id = uri.getLastPathSegment();       // gets the id
                count = database.delete(
                        TABLE_NAME, COLUMN_ID + " = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" +
                                        selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;

    }

    // get type of request
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            // get all properties records
            case PROPERTIES:
                return "vnd.android.cusor.dir/vnd.basov.simplerealestateapplication";
            // get a particular propery
            case PROPERTY_ID:
                return "vnd.android.cusor.item/vnd.basov.simplerealestateapplication";      //returns a single friend
            default:
               throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    //insert real estate property
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long row = database.insert(TABLE_NAME, "", values);

        // if record is added successfully
        if(row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);

            return newUri;
        }

        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBHelper(context);
        // permission to be writable
        database = dbHelper.getWritableDatabase();
        if(database == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // the TABLE_NAME to query on
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            // maps all database column names
            case PROPERTIES:
                queryBuilder.setProjectionMap(birthMap);
                break;
            case PROPERTY_ID:
                queryBuilder.appendWhere(COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(sortOrder == null || sortOrder == "") {
            // No sorting -> sort on names by default
            sortOrder = COLUMN_NAME;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection,
            selectionArgs, null, null, sortOrder);
        // register to watch a content URI for changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case PROPERTIES:
                count = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case PROPERTY_ID:
                // textUtils.isEmpty checks whether the user entered anything in the field.
                // getLastPathSegment gets the last decoded segment in the path...so if the
                // last segment is ID, it will return the ID
                count = database.update(
                        TABLE_NAME, values, COLUMN_ID + " = " +
                    uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                        selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
