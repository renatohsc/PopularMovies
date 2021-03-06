package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

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


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final String MOST_POP = "popularity.desc";
    private static final String HIGH_RATED = "vote_average.desc";

    private static final String MAINFRAGMENT_TAG = "MATAG";

    public final static String ARG_ORDER_TYPE = "ARG_ORDER_TYPE";

    public final static String TYPE_SORT = "0";

    public final static String TYPE_POP = "0";
    public final static String TYPE_RATED = "1";
    public final static String  TYPE_FAV = "2";


    private GridView gridMovie;
    public  ArrayList<MovieMinutia> movieList;

    MovieMinutia[] movies;

    // Loadind progress
    private static final int PROGRESS = 0x1;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private TextView textProgress;

    private Handler mHandler = new Handler();

    private Callback mCallback;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(int position, int id, String title, String poster,
                                   String release, String rating, String plot);

        void onFirstItem(int position, int id, String title, String poster,
                               String release, String rating, String plot);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
     //   updateOrder(TYPE_POP);
        Log.v(LOG_TAG, "Maldicao executavel 111");
    }
    public MainActivityFragment() {
        Log.v(LOG_TAG, "Maldicao executavel 70");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MAINFRAGMENT_TAG)) {
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_loading);
        Log.v(LOG_TAG, "Maldicao executavel 555");

        mProgress.setVisibility(View.VISIBLE);

        Log.v(LOG_TAG, "Maldicao executavel 566");
        textProgress = (TextView) rootView.findViewById(R.id.text_loading);

        Log.v(LOG_TAG, "Maldicao executavel 577");

        textProgress.setVisibility(View.VISIBLE);

        Log.v(LOG_TAG, "Maldicao executavel 877");

        gridMovie = (GridView) rootView.findViewById(R.id.gridview_movie);

        gridMovie.setVisibility(View.GONE);
        Log.v(LOG_TAG, "Maldicao executavel 899");



            SharedPreferences sharedPrefs =  getActivity().getSharedPreferences(getString(R.string.arg_order_pref), Context.MODE_PRIVATE);


            Log.v(LOG_TAG, "Maldicachorro executavel 66u");
            String sortOrder = "0";
            if (sharedPrefs != null) {
                Log.v(LOG_TAG, "Maldicachorro executavel 888");
                sortOrder  = sharedPrefs.getString(
                        getActivity().getString(R.string.arg_order_key), getActivity().getString(R.string.pref_order_pop));

            }

            Log.v(LOG_TAG, "Maldicavalo executavel 777" + sortOrder);

            updateOrder(sortOrder);


            Log.v(LOG_TAG, "Maldicavalo executavel 888" + sortOrder);

