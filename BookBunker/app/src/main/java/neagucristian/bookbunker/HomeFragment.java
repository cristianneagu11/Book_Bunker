package neagucristian.bookbunker;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import neagucristian.bookbunker.R;

public class HomeFragment extends android.support.v4.app.Fragment {

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView numberOfBooks = (TextView) rootView.findViewById(R.id.home_countNumber);

        LibraryDbHelper dbHelper = new LibraryDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, LibraryContract.BookEntry.TABLE_NAME);
        db.close();
        numberOfBooks.setText(Long.toString(cnt));

        return rootView;
    }

}