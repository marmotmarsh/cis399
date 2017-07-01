package marmot.pig;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.preference.PreferenceActivity;

/**
 * Created by marmot on 6/29/2017.
 */

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("SETTINGSACTIVITY", "Entering Settings Activity");
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}