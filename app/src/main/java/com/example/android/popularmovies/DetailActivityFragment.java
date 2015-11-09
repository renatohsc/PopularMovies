package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public static final String ID_KEY = "id";
    public static final String TITLE_KEY = "titleMovie";
    public static final String POSTER_KEY = "posterMovie";
    public static final String RELEASE_KEY = "releaseDate";
    public static final String RATING_KEY = "ratingMovie";
    public static final String PLOT_KEY = "plotMovie";

    private static final String TRAILERS_KEY = "trailers";
    private static final String REVIEWS_KEY = "reviews";




    int id_movie;
    String title_movie;
    String poster_movie ;
    String release_movie;
    String rating_movie;
    String plot_movie;

    private ViewGroup trailersLayout;
    private ViewGroup reviewsLayout;
    private String[] movieTrailers;
    private boolean isSaveInstance;


    private ArrayList<ReviewMinutia> ReviewList;

    ReviewMinutia[] ReviewMinutias;




    private ShareActionProvider mShareActionProvider;


    public DetailActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ID_KEY)) {
            id_movie = getArguments().getInt(ID_KEY);
            title_movie = getArguments().getString(TITLE_KEY);
            poster_movie = getArguments().getString(POSTER_KEY);

            release_movie = getArguments().getString(RELEASE_KEY);
            rating_movie = getArguments().getString(RATING_KEY);
            plot_movie =getArguments().getString(PLOT_KEY);

        }
        if(savedInstanceState == null || !savedInstanceState.containsKey(REVIEWS_KEY) ||
                !savedInstanceState.containsKey(TRAILERS_KEY)) {
            isSaveInstance = false;
            new FetchTrailersTask().execute(id_movie);

        }

        else {
            ReviewList = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
            movieTrailers = savedInstanceState.getStringArray(TRAILERS_KEY);
            if(movieTrailers != null && ReviewList != null) {
                Log.d(LOG_TAG, "movieTrailers retrieved: " + movieTrailers.length + " and reviews : " +
                        ReviewList.size());
            }
            isSaveInstance = true;
        }




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        String titleMovie = getArguments().getString(TITLE_KEY);
        String posterMovie = getArguments().getString(POSTER_KEY);
        String releaseDate = getArguments().getString(RELEASE_KEY);

        String releaseYear = "";

        releaseYear = releaseDate.substring( 0, 4 );

        String ratingMovie = getArguments().getString(RATING_KEY) + "/10";
        String plotMovie = getArguments().getString(PLOT_KEY);

        TextView titleView = (TextView) rootView.findViewById(R.id.titleMovie);
        TextView dateView = (TextView) rootView.findViewById(R.id.releaseDate);
        TextView ratingView = (TextView) rootView.findViewById(R.id.ratingMovie);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.posterMovie);
        TextView plotTextView = (TextView) rootView.findViewById(R.id.plotMovie);

        final FloatingActionButton favouriteButton = (FloatingActionButton) rootView.findViewById(R.id.favButton);


        trailersLayout = (ViewGroup) rootView.findViewById(R.id.movie_trailers);
        reviewsLayout = (ViewGroup) rootView.findViewById(R.id.movie_reviews);
        Button showReviews = (Button) rootView.findViewById(R.id.show_reviews);
        final ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.movieScrolLView);


        String sizeImage = getActivity().getResources().getString(R.string.size_image);
        //String sizeImage = "w185";
        if(posterMovie != null) {
        String posterUrl = "http://image.tmdb.org/t/p/" + sizeImage + "/" + posterMovie;
        // Log.v(LOG_TAG, "poster url : " + posterUrl);
        Picasso.with(getActivity())
        .load(posterUrl)
        .resize(400, 600)
//        .fit()
        .into(posterView);
        } else {

        posterView.setImageResource(R.mipmap.ic_launcher);
        }




        titleView.setText(titleMovie);
        dateView.setText(releaseYear);
        ratingView.setText(ratingMovie);
        plotTextView.setText(plotMovie);

        checkFavourite(id_movie, favouriteButton);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFavourite(id_movie, favouriteButton);

            }

        });

        if(isSaveInstance) {
            getMovieTrailers(movieTrailers);
            getMovieReviews(ReviewList);
        }
        showReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewsLayout.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);

                    }
                });
            }
        });

        setHasOptionsMenu(true);
        if (movieTrailers != null && movieTrailers.length != 0 && mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent(movieTrailers[0]));
        }

        return rootView;

        }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

