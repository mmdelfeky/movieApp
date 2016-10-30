package training.elfeky.com.gridviewexample.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import training.elfeky.com.gridviewexample.customs.Review;
import training.elfeky.com.gridviewexample.customs.ReviewAdapter;
import training.elfeky.com.gridviewexample.data.DbHandler;
import training.elfeky.com.gridviewexample.helpers.GetMovies;
import training.elfeky.com.gridviewexample.helpers.JsonUtils;
import training.elfeky.com.gridviewexample.helpers.MovieDetailStatics;
import training.elfeky.com.gridviewexample.main.MainActivity;
import training.elfeky.com.gridviewexample.main.MovieDetailActivity;
import training.elfeky.com.gridviewexample.main.MovieDetails;
import training.elfeky.com.gridviewexample.customs.NonScrollListView;
import training.elfeky.com.gridviewexample.R;


public class MovieDetailFragment extends FragmentInterface {

    ImageView posterLink;
    TextView title,overview ,voteAverage,releaseDate,time,trailerNotFound,trailer,review;
    TextView reviewNotFound;
    List<String>  trailerNames,trailerSources;
    List<Review> reviews;
    //SharedPreferences  sharedPreferences ;
    String id;
    Button makeFavourite;
    MovieDetails movieDetails;
    NonScrollListView reviewsList,trailerListView;
    DbHandler db;
    String  forecastJsonStr;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(savedInstanceState!=null&&savedInstanceState.containsKey(MovieDetailStatics.ENTERED))
        {
            movieDetails = (MovieDetails) savedInstanceState.getSerializable(MovieDetailStatics.ENTERED);
        }
        title           = (TextView)  view.findViewById(R.id.original_title);
        posterLink      = (ImageView) view.findViewById(R.id.SingleView);
        overview        = (TextView)  view.findViewById(R.id.overview);
        voteAverage     = (TextView)  view.findViewById(R.id.vote_average);
        releaseDate     = (TextView)  view.findViewById(R.id.releasedate);
        time            = (TextView)  view.findViewById(R.id.time);
        trailerNotFound = (TextView)  view.findViewById(R.id.trailer_not_found);
        reviewNotFound  = (TextView)  view.findViewById(R.id.review_not_found);
        makeFavourite   = (Button)    view.findViewById(R.id.favourite);
        trailer           = (TextView)  view.findViewById(R.id.trailer);
        review           = (TextView)  view.findViewById(R.id.review);

