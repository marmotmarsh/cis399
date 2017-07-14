package marmot.pig;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by marmot on 7/13/2017.
 */

public class SetupActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view for the activity using XML
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SetupFragment())
                .commit();
    }
}
