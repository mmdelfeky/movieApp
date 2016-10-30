package training.elfeky.com.gridviewexample.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import training.elfeky.com.gridviewexample.data.DbHandler;
import training.elfeky.com.gridviewexample.helpers.MovieDetailStatics;
import training.elfeky.com.gridviewexample.helpers.GetMovies;
import training.elfeky.com.gridviewexample.customs.ImageAdapter;
import training.elfeky.com.gridviewexample.helpers.JsonUtils;
import training.elfeky.com.gridviewexample.main.MovieDetailActivity;
import training.elfeky.com.gridviewexample.main.MovieDetails;
import training.elfeky.com.gridviewexample.R;



public class MobileMainFragment extends FragmentInterface {

    public GridView gridview;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public List<MovieDetails> movies;
    public static  boolean isInFavourite = false;
    int pos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mobile, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        editor.putString(JsonUtils.SORT, JsonUtils.POPULAR);
        editor.commit();
        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                pos = position;
               MovieDetailFragment detailFragment= (MovieDetailFragment) getActivity().getFragmentManager().findFragmentById(R.id.detailsFragment);
                if(detailFragment != null && detailFragment.isVisible()){
                    detailFragment.setMovieDetails(movies.get(position),false);
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie",movies.get(position));
                    bundle.putBoolean("fav",isInFavourite);
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey(MovieDetailStatics.FAVOURITE))
                isInFavourite = savedInstanceState.getBoolean(MovieDetailStatics.FAVOURITE);
            if (savedInstanceState.containsKey(MovieDetailStatics.POS_KEY))
                pos = savedInstanceState.getInt(MovieDetailStatics.POS_KEY);
        }

        return  view;
    }



    @Override
    public void update()
    {

        isInFavourite = false;
        String sort = sharedPreferences.getString(JsonUtils.SORT, "");

        if(isNetworkAvailable()) {
            GetMovies getMovies = new GetMovies(this, progressDialog);
            getMovies.execute(urlBuilder(JsonUtils.MOVIES_BASE_URL + sort));
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            gridview.setAdapter(new ImageAdapter(getActivity(),new ArrayList<MovieDetails>()));
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getOverflowMenu();
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh)
        {
            if(!isInFavourite)
            update();
            return true;
        }
        else if(id == R.id.action_favourite)
        {
            isInFavourite = true;
            getFavouriteMovies();
            //isInFavourite = true;
            return true;
        }
        else if(id == R.id.action_top_rated)
        {
            editor.putString(JsonUtils.SORT,JsonUtils.TOP_RATED);
            editor.commit();
            pos = 0;
            update();
            return true;
        }
        else if(id == R.id.action_popular)
        {
            editor.putString(JsonUtils.SORT,JsonUtils.POPULAR);
            editor.commit();
            pos = 0;
            update();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getFavouriteMovies()
    {
        DbHandler db = new DbHandler(getActivity());
        movies = new ArrayList<>();
        movies = db.getmovie();
        ImageAdapter adapter = new ImageAdapter(getActivity(), movies);
        gridview.setAdapter(adapter);

        MovieDetailFragment movieDetailFragment = (MovieDetailFragment)getActivity().getFragmentManager()
                .findFragmentById(R.id.detailsFragment);
        if(movieDetailFragment!=null&&movieDetailFragment.isVisible()) {
            movieDetailFragment.setMovieDetails(movies.get(pos),false);
        }

        if(movies.size()==0) {
            Toast.makeText(getActivity(),"No Favourite Movies Yet",Toast.LENGTH_SHORT).show();
        }
    }
    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
        inflater.inflate(R.menu.favourite, menu);
    }
    @Override
    public  void getDataFromJson(String forecastJsonStr)
            throws JSONException {

        try {
            JSONObject jsonRootObject = new JSONObject(forecastJsonStr);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray(JsonUtils.RESULTS);

            //Iterate the jsonArray and print the info of JSONObjects
            movies  = new ArrayList<MovieDetails>();
            for(int i=0; i <jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MovieDetails movieDetails = new MovieDetails();
                String name = JsonUtils.IMAGE_LINK+jsonObject.optString(JsonUtils.POSTER_PATH).toString();
                movieDetails.setPosterLink(name);
                movieDetails.setOverview(jsonObject.optString(JsonUtils.OVERVIEW).toString());

                movieDetails.setVoteAverage(jsonObject.optString(JsonUtils.VOTE_AVERAGE).toString());

                String [] dateParts = jsonObject.optString(JsonUtils.RELEASE_DATE).toString().split("-");
                movieDetails.setReleaseDate(dateParts[0]);
                movieDetails.setTitle(jsonObject.optString(JsonUtils.ORIGINAL_TITLE).toString());
                movieDetails.setId(jsonObject.optString(JsonUtils.ID).toString());
                movies.add(i,movieDetails);

            }
        } catch (JSONException e) {e.printStackTrace();}
        //return movies;
        ImageAdapter adapter = new ImageAdapter(getActivity(), movies);
        gridview.setAdapter(adapter);
        MovieDetailFragment movieDetailFragment = (MovieDetailFragment)getActivity().getFragmentManager()
                .findFragmentById(R.id.detailsFragment);
        if(movieDetailFragment!=null&&movieDetailFragment.isVisible())
            movieDetailFragment.setMovieDetails(movies.get(pos),false);
    }

    @Override
    protected String urlBuilder(String baseUrl)
    {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(JsonUtils.APPID_PARAM,JsonUtils.APP_ID)
                .build();
        return builtUri.toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isInFavourite)
        {
            getFavouriteMovies();
        }
        else
        {
            update();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInFavourite = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MovieDetailStatics.FAVOURITE, isInFavourite);
        outState.putInt(MovieDetailStatics.POS_KEY, pos);
    }
}
