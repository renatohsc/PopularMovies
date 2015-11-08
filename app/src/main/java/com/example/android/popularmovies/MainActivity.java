package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity  implements MainActivityFragment.Callback {

   // private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String DETAILFRAGMENT_TAG = "DFTAG";



    public boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemSelected(int position,int id, String title, String poster_path, String release, String rating, String plot) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(DetailActivityFragment.ID_KEY, id);
            arguments.putString(DetailActivityFragment.TITLE_KEY, title);
            arguments.putString(DetailActivityFragment.POSTER_KEY, poster_path);
            arguments.putString(DetailActivityFragment.RELEASE_KEY, release);
            arguments.putString(DetailActivityFragment.RATING_KEY, rating);
            arguments.putString(DetailActivityFragment.PLOT_KEY, plot);


            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode,

            Intent intent = new Intent(getApplicationContext(), DetailActivityFragment.class);
            intent.putExtra(DetailActivityFragment.ID_KEY,id);
            intent.putExtra(DetailActivityFragment.TITLE_KEY, title);
            intent.putExtra(DetailActivityFragment.POSTER_KEY, poster_path);
            intent.putExtra(DetailActivityFragment.RELEASE_KEY, release);
            intent.putExtra(DetailActivityFragment.RATING_KEY, rating);
            intent.putExtra(DetailActivityFragment.PLOT_KEY, plot);
            startActivity(intent);
        }
    }




    @Override
    public void onFirstItem(int position,int id, String title, String poster_path, String release, String rating, String plot) {
        if(mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(DetailActivityFragment.ID_KEY, id);
            arguments.putString(DetailActivityFragment.TITLE_KEY, title);
            arguments.putString(DetailActivityFragment.POSTER_KEY, poster_path);
            arguments.putString(DetailActivityFragment.RELEASE_KEY, release);
            arguments.putString(DetailActivityFragment.RATING_KEY, rating);
            arguments.putString(DetailActivityFragment.PLOT_KEY, plot);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();



        }
    }


}
