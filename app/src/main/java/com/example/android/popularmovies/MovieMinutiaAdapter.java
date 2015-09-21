package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RenatoHenrique on 15/09/2015.
 */
public class MovieMinutiaAdapter

           extends BaseAdapter {
        // extends ArrayAdapter<MovieMinutia> {

    private static final String LOG_TAG = MovieMinutiaAdapter.class.getSimpleName();

    private Context movieContext;
    private LayoutInflater movieInflater;
    private ArrayList<MovieMinutia> movieList;


    public MovieMinutiaAdapter(Context context, ArrayList<MovieMinutia> movieMinutias) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        //super(context, 0, movieMinutias);
        this.movieContext = context;
        this.movieList = movieMinutias;
        movieInflater = (LayoutInflater) movieContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return movieList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View view = convertView;


        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (view == null) {

            view = movieInflater.inflate(R.layout.grid_item_movie, null);
            viewHolder = new ViewHolder();
            viewHolder.posterView = (ImageView) view.findViewById(R.id.poster);
            //viewHolder.posterView.setLayoutParams(new LinearLayout
            //        .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            view.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        ImageView posterView = viewHolder.posterView;
        MovieMinutia movie = movieList.get(position);



        //Using Picasso to display poster


        String posterPath = movie.posterMovie;

        String sizeImage = movieContext.getResources().getString(R.string.size_image);

        //String sizeImage = "w185";
        if(posterPath != null) {
            String posterUrl = "http://image.tmdb.org/t/p/" + sizeImage + "/" + posterPath;
            Log.v(LOG_TAG, "poster url : " + posterUrl);
            Picasso.with(movieContext)
                    .load(posterUrl)
                    .resize(500, 750)
              //    .fit()
                    .into(posterView);
        } else {

            posterView.setImageResource(R.mipmap.ic_launcher);
        }





        return view;
    }

    public class ViewHolder {
        ImageView posterView;
    }

}


