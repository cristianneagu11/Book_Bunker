package neagucristian.bookbunker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import neagucristian.bookbunker.LibraryContract.BookEntry;

public class LibraryDbHelper extends SQLiteOpenHelper{
    
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "books.db";

    public LibraryDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                BookEntry._ID + " INTEGER PRIMARY KEY," +
                BookEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                BookEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                BookEntry.COLUMN_COMMENT + " TEXT," +
                BookEntry.COLUMN_RATING + " INTEGER NOT NULL," +
                BookEntry.COLUMN_PHOTO + " BLOB);";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TO-DO REMOVE THESE LINES
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