//        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
//        if (mForecast != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void checkFavourite(Integer idmovie,FloatingActionButton favouriteButton) {
        String id = String.valueOf(idmovie);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_starlight);
        drawable = DrawableCompat.wrap(drawable);

    }
    private void handleFavourite(int idmovie, FloatingActionButton favouriteButton) {


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(TRAILERS_KEY, movieTrailers);
        outState.putParcelableArrayList(REVIEWS_KEY, ReviewList);
    }

    private Intent createShareTrailerIntent(String source) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + source);
        return shareIntent;
    }

    private void getMovieTrailers(final String[] trailers) {

        if(trailers == null )
            return;
        if (trailers != null && trailers.length != 0 && mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent(trailers[0]));
        }
        final ViewGroup viewGroup = trailersLayout;

        // Remove all existing trailers (everything but first child, which is the header)
        for (int i = viewGroup.getChildCount() - 1; i >= 1; i--) {
            viewGroup.removeViewAt(i);
        }

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        boolean hasTrailers = true;

        for (int i= 0; i < trailers.length; i++) {

            final View trailerView = inflater
                    .inflate(R.layout.trailer_item_movie, viewGroup, false);
            final TextView trailerTitle = (TextView) trailerView
                    .findViewById(R.id.trailerTitle);
            final ImageButton trailerPlay = (ImageButton) trailerView
                    .findViewById(R.id.trailerPlay);
            hasTrailers = true;
            int iaux = i + 1;
            trailerTitle.setText(String.format(Locale.US, "Trailer %d", iaux));
            final String source = trailers[i];
            trailerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(source);
                    Log.d(LOG_TAG, "clicked on source :" + source);
                }
            });
            viewGroup.addView(trailerView);
        }

        viewGroup.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);

    }

    private void getMovieReviews(ArrayList<ReviewMinutia> reviews) {

        Log.v(LOG_TAG, "movie re88888 : " + reviews);

        if(reviews == null || reviews.size() == 0 )
            return;

        Log.v(LOG_TAG, "movie re9999 : " + reviews);
        final ViewGroup viewGroup = reviewsLayout;

        for (int i = viewGroup.getChildCount(); i > 0; i--) {
            viewGroup.removeViewAt(i);
        }

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        for (int i= 0; i < reviews.size(); i++) {
            ReviewMinutia review = reviews.get(i);
            Log.v(LOG_TAG, "movie re3424 : " + review);
            final View reviewView = inflater
                    .inflate(R.layout.review_item_movie, viewGroup, false);
            final TextView authorView = (TextView) reviewView
                    .findViewById(R.id.reviewAuthor);
            final TextView contentView = (TextView) reviewView
                    .findViewById(R.id.reviewContent);
            final String author =review.authorReview;
            final String content =review.contentReview;
            authorView.setText(author);
            contentView.setText(content);

            Log.v(LOG_TAG, "movie re1111 : " + author);
            Log.v(LOG_TAG, "movie re6222 : " + content);

            viewGroup.addView(reviewView);
        }

    }
    private void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            getActivity().startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            getActivity().startActivity(intent);
        }
    }

    public class FetchTrailersTask extends AsyncTask<Integer,Void,String[]> {
        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();


        private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";


        private String[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {


            // These are the names of the JSON objects that need to be extracted.
            final String TMD_TRAILERS = "trailers";
            final String TMD_TRAILER_LIST = "youtube";
            final String TMD_REVIEWS = "reviews";
            final String TMD_REVIEWS_RESULT = "results";
            final String TMD_TRAILER_SOURCE = "source";
            final String TMD_REVIEW_AUTHOR = "author";
            final String TMD_REVIEWS_CONTENT = "content";



            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONObject trailerObj = movieJson.getJSONObject(TMD_TRAILERS);
            JSONArray youtubeArray = trailerObj.getJSONArray(TMD_TRAILER_LIST);

            JSONObject reviewsObj = movieJson.getJSONObject(TMD_REVIEWS);
            JSONArray reviewArray = reviewsObj.getJSONArray(TMD_REVIEWS_RESULT);


            String[] resultStrs = new String[youtubeArray.length()];
            for(int i = 0; i < youtubeArray.length(); i++) {
                int k = 0;

                JSONObject trailerData = youtubeArray.getJSONObject(i);
                resultStrs[i] = trailerData.getString(TMD_TRAILER_SOURCE);

                Log.d(LOG_TAG, "Movie data :" + i + "\n" + resultStrs[i]);
            }
            if(reviewArray!= null && reviewArray.length() > 0) {
                ReviewMinutias = new ReviewMinutia[reviewArray.length()];
                Log.d(LOG_TAG,"movie reviews : " + reviewArray.length());
                for(int j = 0; j < reviewArray.length(); j++) {
                    JSONObject movieReviewData = reviewArray.getJSONObject(j);
                    ReviewMinutias[j] = new ReviewMinutia(
                            movieReviewData.getString(TMD_REVIEW_AUTHOR),
                            movieReviewData.getString(TMD_REVIEWS_CONTENT)

                    );
                    Log.v(LOG_TAG, "movie review radical : " + ReviewMinutias[j]);
                }
                ReviewList = new ArrayList<ReviewMinutia>(Arrays.asList(ReviewMinutias));
            }
            return resultStrs;
        }
        @Override
        protected String[] doInBackground(Integer... params) {

            if(params.length == 0)
                return  null;

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String movieJsonStr = null;



            try {


                final String MOVIES_BASE_URL = BASE_URL + params[0] + "?";
                final String API_PARAM = "api_key";
                final String APPEND_PARAMS = "append_to_response";
                final String APPEND_TRAILERS_REVIEWS = "trailers,reviews";
                String api_key = getActivity().getResources().getString(R.string.api_key);

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, api_key)
                        .appendQueryParameter(APPEND_PARAMS,APPEND_TRAILERS_REVIEWS)
                        .build();
                URL url = new URL(builtUri.toString());

                Log.d(LOG_TAG,"movies Uri  : " + builtUri.toString());

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = bufferedReader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                if(buffer.length() == 0) {
                    return null;
                }

                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "IO Error " + e);
                return null;
            } finally {
                if(httpURLConnection != null)
                    httpURLConnection.disconnect();
                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }
            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final String[] result) {
            if(result != null) {
                movieTrailers = result;
                getMovieTrailers(result);
                getMovieReviews(ReviewList);
            }

        }
    }

}







