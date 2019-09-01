package com.example.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "items_table";
    private static final String _ID = "id";
    private static final String COLUMN_ITEM = "item";
    private static final String COLUMN_DATE_TIME = "dateTime";

    private static int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ITEM
                + " TEXT," + COLUMN_DATE_TIME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //adds data in the database.....
    public void addData(String item, String dt) {

        //getting writable pointer/object..
        SQLiteDatabase db = this.getWritableDatabase();

        //creating ContentValues object reference..
        ContentValues contentValues = new ContentValues();

        //putting values int the reference..
        contentValues.put(COLUMN_ITEM, item);
        contentValues.put(COLUMN_DATE_TIME, dt);

        //inserting elements in the database..
        long result = db.insert(TABLE_NAME, null, contentValues);

    }

    //returns data from database.....
    public Cursor getData() {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //returns the id of the selected item.....
    public Cursor getItemId(String item, String dt) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = " SELECT " + _ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM + "= '" + item + "'"
                + " AND " + COLUMN_DATE_TIME + "= '" + dt + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    //deletes the item selected from the database.....
    public void deleteItem(int id, String item) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + _ID + " ='" + id + "'" + " AND "
                + COLUMN_ITEM + "='" + item + "'";
        db.execSQL(query);
    }
}