//        int sortOrder  = sharedPrefs.getInt(
//                ARG_ORDER_TYPE, TYPE_POP);
//
//            Log.v(LOG_TAG, "Maldicachorro executavel 666" + getString(sortOrder));


           if (sortOrder == TYPE_RATED) {

               Log.v(LOG_TAG, "Maldicavalo execu66" + sortOrder + TYPE_RATED + "w");
               updateMovies(HIGH_RATED);

            }
            else {
               Log.v(LOG_TAG, "Maldicavalo execu644" + sortOrder + TYPE_RATED + "z");
               updateMovies(MOST_POP);

           }
        }

        else {
            movieList = savedInstanceState.getParcelableArrayList(MAINFRAGMENT_TAG);
            Log.d(LOG_TAG, "MovieList retrieved with size : " + movieList.size());
        }


        //minutiaAdapter = new MovieMinutiaAdapter(getActivity(), Arrays.asList(movieList));

        // Get a reference to the GridView, and attach this adapter to it.

        //  gridView.setAdapter(minutiaAdapter);

        setHasOptionsMenu(true);


        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by) {

        }
        else {


            if (id == R.id.action_sort_by_high_rated) {
                updateOrder(TYPE_RATED);
                updateMovies(HIGH_RATED);
            } else {
                updateOrder(TYPE_POP);
                updateMovies(MOST_POP);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateOrder(String type){
//        SharedPreferences sharedPrefs =
//                     PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getString(R.string.arg_order_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ARG_ORDER_TYPE, type);
        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MAINFRAGMENT_TAG, movieList);
    }

    private void updateMovies(String sortCriteria) {


       // Log.v(LOG_TAG, "Maldicao executavel 2");



//        SharedPreferences sharedPrefs =
//                PreferenceManager.getDefaultSharedPreferences(getActivity());

//        String sortOrder  = sharedPrefs.getString(
//                getString(R.string.pref_sort_key),
//                getString(R.string.pref_sort_pop));

        String sortLabel = " " ;


        if (sortCriteria == HIGH_RATED) {
            sortLabel = getString(R.string.pref_sort_label_highest);
            Log.v(LOG_TAG, "Maldicao executavel 10");
        }
            else {
            Log.v(LOG_TAG, "Maldicao executavel 20");
            sortLabel = getString(R.string.pref_sort_label_popular);
        }


        getActivity().setTitle(sortLabel);

         Log.v(LOG_TAG, "Maldicao executavel 1" + sortCriteria);

         Log.v(LOG_TAG, "Maldicao executavel 2" + getString(R.string.pref_sort_high));

         Log.v(LOG_TAG, "Maldicao executavel 3" + sortLabel);






        Log.v(LOG_TAG, "Maldicao executavel 98877");
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus += 1;

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                            textProgress.setText(mProgressStatus+"/"+mProgress.getMax());

                        }
                    });

                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }




                }
            }
        }).start();
        Log.v(LOG_TAG, "Maldicao executavel 40");

        FetchMovieTask moviesTask = new FetchMovieTask();
        moviesTask.execute(sortCriteria);
        Log.v(LOG_TAG, "Maldicao executavel 50");



    }


    @Override
    public void onStart() {

        super.onStart();
        Log.v(LOG_TAG, "Maldicao executavel 30");
       // Log.v(LOG_TAG, "Maldicao executavel 1");


    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[][]> {


        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();




        private String[][] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {
            //  Log.v(LOG_TAG, "Maldicao executavel 5");


            // These are the names of the JSON objects that need to be extracted.
            final String TMD_ID = "id";
            final String TMD_LIST = "results";
            final String TMD_TITLE = "original_title";
            final String TMD_POSTER = "poster_path";
            final String TMD_PLOT = "overview";
            final String TMD_RELEASE = "release_date";
            final String TMD_RATING = "vote_average";

            boolean favMovies = false;



            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMD_LIST);



            String[][] resultStrs = new String[movieArray.length()][6];
            movies = new MovieMinutia[movieArray.length()];
            for(int i = 0; i < movieArray.length(); i++) {
                int k = 0;



                // Get the JSON object representing the day
                JSONObject movieData = movieArray.getJSONObject(i);
                movies[i] = new MovieMinutia(
                        movieData.getInt(TMD_ID),
                        movieData.getString(TMD_TITLE),
                        movieData.getString(TMD_POSTER),
                        movieData.getString(TMD_RELEASE),
                        movieData.getString(TMD_RATING),
                        movieData.getString(TMD_PLOT));

                resultStrs[i][k+5] = Integer.toString(movieData.getInt(TMD_ID));
                resultStrs[i][k] =   movieData.getString(TMD_TITLE);
                resultStrs[i][k+1] = movieData.getString(TMD_POSTER);
                resultStrs[i][k+3] = movieData.getString(TMD_RELEASE);
                resultStrs[i][k+4] = movieData.getString(TMD_RATING);
                resultStrs[i][k+2] = movieData.getString(TMD_PLOT);



            }
            movieList = new ArrayList<MovieMinutia>(Arrays.asList(movies));
            return resultStrs;
        }



        @Override
        protected String[][] doInBackground(String... params) {

            // no params, no fetching

            if (params.length == 0) {
                return null;
            }


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String sortCrit = params[0];
            String sortCountry = "US";
            String sortCount = "10";



            String api_key = getActivity().getResources().getString(R.string.api_key);

            // Log.v(LOG_TAG, "Maldicao executavel 6" + sortCrit);


            try {
                // Construct the URL for The Movie Database query
                // Possible parameters are avaiable at TMD's API page, at
                // https://www.themoviedb.org/documentation/api

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                final String COUNTRY_PARAM = "certification_country";

                final String VOTECOUNT_PARAM = "vote_count.gte";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(COUNTRY_PARAM, sortCountry)
                        .appendQueryParameter(VOTECOUNT_PARAM, sortCount)
                        .appendQueryParameter(SORT_PARAM, sortCrit)
                        .appendQueryParameter(API_PARAM, api_key)
                        .build();


                URL url = new URL(builtUri.toString());

                //  Log.v(LOG_TAG, "Built URI " + builtUri.toString());


                // Create the request to The Movie Database, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

                //VERBOSE LOG
                //    Log.v(LOG_TAG, "Forecast Json String" + movieJsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
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
            // This will only happen if there was an error getting or parsing the forecast.
            return null;

        }
        protected void onPostExecute(final String[][] result) {
            //  Log.v(LOG_TAG, "Maldicao executavel 8");

            if (result != null) {

                final MovieMinutiaAdapter mMovieMinutiaAdapter = new MovieMinutiaAdapter(getActivity(),movieList);
                mProgress.setVisibility(View.GONE);
                textProgress.setVisibility(View.GONE);
                gridMovie.setVisibility(View.VISIBLE);
                gridMovie.setAdapter(mMovieMinutiaAdapter);



                gridMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        //       Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                        MovieMinutia movie = movieList.get(position);




                        Intent sendIntent = new Intent(getActivity(), DetailActivity.class).
                                putExtra(DetailActivityFragment.ID_KEY, movie.idMovie).
                                putExtra(DetailActivityFragment.TITLE_KEY, movie.titleMovie).
                                putExtra(DetailActivityFragment.POSTER_KEY, movie.posterMovie).
                                putExtra(DetailActivityFragment.RELEASE_KEY, movie.releaseDate).
                                putExtra(DetailActivityFragment.RATING_KEY, movie.ratingMovie).
                                putExtra(DetailActivityFragment.PLOT_KEY, movie.plotMovie);
                                startActivity(sendIntent);




                    }
                });






            }

        }
    }

}









