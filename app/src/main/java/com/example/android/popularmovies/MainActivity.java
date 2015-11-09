package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity  implements MainActivityFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public final static String ARG_ORDER_TYPE = "ARG_ORDER_TYPE";

    public final static int TYPE_POP = 0;

    public boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(LOG_TAG, "Maldicao executavel 33");
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            Log.v(LOG_TAG, "Maldigato executavel 43");
            mTwoPane = true;
        } else {
            Log.v(LOG_TAG, "Maldigato executavel 58");
            mTwoPane = false;
        }

     //   initOrder();
        Log.v(LOG_TAG, "Maldicao executavel 43");
    }


    private void initOrder(){
       // SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ARG_ORDER_TYPE, TYPE_POP);
        editor.commit();

    }


    @Override
    public void onItemSelected(int position,int id, String title, String poster_path, String release, String rating, String plot) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Log.v(LOG_TAG, "Maldicao executavel 53");
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
            Log.v(LOG_TAG, "Maldicao executavel 55");
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
            Log.v(LOG_TAG, "Maldicao executavel 22");


        }
    }


}
