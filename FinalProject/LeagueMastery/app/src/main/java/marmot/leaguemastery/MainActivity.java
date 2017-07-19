package marmot.leaguemastery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class MainActivity extends Activity implements Button.OnClickListener, EditText.OnEditorActionListener {

    // Widgets

    private EditText usernameEditText;
    private Button showMasteriesButton;

    // Query information

    private RequestQueue requestQueue;
    private String summonerURL;
    private String masteriesURL;

    // Other Local Information

    private String username;

    // Saved Information

    private SharedPreferences savedValues;

    // Constants

    private final String API_KEY = "RGAPI-fce3c9c0-5ff5-4793-a4ff-87cd10d5b328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        showMasteriesButton = findViewById(R.id.showMasteriesButton);

        username = "";

        requestQueue = Volley.newRequestQueue(this);
        summonerURL = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/metalmarsh89?api_key=" + API_KEY;
        masteriesURL = "https://na1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries/by-summoner/73043813?api_key=" + API_KEY;

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);

        checkForInformation();
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("username", username);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        username = savedValues.getString("username", "");
    }

    @Override
    public void onStop() {
        super.onStop();

        requestQueue.cancelAll(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showMasteriesButton:
                //TODO: New Class
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            switch (view.getId()) {
                case R.id.usernameEditText:
                    username = usernameEditText.getText().toString();
                    break;
            }

            return true;
        }
        return false;
    }

    public void checkForInformation() {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, masteriesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.d("RESPONSE", "Response is: "+ response.toString());

                        jsonResponse(response);
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

    public void jsonResponse(String response) {
        try {

            JSONArray obj = new JSONArray(response);

            Log.d("My App", obj.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
        }
    }
}
