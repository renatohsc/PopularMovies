package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putInt(DetailActivityFragment.ID_KEY,
                    getIntent().getIntExtra(DetailActivityFragment.ID_KEY, 0));
            arguments.putString(DetailActivityFragment.TITLE_KEY,
                    getIntent().getStringExtra(DetailActivityFragment.TITLE_KEY));
            arguments.putString(DetailActivityFragment.POSTER_KEY,
                    getIntent().getStringExtra(DetailActivityFragment.POSTER_KEY));
            arguments.putString(DetailActivityFragment.RELEASE_KEY,
                    getIntent().getStringExtra(DetailActivityFragment.RELEASE_KEY));
            arguments.putString(DetailActivityFragment.RATING_KEY,
                    getIntent().getStringExtra(DetailActivityFragment.RATING_KEY));

            arguments.putString(DetailActivityFragment.PLOT_KEY,
                    getIntent().getStringExtra(DetailActivityFragment.PLOT_KEY));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }




}
