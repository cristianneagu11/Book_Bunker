package neagucristian.bookbunker;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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

    private static final int CAMERA_REQUEST = 2500;
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

        final View rootView = inflater.inflate(R.layout.fragment_entry, container, false);

        Button takePhoto = (Button) rootView.findViewById(R.id.entry_takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        });

        Button addEntry = (Button) rootView.findViewById(R.id.entry_addButton);
        addEntry.setOnClickListener(new View.OnClickListener () {
            public void onClick(View v) {
                EditText author = (EditText) rootView.findViewById(R.id.entry_authorEdit);
                EditText title = (EditText) rootView.findViewById(R.id.entry_titleEdit);
                EditText comment = (EditText) rootView.findViewById(R.id.entry_commentEdit);
                RatingBar rating = (RatingBar) rootView.findViewById(R.id.ratingBar);


                if(title.getText().length()==0) {
                    Snackbar snack = Snackbar.make(rootView, "No title entered!!!", Snackbar.LENGTH_SHORT);
                    snack.show();
                    return;
                }
                //SQL STUFF

                LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(BookEntry.COLUMN_AUTHOR, author.getText().toString());
                values.put(BookEntry.COLUMN_TITLE, title.getText().toString());
                values.put(BookEntry.COLUMN_COMMENT, comment.getText().toString());
                values.put(BookEntry.COLUMN_RATING, rating.getRating());
                values.put(BookEntry.COLUMN_PHOTO, bytePhoto);

                db.insert(
                        BookEntry.TABLE_NAME,
                        null,
                        values
                );

                db.close();

                //end of sql stuff
                author.setText("");
                title.setText("");
                comment.setText("");
                rating.setRating(0);

                if (rootView != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }

                Snackbar snack = Snackbar.make(v, "Entry added", Snackbar.LENGTH_LONG);
                snack.show();

            }
        });

        Button clearEntry = (Button) rootView.findViewById(R.id.entry_clearButton);
        clearEntry.setOnClickListener(new View.OnClickListener () {
            public void onClick(View v) {
                EditText author = (EditText) rootView.findViewById(R.id.entry_authorEdit);
                EditText title = (EditText) rootView.findViewById(R.id.entry_titleEdit);
                EditText comment = (EditText) rootView.findViewById(R.id.entry_commentEdit);
                RatingBar rating = (RatingBar) rootView.findViewById(R.id.ratingBar);

                author.setText("");
                title.setText("");
                comment.setText("");
                rating.setRating(0);

                Snackbar snack = Snackbar.make(v, "Entry cleared", Snackbar.LENGTH_SHORT);
                snack.show();
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST)
            if(resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            int bytes = photo.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            photo.copyPixelsToBuffer(buffer);
            bytePhoto = buffer.array();

        }
    }

}
