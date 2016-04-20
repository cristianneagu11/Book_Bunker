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

    private static final int REQUEST_IMAGE_CAPTURE = 2500;
    private ContentValues values = new ContentValues();
    private byte[] bytePhoto;
    public EntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final View rootView = inflater.inflate(R.layout.fragment_entry, container, false);
        setHasOptionsMenu(true);

        Button takePhoto = (Button) rootView.findViewById(R.id.entry_takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bytePhoto = stream.toByteArray();
                }
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
                Snackbar snack = Snackbar.make(getView(), "No title entered!!!", Snackbar.LENGTH_SHORT);
                snack.show();
                return true;
            }
            LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            if(bytePhoto!=null)
                values.put(BookEntry.COLUMN_PHOTO, bytePhoto);
            values.put(BookEntry.COLUMN_AUTHOR, author.getText().toString());
            values.put(BookEntry.COLUMN_TITLE, title.getText().toString());
            values.put(BookEntry.COLUMN_COMMENT, comment.getText().toString());
            values.put(BookEntry.COLUMN_RATING, rating.getRating());

            Toast warning2 = Toast.makeText(getContext(), "A salvat\n" +
                    values.getAsString(BookEntry.COLUMN_PHOTO), Toast.LENGTH_SHORT);
            warning2.show();

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

            Snackbar snack = Snackbar.make(getView(), "Entry cleared", Snackbar.LENGTH_SHORT);
            snack.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