        //lists
        trailerNames    = new ArrayList<>();
        trailerSources  = new ArrayList<>();
        db              = new DbHandler(getActivity());
        //movieDetails    = new MovieDetails();
        trailerListView = (NonScrollListView) view.findViewById(R.id.trailers_list);
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(JsonUtils.YOUTUBE_LINK + trailerSources.get(position)));
                intent = Intent.createChooser(intent, "Choose");
                getActivity().startActivityForResult(intent, 1);
            }
        });

        //reviews list
        reviewsList = (NonScrollListView) view.findViewById(R.id.reviews_list);
        reviews = new ArrayList<Review>();
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        MobileMainFragment mobileMainFragment = (MobileMainFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.fragment2);
        if (MobileMainFragment.isInFavourite&&getActivity()instanceof MainActivity)
           setMovieDetails(movieDetails,false);

        return view;
    }

    public void setMovieDetails(MovieDetails movieDetail,boolean isFavourite)
    {
        this.movieDetails = movieDetail;
        Picasso.with(getActivity()).load(movieDetails.getPosterLink())
                .into(posterLink);
        title.setBackgroundColor(Color.parseColor("#ff08b8af"));
        trailer.setBackgroundColor(Color.parseColor("#ff08b8af"));
        review.setBackgroundColor(Color.parseColor("#ff08b8af"));
        title.setText(movieDetails.getTitle());
        trailer.setText("Trailers :");
        review.setText("Reviews :");

        releaseDate.setText(movieDetails.getReleaseDate());
        voteAverage.setText(movieDetails.getVoteAverage());
        overview.setText(movieDetails.getOverview());
        id = "";
        id = movieDetails.getId();

        if(db.search(id) == 1){
            makeFavourite.setBackgroundColor(Color.parseColor("#FFD700"));
            makeFavourite.setText("It is a\nfavourite");
    }
        else{
            makeFavourite.setBackgroundColor(Color.parseColor("#00aadd"));
            makeFavourite.setText("make as \nfavourite");
    }
        makeFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = db.search(id);
                if (flag == 0) {
                    movieDetails.setExtra(forecastJsonStr);
                    db.insert(movieDetails);
                    makeFavourite.setBackgroundColor(Color.parseColor("#FFD700"));
                    makeFavourite.setText("It is a\nfavourite");
                } else if (flag == 1) {
                    makeFavourite.setBackgroundColor(Color.parseColor("#2299ff"));
                    makeFavourite.setText("make as \nfavourite");
                    db.deleteName(movieDetails.getId());
                }
                MobileMainFragment mobileMainFragment = (MobileMainFragment) getActivity().getFragmentManager()

                        .findFragmentById(R.id.fragment2);
                if (mobileMainFragment != null && mobileMainFragment.isVisible()
                        && MobileMainFragment.isInFavourite)
                    mobileMainFragment.getFavouriteMovies();
            }
        });
        if(MobileMainFragment.isInFavourite||((getActivity() instanceof  MovieDetailActivity)&&isFavourite))
        {
            try {
                getDataFromJson(movieDetail.getExtra());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            update();
        }

    }
        @Override
    protected String urlBuilder(String baseUrl)
    {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(JsonUtils.APPID_PARAM,JsonUtils.APP_ID)
                .build();
        String url = builtUri.toString();
        return url+JsonUtils.TRAILER_URL+"="+JsonUtils.TRAILER;
    }

    @Override
    public  void getDataFromJson(String forecastJsonStr)
            throws JSONException {
        try {
            JSONObject jsonRootObject = new JSONObject(forecastJsonStr);


            //Get the instance of JSONArray that contains JSONObjects
            int runtime = jsonRootObject.getInt("runtime");
            time.setText(runtime + "min");

            JSONObject jsonArray = jsonRootObject.getJSONObject("trailers");

            JSONArray youtube = jsonArray.optJSONArray("youtube");
            //get trailers info
            trailerNames.clear();
            trailerSources.clear();
            if(youtube.length()== 0)
                trailerNotFound.setText("Trailers Not Found");
            else {
                for (int i = 0; i < youtube.length(); i++) {
                    JSONObject jsonObject = youtube.getJSONObject(i);
                    String source = jsonObject.optString("source").toString();
                    String name = jsonObject.optString("name").toString();
                    trailerSources.add(i, source);
                    trailerNames.add(i, name);
                }
                try {
                    ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.trailer, R.id.name, trailerNames);
                    trailerListView.setAdapter(trailerAdapter);
                } catch (Exception e) {
                }
            }

            //reviws info
            JSONObject jsonReviews= jsonRootObject.getJSONObject("reviews");
            JSONArray reviewsResult = jsonReviews.optJSONArray("results");
            if(reviewsResult.length()>0) {
                for (int i = 0; i < reviewsResult.length(); i++) {
                    JSONObject resultsJSONObject = reviewsResult.getJSONObject(i);

                    String content = resultsJSONObject.optString("content").toString();
                    String author = resultsJSONObject.optString("author").toString();
                    Review review = new Review(author, content);
                    reviews.add(i, review);
                }
                ReviewAdapter reviewAdapter = new ReviewAdapter(reviews,getActivity());
                reviewsList.setAdapter(reviewAdapter);
            }
            else {
                reviewNotFound.setText("Reviews Not Found");
            }

        } catch (JSONException e) {e.printStackTrace();}
        this.forecastJsonStr = forecastJsonStr;
    }

    @Override
    public void update() {

        if(isNetworkAvailable()) {
            GetMovies getMovies = new GetMovies(this,progressDialog);
            getMovies.execute(urlBuilder(JsonUtils.MOVIE_BASE_URL + id));
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MovieDetailStatics.ENTERED,movieDetails);
    }
}
