package training.elfeky.com.gridviewexample.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import training.elfeky.com.gridviewexample.main.MovieDetails;

/**
 * Created by f on 12/09/2016.
 */
public class DbHandler {
    DbHelper Db;
    Context context;


    public DbHandler(Context context) {
        Db = new DbHelper(context);
        this.context = context;
    }

    public List<MovieDetails> getmovie(){
        int i = 0;
        //SQLiteDatabase sqLiteDatabase = Db.getWritableDatabase();
        String col[] = {
                MovieEntry.ID, MovieEntry.POSTER_LINK
                ,MovieEntry.TITLE, MovieEntry.REALEASE_DATE
                ,MovieEntry.RATING,MovieEntry.OVERVIEW
                ,MovieEntry.EXTRA_INFO
        };
        Cursor cursor = context.getContentResolver().query(MovieEntry.CONTENT_URI, col, null, null, null, null);
        List<MovieDetails> movies = new ArrayList<>();
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(MovieEntry.ID));
            String url = cursor.getString(cursor.getColumnIndex(MovieEntry.POSTER_LINK));
            String title = cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE));
            String realeseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.REALEASE_DATE));
            String rating = cursor.getString(cursor.getColumnIndex(MovieEntry.RATING));
            String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.OVERVIEW));
            String extra = cursor.getString(cursor.getColumnIndex(MovieEntry.EXTRA_INFO));

            MovieDetails movie = new MovieDetails();
            movie.setId(id);
            movie.setPosterLink(url);
            movie.setTitle(title);
            movie.setReleaseDate(realeseDate);
            movie.setOverview(overview);
            movie.setVoteAverage(rating);
            movie.setExtra(extra);
            movies.add(movie);
        }
        return movies;
    }


    public Uri insert(MovieDetails movieDetails) {
        //SQLiteDatabase sqLiteDatabase = Db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieEntry.ID, movieDetails.getId());
        contentValues.put(MovieEntry.POSTER_LINK, movieDetails.getPosterLink());
        contentValues.put(MovieEntry.TITLE, movieDetails.getTitle());
        contentValues.put(MovieEntry.REALEASE_DATE, movieDetails.getReleaseDate());
        contentValues.put(MovieEntry.RATING, movieDetails.getVoteAverage());
        contentValues.put(MovieEntry.OVERVIEW, movieDetails.getOverview());
        contentValues.put(MovieEntry.EXTRA_INFO,movieDetails.getExtra());
        Uri i = context.getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);

        return i;

    }

    public int deleteName(String id) {
        String[] nameDelete = {id};
        int count= context.getContentResolver().delete(MovieEntry.CONTENT_URI, MovieEntry.ID + " =? ", nameDelete);
        return count;
    }

    public int search(String id){
        int i = 0;
        //SQLiteDatabase sqLiteDatabase = Db.getWritableDatabase();
        String col[] = {MovieEntry.ID};
        String[] args = {id};
        Cursor cursor = context.getContentResolver().query(MovieEntry.CONTENT_URI, col, MovieEntry.ID + "=?", args, null);
        while (cursor.moveToNext()){
            i = 1;
        }
        return i;
    }
}
