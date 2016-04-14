package neagucristian.bookbunker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

        LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                LibraryContract.BookEntry._ID,
                LibraryContract.BookEntry.COLUMN_TITLE,
                LibraryContract.BookEntry.COLUMN_AUTHOR,
                LibraryContract.BookEntry.COLUMN_COMMENT,
                LibraryContract.BookEntry.COLUMN_RATING,
                //LibraryContract.BookEntry.COLUMN_PHOTO
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
        //ImageView photo = (ImageView) rootView.findViewById(R.id.item_photo);
        c.moveToNext();
        author.setText(c.getString(2));
        title.setText(c.getString(1));
        comment.setText(c.getString(3));
        rating.setRating(c.getInt(4));
        //photo.set

        return rootView;
    }

}