package neagucristian.bookbunker;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import neagucristian.bookbunker.LibraryContract.BookEntry;

public class EntryFragment extends android.support.v4.app.Fragment {

    public EntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_entry, container, false);
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_entry, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        EditText author = (EditText) getActivity().findViewById(R.id.entry_authorEdit);
        EditText title = (EditText) getActivity().findViewById(R.id.entry_titleEdit);
        EditText comment = (EditText) getActivity().findViewById(R.id.entry_commentEdit);
        RatingBar rating = (RatingBar) getActivity().findViewById(R.id.ratingBar);

        if (id == R.id.entry_add) {
            if (title.getText().length() == 0) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Snackbar snack = Snackbar.make(getView(), "No title entered!!!", Snackbar.LENGTH_LONG);
                snack.show();
                return true;
            }

            BookProvider provider = new BookProvider(getContext());

            if (provider.getDuplicateState(author.getText().toString(), title.getText().toString())) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Snackbar snack = Snackbar.make(getView(), "Duplicate entry!!!", Snackbar.LENGTH_LONG);
                snack.show();
                provider.closer();
                return true;
            }

            provider.closer();

            LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_AUTHOR, author.getText().toString());
            values.put(BookEntry.COLUMN_TITLE, title.getText().toString());
            values.put(BookEntry.COLUMN_COMMENT, comment.getText().toString());
            values.put(BookEntry.COLUMN_RATING, rating.getRating());

            db.insert(
                    BookEntry.TABLE_NAME,
                    null,
                    values
            );

            db.close();

            author.setText("");
            title.setText("");
            comment.setText("");
            rating.setRating(0);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            Snackbar snack = Snackbar.make(getView(), "Entry added", Snackbar.LENGTH_LONG);
            snack.show();
        }

        if (id == R.id.entry_clear) {
            author.setText("");
            title.setText("");
            comment.setText("");
            rating.setRating(0);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            Snackbar snack = Snackbar.make(getView(), "Entry cleared", Snackbar.LENGTH_LONG);
            snack.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
