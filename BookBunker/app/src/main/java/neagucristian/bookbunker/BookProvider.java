package neagucristian.bookbunker;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import neagucristian.bookbunker.LibraryContract.BookEntry;
public class BookProvider {


    private Context context;
    private LibraryDbHelper mDbHelper;
    private SQLiteDatabase db;


    public BookProvider(Context context) {
        this.context=context;
        mDbHelper = new LibraryDbHelper(context);
        db = mDbHelper.getReadableDatabase();
    }

    public Cursor getEntireList() {


        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_COMMENT,
                BookEntry.COLUMN_RATING
        };

        String sortOrder = LibraryContract.BookEntry._ID + " ASC";

        Cursor c = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return c;
    }

    public Cursor getWithoutComment() {

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_RATING
        };

        String sortOrder = LibraryContract.BookEntry._ID + " ASC";

        Cursor c = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return c;
    }

    public Cursor getById(int id) {

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_COMMENT,
                BookEntry.COLUMN_RATING
        };

        Cursor c = db.query(
                BookEntry.TABLE_NAME,
                projection,
                "_id=?",
                new String[]{Integer.toString(id)},
                null,
                null,
                null
        );
        return c;
    }

    public long getCount() {
        long cnt = DatabaseUtils.queryNumEntries(db, LibraryContract.BookEntry.TABLE_NAME);
        return cnt;
    }

    public void closer() {
        db.close();
    }
}

