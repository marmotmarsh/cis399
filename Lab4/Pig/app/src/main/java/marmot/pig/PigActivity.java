package marmot.pig;

import android.app.Activity;
import android.os.Bundle;

public class PigActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view for the activity using XML
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PigFragment())
                .commit();
    }
}