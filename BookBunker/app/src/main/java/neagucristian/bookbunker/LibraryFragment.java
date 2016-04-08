package neagucristian.bookbunker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import neagucristian.bookbunker.LibraryContract.BookEntry;

public class LibraryFragment extends android.support.v4.app.Fragment {

    ArrayAdapter<String> adapter;

    public LibraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_RATING
        };

        String sortOrder = BookEntry._ID + " ASC";

        Cursor c = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        ListView list = (ListView) rootView.findViewById(R.id.library_list);
        ArrayAdapter<String> library = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_book,
                R.id.list_item_book_textview,
                new ArrayList<String>()
        );

        String[] data;
        if(c != null) {
            while(c.moveToNext()) {
                data = new String[4];
                data[0] = Integer.toString(c.getInt(0));
                data[1] = c.getString(1);
                data[2] = c.getString(2);
                data[3] = Float.toString(c.getInt(3));
                library.add(data[0] + " " + data[1] + " " + data[2] + " "
                        + data[3]);
            }
            list.setAdapter(library);

        }
        c.close();
        db.close();


        return rootView;
    }

}