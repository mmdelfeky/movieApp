package training.elfeky.com.gridviewexample.data;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by f on 12/09/2016.
 */
public class MovieEntry {
    static final String CONTENT_AUTHORITY = "training.elfeky.com.gridviewexample.data";
    public static final Uri BASE_CONTNT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "favMovies";
    public static final Uri CONTENT_URI = BASE_CONTNT_URI.buildUpon().appendPath(PATH_MOVIE).build();
    public static final String CONTENT_TYPE  = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+"/"+PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE  = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+"/"+PATH_MOVIE;


    static final String DATABASE_NAME = "mmd";
    static final String TABLE_NAME = "favMovies";
    static final int VERSION = 1;
    static final String ID = "id";
    static final String POSTER_LINK = "url";
    static final String TITLE = "title";
    static final String REALEASE_DATE = "realeseDate";
    static final String RATING = "rating";
    static final String OVERVIEW = "overview";
    static final String EXTRA_INFO = "exInfo";


    public static final String Create_Table = "create table " + TABLE_NAME + " ( " + ID+ " text, "
            +POSTER_LINK    +" text, "
            +TITLE          +" text, "
            +REALEASE_DATE  +" text, "
            +RATING         +" text, "
            +OVERVIEW       +" text, "
            +EXTRA_INFO           +" text "
                            +");";

    public static final String Drop_Table = "Drop table if exists " + TABLE_NAME;

    public static Uri buildMovieUri(long id)
    {
        return ContentUris.withAppendedId(CONTENT_URI,id);
    }

   public static Uri buildMovieWithId(String id)
    {
        return CONTENT_URI.buildUpon().appendPath(id).build();
    }
}
