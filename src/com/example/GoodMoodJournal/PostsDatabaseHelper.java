package com.example.GoodMoodJournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PostsDatabaseHelper extends SQLiteOpenHelper {
    private static PostsDatabaseHelper sInstance;
    private final String TAG = getClass().getName();

    // Database Info
    private static final String DATABASE_NAME = "entriesDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ENTRIES = "entries";

    // Entry Table Columns
    private static final String KEY_ENTRY_ID = "id";
    private static final String KEY_ENTRY_DATE = "date";
    private static final String KEY_ENTRY_TEXT = "text";

    public static synchronized PostsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PostsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ENTRIES +
                " (" +
                KEY_ENTRY_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                KEY_ENTRY_DATE + " DATE, " +
                KEY_ENTRY_TEXT + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
            onCreate(db);
        }
    }

    public void addEntry(Entry entry) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ENTRY_DATE, entry.date);
            values.put(KEY_ENTRY_TEXT, entry.text);
            db.insertOrThrow(TABLE_ENTRIES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while adding entry");
        } finally {
            db.endTransaction();
        }
    }

    public HashMap<Integer, Entry> getEntries(Integer limit, Integer offset) {
        HashMap<Integer, Entry> entries = new LinkedHashMap<>();

        String QUERY;
        if(limit == null && offset == null) {
            QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC", TABLE_ENTRIES, KEY_ENTRY_ID);
        }
        else {
            QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT %s OFFSET %s",
                    TABLE_ENTRIES, KEY_ENTRY_ID, limit, offset);
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    Entry entry = new Entry();
                    entry.date = cursor.getString(cursor.getColumnIndex(KEY_ENTRY_DATE));
                    entry.text = cursor.getString(cursor.getColumnIndex(KEY_ENTRY_TEXT));
                    entries.put(cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_ID)), entry);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while retrieving entries");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return entries;
    }

    public HashMap<Integer, Entry> getAllEntries() {
        return getEntries(null, null);
    }

    public Integer getCount() {
        return getAllEntries().size();
    }
}
