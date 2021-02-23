package com.example.contact_baljeet_c0782220_android;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

     SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc , String phone,String email,String lastname) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FIRSTNAME, name);
        contentValue.put(DatabaseHelper.ADDRESS, desc);
        contentValue.put(DatabaseHelper.PHONENO, phone);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.LASTNAME, lastname);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.FIRSTNAME, DatabaseHelper.ADDRESS, DatabaseHelper.PHONENO,DatabaseHelper.EMAIL,DatabaseHelper.LASTNAME};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }



    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public int update(long _id, String name, String address , String phone,String email,String lastname) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FIRSTNAME, name);
        contentValue.put(DatabaseHelper.ADDRESS, address);
        contentValue.put(DatabaseHelper.PHONENO, phone);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.LASTNAME, lastname);

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValue, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

}
