package neagucristian.bookbunker;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import neagucristian.bookbunker.LibraryContract.BookEntry;

public class LibraryFragment extends android.support.v4.app.Fragment {


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


        ArrayList<ListEntry> objects = new ArrayList<ListEntry>();
        ListEntry object;
        if(c != null) {
            while(c.moveToNext()) {
                object = new ListEntry(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
                objects.add(object);
            }
        ListAdapter adapter = new ListAdapter(getContext(), objects);
        list.setAdapter(adapter);
        View emptyView = rootView.findViewById(R.id.library_oops);
            list.setEmptyView(emptyView);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                position += 1;
                android.support.v4.app.Fragment fragment = null;
                fragment = (android.support.v4.app.Fragment) ItemFragment.newInstance(position);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontainer, fragment).commit();
            }
        });

        c.close();
        db.close();


        return rootView;
    }

}