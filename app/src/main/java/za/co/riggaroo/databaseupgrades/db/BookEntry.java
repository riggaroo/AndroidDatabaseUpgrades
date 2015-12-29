package za.co.riggaroo.databaseupgrades.db;


import android.provider.BaseColumns;

public class BookEntry implements BaseColumns {

    public static final String TABLE_NAME = "books";
    public static final String COL_BOOKNAME = "book_name";

    public static final String COL_NO_PAGES = "book_pages";

    public static final String COL_DESCRIPTION = "book_description";
    public static final String COL_RATING = "book_rating";

    public static final String SQL_CREATE_BOOK_ENTRY_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            BookEntry.COL_BOOKNAME + " TEXT ," +
            BookEntry.COL_DESCRIPTION + " TEXT, " +
            BookEntry.COL_RATING + " INTEGER, " +
            BookEntry.COL_NO_PAGES + " INTEGER )";

}
