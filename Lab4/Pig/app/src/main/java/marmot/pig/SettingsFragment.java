package marmot.pig;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences savedValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        savedValues = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        savedValues.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        savedValues.unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs,
                                          String key) {
        PigFragment pigFragment =
                (PigFragment) getFragmentManager()
                        .findFragmentById(R.id.main_fragment);
        if (pigFragment != null) {
            pigFragment.onResume();
        }

    }
}