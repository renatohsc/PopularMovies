package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    public static final String TITLE_KEY = "titleMovie";
    public static final String POSTER_KEY = "posterMovie";
    public static final String RELEASE_KEY = "releaseDate";
    public static final String RATING_KEY = "ratingMovie";
    public static final String PLOT_KEY = "plotMovie";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String titleMovie = getIntent().getExtras().getString(TITLE_KEY);
        String posterMovie = getIntent().getExtras().getString(POSTER_KEY);
        String releaseDate = getIntent().getExtras().getString(RELEASE_KEY);

        String releaseYear = "";

        releaseYear = releaseDate.substring( 0, 4 );

        String ratingMovie = getIntent().getExtras().getString(RATING_KEY) + "/10";
        String plotMovie = getIntent().getExtras().getString(PLOT_KEY);

        TextView titleView = (TextView) findViewById(R.id.titleMovie);
        TextView dateView = (TextView) findViewById(R.id.releaseDate);
        TextView ratingView = (TextView) findViewById(R.id.ratingMovie);
        ImageView posterView = (ImageView) findViewById(R.id.posterMovie);
        TextView plotTextView = (TextView) findViewById(R.id.plotMovie);





        String sizeImage = "w185";
        if(posterMovie != null) {
            String posterUrl = "http://image.tmdb.org/t/p/" + sizeImage + "/" + posterMovie;
           // Log.v(LOG_TAG, "poster url : " + posterUrl);
            Picasso.with(this)
                    .load(posterUrl)
                    .resize(500, 750)
                            //    .fit()
                    .into(posterView);
        } else {

            posterView.setImageResource(R.mipmap.ic_launcher);
        }




        titleView.setText(titleMovie);
        dateView.setText(releaseYear);
        ratingView.setText(ratingMovie);
        plotTextView.setText(plotMovie);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
