package neagucristian.bookbunker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

    public static final ItemFragment newInstance(int position)
    {
        ItemFragment f = new ItemFragment();
        Bundle bdl = new Bundle(2);
        bdl.putInt("position", position);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=getArguments().getInt("position");
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
                new String[] {Integer.toString(position)},
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
        if(bytes!=null) {
            photoBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if(photoBitmap == null) {
                Toast warning2 = Toast.makeText(getContext(), "PHOTO NULL", Toast.LENGTH_SHORT);
                warning2.show();
            }
            photo.setImageBitmap(photoBitmap);
        }

        c.close();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            android.support.v4.app.Fragment fragment = null;
            fragment = (android.support.v4.app.Fragment) EditFragment.newInstance(position);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontainer, fragment)
                    .addToBackStack(null).commit();
        }
        if (id == R.id.menu_delete) {
            LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            db.delete(LibraryContract.BookEntry.TABLE_NAME,
                    LibraryContract.BookEntry._ID + "=" + Integer.toString(position),
                    null);

            android.support.v4.app.Fragment fragment = null;
            Class fragmentClass = LibraryFragment.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

}