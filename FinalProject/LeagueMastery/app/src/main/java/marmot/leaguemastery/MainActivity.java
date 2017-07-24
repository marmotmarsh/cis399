package marmot.leaguemastery;

import android.app.Activity;
import android.content.Intent;
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

import org.json.JSONArray;

public class MainActivity extends Activity implements Button.OnClickListener, EditText.OnEditorActionListener {

    // Widgets

    private EditText usernameEditText;
    private Button showMasteriesButton;

    // Query information

    private RequestQueue requestQueue;

    // Other Local Information

    private String player;
    private JSONArray jsonArray;

    // Saved Information

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        showMasteriesButton = findViewById(R.id.showMasteriesButton);

        player = "";

        requestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);

        showMasteriesButton.setOnClickListener(this);
        usernameEditText.setOnEditorActionListener(this);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("player", player);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        player = savedValues.getString("player", "");
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
                SharedPreferences.Editor editor = savedValues.edit();
                editor.putString("player", usernameEditText.getText().toString());
                editor.commit();

                Log.d("Button Clicked", "Going to get info");

                startActivity(new Intent(getApplicationContext(), ListViewActivity.class));
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            switch (view.getId()) {
                case R.id.usernameEditText:
                    player = usernameEditText.getText().toString();
                    break;
            }

            return true;
        }
        return false;
    }

    private void getInformation(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.d("RESPONSE", "Response is: "+ response.toString());

                        jsonArray = jsonResponse(response);
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

    public JSONArray jsonResponse(String response) {
        try {

            JSONArray obj = new JSONArray(response);

            Log.d("RESPONSE", obj.toString());

            return obj;
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");

            return null;
        }
    }
}
