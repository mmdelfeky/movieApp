package training.elfeky.com.gridviewexample.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by f on 12/09/2016.
 */
public class DbHelper extends SQLiteOpenHelper{

    private Context context;

    public DbHelper(Context context) {
        super(context, MovieEntry.DATABASE_NAME, null, MovieEntry.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(MovieEntry.Create_Table);

        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage() + "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(MovieEntry.Drop_Table);
            onCreate(db);
        } catch (SQLiteException e) {
            Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
        }
    }
}
