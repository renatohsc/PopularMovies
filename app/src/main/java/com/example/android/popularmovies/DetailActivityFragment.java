package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public static final String ID_KEY = "id";
    public static final String TITLE_KEY = "titleMovie";
    public static final String POSTER_KEY = "posterMovie";
    public static final String RELEASE_KEY = "releaseDate";
    public static final String RATING_KEY = "ratingMovie";
    public static final String PLOT_KEY = "plotMovie";

    public DetailActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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




        String sizeImage = getActivity().getResources().getString(R.string.size_image);
        //String sizeImage = "w185";
        if(posterMovie != null) {
        String posterUrl = "http://image.tmdb.org/t/p/" + sizeImage + "/" + posterMovie;
        // Log.v(LOG_TAG, "poster url : " + posterUrl);
        Picasso.with(getActivity())
        .load(posterUrl)
//        .resize(500, 750)
        //    .fit()
        .into(posterView);
        } else {

        posterView.setImageResource(R.mipmap.ic_launcher);
        }




        titleView.setText(titleMovie);
        dateView.setText(releaseYear);
        ratingView.setText(ratingMovie);
        plotTextView.setText(plotMovie);


        return rootView;

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
