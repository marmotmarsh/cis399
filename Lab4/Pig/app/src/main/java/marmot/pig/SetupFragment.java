package marmot.pig;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by marmot on 7/13/2017.
 */

public class SetupFragment extends Fragment implements Button.OnClickListener,
        EditText.OnEditorActionListener{

    // Widgets and things
    private EditText player1Text;
    private EditText player2Text;
    private Button newGameButton;

    // Preferences and saved data
    private SharedPreferences savedValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(getActivity(),
                R.xml.preferences, false);

        // get the default SharedPreferences object
        savedValues = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // turn on the options menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_setup,
                container, false);

        player1Text = (EditText) view.findViewById(R.id.player1Text);
        player2Text = (EditText) view.findViewById(R.id.player2Text);
        newGameButton = (Button) view.findViewById(R.id.newGameButton);

        player1Text.setOnEditorActionListener(this);
        player2Text.setOnEditorActionListener(this);
        newGameButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("player1Text", player1Text.getText().toString());
        editor.putString("player2Text", player2Text.getText().toString());
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        player1Text.setText(savedValues.getString("player1Text", "Player 1"));
        player2Text.setText(savedValues.getString("player2Text", "Player 2"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGameButton:
                startActivity(new Intent(getActivity(), PigActivity.class));
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        SharedPreferences.Editor editor = savedValues.edit();
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            switch (view.getId()) {
                case R.id.player1Text:
                    editor.putString("player1Text", player1Text.getText().toString());
                    break;

                case R.id.player2Text:
                    editor.putString("player2Text", player2Text.getText().toString());
                    break;
            }
            editor.commit();
            return true;
        }
        return false;
    }
}
