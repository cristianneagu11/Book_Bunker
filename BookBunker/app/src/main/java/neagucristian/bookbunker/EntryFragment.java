package neagucristian.bookbunker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

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

        Button addEntry = (Button) rootView.findViewById(R.id.entry_addButton);
        addEntry.setOnClickListener(new View.OnClickListener () {
            public void onClick(View v) {
                EditText author = (EditText) rootView.findViewById(R.id.entry_authorEdit);
                EditText title = (EditText) rootView.findViewById(R.id.entry_titleEdit);
                EditText comment = (EditText) rootView.findViewById(R.id.entry_commentEdit);
                RatingBar rating = (RatingBar) rootView.findViewById(R.id.ratingBar);
//                Button takePhoto = (Button) rootView.findViewById(R.id.entry_takePhoto);
//                takePhoto.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        int REQUEST_IMAGE_CAPTURE = 1;
//                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                        }
//                    }
//                });
                if(title.getText().length()==0) {
                    Toast toast = Toast.makeText(getContext(), "No title entered", Toast.LENGTH_LONG);
                    toast.show();
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
//                values.put(BookEntry.COLUMN_PHOTO, )

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

}
