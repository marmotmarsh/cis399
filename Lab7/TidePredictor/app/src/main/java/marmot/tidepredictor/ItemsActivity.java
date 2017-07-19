package marmot.tidepredictor;

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

public class ItemsActivity extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList<TideItem> tideItems;

    private TextView titleTextView;
    private ListView itemsListView;

    private TideListDB db;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        titleTextView = findViewById(R.id.titleTextView);
        itemsListView = findViewById(R.id.itemsListView);

        tideItems = new ArrayList<>();

        itemsListView.setOnItemClickListener(this);
        itemsListView.setFastScrollEnabled(true);

        db = new TideListDB(this);

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);

        updateDisplay();
    }

    public void updateDisplay() {
        String location = savedValues.getString("location", "");
        if (location == "") {
            Log.e("ERROR", "Found nothing");
            return;
        }

        tideItems = db.getTideItems(location);

        if (tideItems == null) {
            titleTextView.setText(R.string.item_error);
            return;
        }

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<>();
        for (TideItem item : tideItems) {
            HashMap<String, String> map = new HashMap<>();
            map.put("date", item.getDateFormatted());
            map.put("day", item.getDay());
            map.put("type", item.getType());
            map.put("time", item.getTime());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "day", "type", "time"};
        int[] to = {R.id.dateTextView, R.id.dayTextView, R.id.tideTypeTextView, R.id.timeTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed: " + new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        TideItem item = tideItems.get(position);

        Toast.makeText(this, String.valueOf(item.getPredCm()) + " cm", Toast.LENGTH_SHORT).show();
    }
}