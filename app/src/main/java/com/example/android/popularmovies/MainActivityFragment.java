package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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


    private GridView gridMovie;
    public  ArrayList<MovieMinutia> movieList;

    MovieMinutia[] movies;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public MainActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        //minutiaAdapter = new MovieMinutiaAdapter(getActivity(), Arrays.asList(movieList));

        // Get a reference to the GridView, and attach this adapter to it.
        gridMovie = (GridView) rootView.findViewById(R.id.gridview_movie);
      //  gridView.setAdapter(minutiaAdapter);



        return rootView;

    }


    private void updateMovies() {


        Log.v(LOG_TAG, "Maldicao executavel 2");



        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPrefs.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_pop));

        Log.v(LOG_TAG, "Maldicao executavel 3" + sortOrder);




        fetchMoviesTask moviesTask = new fetchMoviesTask();
        moviesTask.execute(sortOrder);




    }

    @Override
    public void onStart() {

        super.onStart();

        Log.v(LOG_TAG, "Maldicao executavel 1");

        updateMovies();

    }


    public class fetchMoviesTask extends AsyncTask<String, Void, String[][]>

    {
        private final String LOG_TAG = fetchMoviesTask.class.getSimpleName();




        private String[][] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {
            Log.v(LOG_TAG, "Maldicao executavel 5");


            // These are the names of the JSON objects that need to be extracted.
            final String TMD_LIST = "results";
            final String TMD_TITLE = "original_title";
            final String TMD_POSTER = "poster_path";
            final String TMD_PLOT = "overview";
            final String TMD_RELEASE = "release_date";
            final String TMD_RATING = "vote_average";




            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMD_LIST);



            String[][] resultStrs = new String[movieArray.length()][5];
            movies = new MovieMinutia[movieArray.length()];
            for(int i = 0; i < movieArray.length(); i++) {
                int k = 0;



                // Get the JSON object representing the day
                JSONObject movieData = movieArray.getJSONObject(i);
                movies[i] = new MovieMinutia(
                        movieData.getString(TMD_TITLE),
                        movieData.getString(TMD_POSTER),
                        movieData.getString(TMD_RELEASE),
                        movieData.getString(TMD_RATING),
                        movieData.getString(TMD_PLOT));
                resultStrs[i][k] = movieData.getString(TMD_TITLE);
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

            String api_key = getActivity().getResources().getString(R.string.api_key);

            Log.v(LOG_TAG, "Maldicao executavel 6" + sortCrit);


            try {
                // Construct the URL for The Movie Database query
                // Possible parameters are avaiable at TMD's API page, at
                // https://www.themoviedb.org/documentation/api

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                final String COUNTRY_PARAM = "certification_country";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(COUNTRY_PARAM, sortCountry)
                        .appendQueryParameter(SORT_PARAM, sortCrit)
                        .appendQueryParameter(API_PARAM, api_key)
                        .build();


                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());


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
                 Log.v(LOG_TAG, "Forecast Json String" + movieJsonStr);


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
            Log.v(LOG_TAG, "Maldicao executavel 8");

            if (result != null) {

                final MovieMinutiaAdapter mMovieMinutiaAdapter = new MovieMinutiaAdapter(getActivity(),movieList);
                gridMovie.setAdapter(mMovieMinutiaAdapter);



                gridMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        //       Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                        MovieMinutia movie = movieList.get(position);

                        Intent sendIntent = new Intent(getActivity(), DetailActivity.class).
                                 putExtra(DetailActivity.TITLE_KEY, movie.titleMovie).
                                 putExtra(DetailActivity.POSTER_KEY, movie.posterMovie).
                                 putExtra(DetailActivity.RELEASE_KEY, movie.releaseDate).
                                 putExtra(DetailActivity.RATING_KEY, movie.ratingMovie).
                                 putExtra(DetailActivity.PLOT_KEY, movie.plotMovie);
                                 startActivity(sendIntent);





                    }
                });






        }

    }
}


}





