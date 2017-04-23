package com.example.readingJournal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.readingJournal.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {
    public static final  String LOG_TAG          = BookDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME    = "books.db";
    private static final int    DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_AUTHOR + " TEXT, "
                + BookEntry.COLUMN_BOOK_DESCRIPTION + " TEXT, "
                + BookEntry.COLUMN_BOOK_GENRES + " TEXT, "
                + BookEntry.COLUMN_BOOK_IMAGE_ID + " INTEGER DEFAULT -1, "
                + BookEntry.COLUMN_BOOK_NOTES + " TEXT, "
                + BookEntry.COLUMN_BOOK_QUOTES + " TEXT, "
                + BookEntry.COLUMN_BOOK_STATUS + " INTEGER DEFAULT 0, "
                + BookEntry.COLUMN_BOOK_RATING + " REAL DEFAULT 0);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}