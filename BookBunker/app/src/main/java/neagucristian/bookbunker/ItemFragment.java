package neagucristian.bookbunker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import neagucristian.bookbunker.R;

public class ItemFragment extends android.support.v4.app.Fragment {

    private int position;

    public ItemFragment() {
    }

    public static final ItemFragment newInstance(int position) {
        ItemFragment f = new ItemFragment();
        Bundle bdl = new Bundle(2);
        bdl.putInt("position", position);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        setHasOptionsMenu(true);

        LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                LibraryContract.BookEntry._ID,
                LibraryContract.BookEntry.COLUMN_TITLE,
                LibraryContract.BookEntry.COLUMN_AUTHOR,
                LibraryContract.BookEntry.COLUMN_COMMENT,
                LibraryContract.BookEntry.COLUMN_PHOTO,
                LibraryContract.BookEntry.COLUMN_RATING,

        };

        Cursor c = db.query(
                LibraryContract.BookEntry.TABLE_NAME,
                projection,
                "_id=?",
                new String[]{Integer.toString(position)},
                null,
                null,
                null
        );

        TextView author = (TextView) rootView.findViewById(R.id.item_author);
        TextView title = (TextView) rootView.findViewById(R.id.item_title);
        TextView comment = (TextView) rootView.findViewById(R.id.item_comment);
        RatingBar rating = (RatingBar) rootView.findViewById(R.id.item_rating);
        ImageView photo = (ImageView) rootView.findViewById(R.id.item_photo);

        c.moveToNext();

        author.setText(c.getString(2));
        title.setText(c.getString(1));
        comment.setText(c.getString(3));
        rating.setRating(c.getInt(5));

        Bitmap photoBitmap;
        byte[] bytes = c.getBlob(4);
        if (bytes != null) {
            photoBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            Toast warning4 = Toast.makeText(getContext(), "smth here", Toast.LENGTH_SHORT);
//            warning4.show();
            if (photoBitmap == null) {
                Toast warning2 = Toast.makeText(getContext(), "PHOTO NULL", Toast.LENGTH_SHORT);
                warning2.show();
            }
            photo.setImageBitmap(photoBitmap);
        }
//        else {
//            Toast warning3 = Toast.makeText(getContext(), "NO BYTES", Toast.LENGTH_SHORT);
//            warning3.show();
//        }

        db.close();
        c.close();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_edit) {
            android.support.v4.app.Fragment fragment = null;
            fragment = (android.support.v4.app.Fragment) EditFragment.newInstance(position);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontainer, fragment)
                    .addToBackStack(null).commit();
        }

        if (id == R.id.item_delete) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
                            SQLiteDatabase db = mDbHelper.getReadableDatabase();

                            db.delete(LibraryContract.BookEntry.TABLE_NAME,
                                    LibraryContract.BookEntry._ID + "=" + Integer.toString(position),
                                    null);
                            db.close();

                            android.support.v4.app.Fragment fragment = null;
                            Class fragmentClass = LibraryFragment.class;
                            try {
                                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            Snackbar snack = Snackbar.make(getView(), "Entry deleted", Snackbar.LENGTH_SHORT);
                            snack.show();

                            getActivity().getSupportFragmentManager().popBackStack();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        if (id == R.id.item_share) {
            TextView author = (TextView) getActivity().findViewById(R.id.item_author);
            TextView title = (TextView) getActivity().findViewById(R.id.item_title);
            RatingBar rating = (RatingBar) getActivity().findViewById(R.id.item_rating);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "I just finished reading " + title.getText().toString() + " by "
                            + author.getText().toString() +
                            " and i gave it a rating of " +
                            Float.toString(rating.getRating()) +
                            "/5.0" +
                            ".\nExperience shared via Book Bunker.");
            startActivity(Intent.createChooser(shareIntent, "Share using"));
        }

        return super.onOptionsItemSelected(item);
    }

}