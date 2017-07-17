package marmot.tidepredictor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Holden on 7/16/2017.
 *
 */

public class SetupActivity extends Activity implements Button.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner locationSpinner;
    private Spinner dateSpinner;
    private Button showTidesButton;

    private String selectedLocation;
    private String selectedDate;

    private LinkedHashMap<String, String> locationMap;
    private LinkedHashMap<String, String> dateMap;

    private TideListDB db;

    private final int[] xmlFiles = {R.xml.coos_bay, R.xml.florence, R.xml.gold_beach};

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // get db and StringBuilder objects
        db = new TideListDB(this);
        readXMLFile();

        // Set up Location Spinner

        locationSpinner = findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.locations_array_keys, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(this);

        String[] locationKeys = this.getResources().getStringArray(R.array.locations_array_keys);
        String[] locationValues = this.getResources().getStringArray(R.array.locations_array_values);
        locationMap = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(locationKeys.length, locationValues.length); ++i) {
            locationMap.put(locationKeys[i], locationValues[i]);
        }

        // Set up Date Spinner

        dateSpinner = findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(this,
                R.array.dates_array_keys, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(this);

        String[] dateKeys = this.getResources().getStringArray(R.array.dates_array_keys);
        String[] dateValues = this.getResources().getStringArray(R.array.dates_array_values);
        dateMap = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(dateKeys.length, dateValues.length); ++i) {
            dateMap.put(dateKeys[i], dateValues[i]);
        }

        // Set up Show Tides Button

        showTidesButton = findViewById(R.id.show_tides_button);
        showTidesButton.setOnClickListener(this);

        // Other values

        selectedLocation = "";
        selectedDate = "";

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
        switch (parent.getId()) {
            case R.id.location_spinner:
                selectedLocation = parent.getItemAtPosition(pos).toString();
                Log.d("SELECTED", selectedLocation);
                break;
            case R.id.date_spinner:
                selectedDate = parent.getItemAtPosition(pos).toString();
                Log.d("SELECTED", selectedDate);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView parent) {
        switch (parent.getId()) {
            case R.id.location_spinner:
                selectedLocation = parent.getItemAtPosition(0).toString();
                Log.d("SELECTED", "Reset, " + selectedLocation);
                break;
            case R.id.date_spinner:
                selectedDate = parent.getItemAtPosition(0).toString();
                Log.d("SELECTED", "Reset, " + selectedDate);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_tides_button) {
            SharedPreferences.Editor editor = savedValues.edit();
            editor.putString("location", selectedLocation);
            editor.putString("date", selectedDate);
            editor.commit();

            startActivity(new Intent(getApplicationContext(), ItemsActivity.class));

        }
    }

    public void readXMLFile() {
        Log.d("TIDES", "Trying to read xml file");
        ArrayList<TideItem> tideItems;

        for (int i = 1; i <= 3; i++) {
            try {
                tideItems = new ParseXML().parse(this, i, xmlFiles[i-1]);
                for (TideItem tide: tideItems) {
                    db.insertTideItem(tide);
                }
            } catch (Exception e) {
                Log.d("TIDES", e.getMessage());
            }
        }
    }
}
