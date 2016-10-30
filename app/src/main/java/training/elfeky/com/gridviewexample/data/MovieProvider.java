package training.elfeky.com.gridviewexample.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by f on 19/09/2016.
 */
public class MovieProvider extends ContentProvider {
    public static final int FAVOURITE =1;
    public static final int ID = 2;
    public static UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper dbHelper;
    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case FAVOURITE:

                retCursor =  dbHelper.getWritableDatabase().query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ID:
                retCursor = dbHelper.getWritableDatabase().query(MovieEntry.TABLE_NAME, projection, MovieEntry.ID, selectionArgs, null, null, sortOrder);;
                break;
            default:
                throw  new UnsupportedOperationException("UnKnown Uri: "+uri  );
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case FAVOURITE:
                return MovieEntry.CONTENT_TYPE;
            case ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw  new UnsupportedOperationException("UnKnown Uri: "+uri  );
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retuenUri;
        long i = dbHelper.getWritableDatabase().insert(MovieEntry.TABLE_NAME, null, values);
        if(i>0) {
            retuenUri = MovieEntry.buildMovieUri(i);
        }
            else
            {
                throw new android.database.SQLException("Failed to insert row into"+MovieEntry.TABLE_NAME);
            }
        getContext().getContentResolver().notifyChange(uri,null);
        return retuenUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int count= sqLiteDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
        if(selection==null||count!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher()
    {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieEntry.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority,MovieEntry.PATH_MOVIE,FAVOURITE);
        uriMatcher.addURI(authority, MovieEntry.PATH_MOVIE + "/*", ID);
        return uriMatcher;
    }
}
