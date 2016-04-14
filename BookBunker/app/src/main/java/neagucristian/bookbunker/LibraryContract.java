package neagucristian.bookbunker;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class LibraryContract {

    public static final String CONTENT_AUTHORITY = "neagucristian.bookbunker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOK = "book";

    public static final class BookEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String TABLE_NAME = "book";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_COMMENT = "comment";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_PHOTO = "photo";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
