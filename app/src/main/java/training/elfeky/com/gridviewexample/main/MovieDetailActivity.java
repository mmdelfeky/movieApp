package training.elfeky.com.gridviewexample.main;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import training.elfeky.com.gridviewexample.R;
import training.elfeky.com.gridviewexample.fragments.MovieDetailFragment;

public class MovieDetailActivity extends ActionBarActivity {
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Bundle bundle = getIntent().getExtras();
                ((MovieDetailFragment) getFragmentManager().findFragmentById(R.id.detailsFragment)).
                        setMovieDetails((MovieDetails) bundle.get("movie"),bundle.getBoolean("fav"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState = bundle;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bundle = savedInstanceState;
    }
}