//String titleMovie = getIntent().getExtras().getString(TITLE_KEY);
//String posterMovie = getIntent().getExtras().getString(POSTER_KEY);
//String releaseDate = getIntent().getExtras().getString(RELEASE_KEY);
//
//String releaseYear = "";
//
//releaseYear = releaseDate.substring( 0, 4 );
//
//        String ratingMovie = getIntent().getExtras().getString(RATING_KEY) + "/10";
//        String plotMovie = getIntent().getExtras().getString(PLOT_KEY);
//
//        TextView titleView = (TextView) findViewById(R.id.titleMovie);
//        TextView dateView = (TextView) findViewById(R.id.releaseDate);
//        TextView ratingView = (TextView) findViewById(R.id.ratingMovie);
//        ImageView posterView = (ImageView) findViewById(R.id.posterMovie);
//        TextView plotTextView = (TextView) findViewById(R.id.plotMovie);
//
//
//
//
//        String sizeImage = getApplicationContext().getResources().getString(R.string.size_image);
//        //String sizeImage = "w185";
//        if(posterMovie != null) {
//        String posterUrl = "http://image.tmdb.org/t/p/" + sizeImage + "/" + posterMovie;
//        // Log.v(LOG_TAG, "poster url : " + posterUrl);
//        Picasso.with(this)
//        .load(posterUrl)
//        .resize(500, 750)
//        //    .fit()
//        .into(posterView);
//        } else {
//
//        posterView.setImageResource(R.mipmap.ic_launcher);
//        }
//
//
//
//
//        titleView.setText(titleMovie);
//        dateView.setText(releaseYear);
//        ratingView.setText(ratingMovie);
//        plotTextView.setText(plotMovie);
//
//        }
