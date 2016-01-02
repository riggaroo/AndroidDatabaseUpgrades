package za.co.riggaroo.databaseupgrades.db;


import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "database.db";
    private static final String TAG = DatabaseHelper.class.getName();
    private static final String DROP = "drop table if exists ";

    private static DatabaseHelper mInstance = null;
    private final Context context;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookEntry.SQL_CREATE_BOOK_ENTRY_TABLE);
        // The rest of your create scripts go here.

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);

        //Added new column to book table - book rating
        if (oldVersion < 2){
            db.execSQL(DROP + BookEntry.TABLE_NAME);
            db.execSQL(BookEntry.SQL_CREATE_BOOK_ENTRY_TABLE);
        }
        //Rename table to book_information - this is where things will start failing.
        if (oldVersion < 3){
            db.execSQL(DROP + BookEntry.TABLE_NAME);
            db.execSQL(BookEntry.SQL_CREATE_BOOK_ENTRY_TABLE);
        }
        // Add new column for a calculated value. By this time, if I am upgrading from version 2 to
        // version 4, my table would already contain the new column I am trying to add below,
        // which would result in a SQLException. These situations are sometimes difficult to spot,
        // as you basically need to test from every different version of database to upgrade from.
        // Some upgrades might work and some might fail with this method.
        // It is best to follow the other method that is on the master branch of this repo.
        if (oldVersion < 4){
            db.execSQL("ALTER TABLE " + BookEntry.TABLE_NAME  + " ADD COLUMN calculated_pages_times_rating INTEGER;");
        }
        //As you can probably imagine, this is a terrible way to do upgrades, Please DONT DO IT!!!!


    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }





}

