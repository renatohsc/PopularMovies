package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Renato Henrique on 14/09/2015.
 */
public class SettingsActivity extends PreferenceActivity
        //implements OnSharedPreferenceChangeListener
    {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        checkValues();




        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        // TODO: Add preferences
    }



    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            // set texts correctly
            onSharedPreferenceChanged(null, "");


        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);


        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // just update all
            ListPreference lp = (ListPreference) findPreference(getString(R.string.pref_sort_key));
            lp.setSummary("dummy");
            lp.setSummary(" %s");

        }


    }


    private void checkValues() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String sortOrder = sharedPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_pop));
               // String sortDesc =  sharedPrefs.getString(getString(R.string.pref_sort_label), getString(R.string.pref_sort_label_popular));



        String msg = "Cur Values: " + "\n sortOrder = " + sortOrder;


        Context context = getApplicationContext();
    //    CharSequence textToast = msg;


        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

//        Toast toast = Toast.makeText(context, textToast, duration);
//        toast.show();


    }




//        private void bindPreferenceSummaryToValue(Preference preference) {
//            // Set the listener to watch for value changes.
//
//            ListPreference lp = (ListPreference)preference;
//            lp.setSummary("dummy"); // required or will not update
//
//            lp.setSummary(" %s");
//
//        }






}