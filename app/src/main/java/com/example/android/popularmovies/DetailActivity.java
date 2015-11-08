package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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
