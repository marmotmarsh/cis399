package marmot.leaguemastery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Holden on 7/18/2017.
 *
 */

public class ListViewActivity extends Activity {
    private ArrayList<ChampionMastery> championMasteries;

    private TextView titleTextView;
    private ListView itemsListView;

    private ChampionMasteryDB db;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        titleTextView = findViewById(R.id.titleTextView);
        itemsListView = findViewById(R.id.itemsListView);

        championMasteries = new ArrayList<>();

        itemsListView.setFastScrollEnabled(true);

        db = new ChampionMasteryDB(this);

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);

        updateDisplay();
    }

    public void updateDisplay() {
        String location = savedValues.getString("location", "");
        if (location == "") {
            Log.e("ERROR", "Found nothing");
            return;
        }

        championMasteries = db.getChampionMasteries(location);

        if (championMasteries == null) {
            titleTextView.setText(R.string.item_error);
            return;
        }

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<>();
        for (ChampionMastery mastery : championMasteries) {
            HashMap<String, String> map = new HashMap<>();
            map.put("chestGranted", mastery.getChestGranted());
            map.put("championLevel", mastery.getChampionLevel());
            map.put("championPoints", mastery.getChampionPoints());
            map.put("championLevel", mastery.getChampionId());
            //TODO: More information here
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"championLevel", "championLevel"};
        int[] to = {R.id.textView, R.id.textView2};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed: " + new Date());
    }
}
