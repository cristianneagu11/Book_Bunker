package neagucristian.bookbunker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import neagucristian.bookbunker.R;

public class EditFragment extends android.support.v4.app.Fragment {

    private int id;

    public EditFragment() {
    }

    public static final EditFragment newInstance(int id) {
        EditFragment f = new EditFragment();
        Bundle bdl = new Bundle(2);
        bdl.putInt("id", id);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

        BookProvider provider = new BookProvider(getContext());

        Cursor c = provider.getById(id);

        EditText title = (EditText) rootView.findViewById(R.id.edit_titleEdit);
        EditText author = (EditText) rootView.findViewById(R.id.edit_authorEdit);
        EditText comment = (EditText) rootView.findViewById(R.id.edit_commentEdit);
        RatingBar rating = (RatingBar) rootView.findViewById(R.id.edit_ratingBar);
        c.moveToNext();
        title.setText(c.getString(1));
        author.setText(c.getString(2));
        comment.setText(c.getString(3));
        rating.setRating(c.getInt(4));

        c.close();
        provider.closer();
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idItem = item.getItemId();
        if (idItem == R.id.edit_accept) {



            LibraryDbHelper dbHelper = new LibraryDbHelper(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            EditText title = (EditText) getActivity().findViewById(R.id.edit_titleEdit);
            EditText author = (EditText) getActivity().findViewById(R.id.edit_authorEdit);
            EditText comment = (EditText) getActivity().findViewById(R.id.edit_commentEdit);
            RatingBar rating = (RatingBar) getActivity().findViewById(R.id.edit_ratingBar);

            if (title.getText().length() == 0) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Snackbar snack = Snackbar.make(getView(), "No title entered!!!", Snackbar.LENGTH_LONG);
                snack.show();
                return true;
            }

            BookProvider provider = new BookProvider(getContext());
            Cursor c = provider.getById(id);
            c.moveToNext();
            if (!c.getString(1).equals(title.getText().toString())) {
                if (provider.getDuplicateState(author.getText().toString(), title.getText().toString())) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    Snackbar snack = Snackbar.make(getView(), "Duplicate entry!!!", Snackbar.LENGTH_LONG);
                    snack.show();
                    provider.closer();
                    return true;
                }
            }

            ContentValues values = new ContentValues();

            values.put(LibraryContract.BookEntry.COLUMN_AUTHOR, author.getText().toString());
            values.put(LibraryContract.BookEntry.COLUMN_TITLE, title.getText().toString());
            values.put(LibraryContract.BookEntry.COLUMN_COMMENT, comment.getText().toString());
            values.put(LibraryContract.BookEntry.COLUMN_RATING, rating.getRating());

            db.update(LibraryContract.BookEntry.TABLE_NAME, values, "_id=" + Integer.toString(id), null);

            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            Snackbar snack = Snackbar.make(getView(), "Information updated", Snackbar.LENGTH_SHORT);
            snack.show();

            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (idItem == R.id.edit_reject) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            getActivity().getSupportFragmentManager().popBackStack();

        }
        return super.onOptionsItemSelected(item);
    }

}