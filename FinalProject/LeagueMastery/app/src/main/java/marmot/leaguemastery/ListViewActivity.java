package marmot.leaguemastery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Holden on 7/18/2017.
 *
 */

public class ListViewActivity extends Activity {
    private ArrayList<ChampionMastery> championMasteries;
    private ArrayList<Champion> champions;

    private TextView titleTextView;
    private ListView itemsListView;

    // Query information
    private RequestQueue requestQueue;
    private String summonerURL;
    private String championMasteriesURL;
    private String championsURL;

    private ChampionDB db;

    private SharedPreferences savedValues;

    private String playerDb;
    private String championDb;

    // Constants

    private final String API_KEY = "RGAPI-38ef6272-7f1e-43af-9c30-f157ef9c6032";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        titleTextView = findViewById(R.id.titleTextView);
        itemsListView = findViewById(R.id.itemsListView);

        summonerURL = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/metalmarsh89?api_key=" + API_KEY;
        championMasteriesURL = "https://na1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries/by-summoner/73043813?api_key=" + API_KEY;
        championsURL = "https://na1.api.riotgames.com/lol/static-data/v3/champions?locale=en_US&dataById=true&api_key=" + API_KEY;

        championMasteries = new ArrayList<>();
        champions = new ArrayList<>();

        itemsListView.setFastScrollEnabled(true);

        db = new ChampionDB(this);

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);

        playerDb = "metalmarsh89";
        championDb = "champion";

        requestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        getChampions(championsURL);
        getChampionMasteries(championMasteriesURL);
    }

    @Override
    public void onPause() {
        db.closeDB();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        db.closeDB();
        requestQueue.cancelAll(this);

        super.onStop();
    }

    private void getChampionMasteries(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.d("RESPONSE", "Response is: "+ response.toString());

                        parseMasteriesJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE", "That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    private void getChampions(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.d("RESPONSE", "Response is: "+ response.toString());

                        parseChampionsJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE", "That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void parseChampionsJson(String response) {


        try {
            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
            Log.d("RESPONSE", jsonObject.toString());
            champions = Champion.fromJson(jsonObject);
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
        }



        if (champions != null) {
            for (Champion champion : champions) {
                champion.setListId(1);
                db.insertChampion(champion);
            }
        }
    }

    public void parseMasteriesJson(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            Log.d("RESPONSE", jsonArray.toString());
            championMasteries = ChampionMastery.fromJson(jsonArray);
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
        }

        if (championMasteries != null) {
            for (ChampionMastery mastery : championMasteries) {
                mastery.setListId(2);
                db.insertMastery(mastery);
            }
        }

        updateDisplay();
    }

    public void updateDisplay() {
        championMasteries = db.getMasteries(playerDb);
        champions = db.getChampions(championDb);

        Log.d("UPDATE DISPLAY", "Masteries Size: " + String.valueOf(championMasteries.size()));
        Log.d("UPDATE DISPLAY", "Champions Size: " + String.valueOf(champions.size()));

        HashMap<ChampionMastery, Champion> combined = db.getChampionsAndMasteries();

        if (championMasteries == null) {
            titleTextView.setText(R.string.item_error);
            return;
    }

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<>();
        for (ChampionMastery mastery : combined.keySet()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("championName", combined.get(mastery).getName());
            map.put("championPoints", mastery.getChampionPoints());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"championName", "championPoints"};
        int[] to = {R.id.textView, R.id.textView2};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("Display", "Masteries displayed: " + new Date());
    }
}
