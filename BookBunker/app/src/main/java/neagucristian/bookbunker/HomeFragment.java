package neagucristian.bookbunker;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        setHasOptionsMenu(true);

        TextView numberOfBooks = (TextView) rootView.findViewById(R.id.home_countNumber);

        BookProvider provider = new BookProvider(getContext());

        long cnt = provider.getCount();

        numberOfBooks.setText(Long.toString(cnt));
        provider.closer();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.home_share) {
            TextView numberOfBooks = (TextView) getActivity().findViewById(R.id.home_countNumber);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I have " + numberOfBooks.getText().toString() +
                    " books read and rated in my Book Bunker app!");
            startActivity(Intent.createChooser(shareIntent, "Share using"));
        }

        return super.onOptionsItemSelected(item);
    }


}