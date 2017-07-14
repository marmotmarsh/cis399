package marmot.pig;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PigFragment extends Fragment implements Button.OnClickListener {
    // Gather Widgets and Things
    private TextView player1Label;
    private TextView player2Label;
    private TextView score1Text;
    private TextView score2Text;
    private TextView currentTurnText;
    private ImageView currentDieImage;
    private TextView currentScoreText;
    private Button rollDieButton;
    private Button endTurnButton;

    // Other Objects
    private PigGameLogic pigGame;

    // Shared Information
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
        View view = inflater.inflate(R.layout.fragment_pig,
                container, false);

        player1Label = (TextView) view.findViewById(R.id.player1Label);
        player2Label = (TextView) view.findViewById(R.id.player2Label);
        score1Text = (TextView) view.findViewById(R.id.scoreText1);
        score2Text = (TextView) view.findViewById(R.id.scoreText2);
        currentTurnText = (TextView) view.findViewById(R.id.currentTurnText);
        currentDieImage = (ImageView) view.findViewById(R.id.currentDieImage);
        currentScoreText = (TextView) view.findViewById(R.id.currentScoreText);
        rollDieButton = (Button) view.findViewById(R.id.rollDieButton);
        endTurnButton = (Button) view.findViewById(R.id.endTurnButton);

        pigGame = new PigGameLogic();

        pigGame.setPlayer1Name(savedValues.getString("player1Text", "Player 1"));
        pigGame.setPlayer2Name(savedValues.getString("player2Text", "Player 2"));

        rollDieButton.setOnClickListener(this);
        endTurnButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("player1Text", pigGame.getPlayer1Name());
        editor.putString("player2Text", pigGame.getPlayer2Name());
        editor.putInt("player1Score", pigGame.getPlayer1Score());
        editor.putInt("player2Score", pigGame.getPlayer2Score());
        editor.putInt("currentTurn", pigGame.getCurrentTurn());
        editor.putInt("currentScore", pigGame.getCurrentScore());
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        pigGame = new PigGameLogic();

        pigGame.setPlayer1Name(savedValues.getString("player1Text", "Player 1"));
        pigGame.setPlayer2Name(savedValues.getString("player2Text", "Player 2"));
        pigGame.setPlayer1Score(savedValues.getInt("player1Score", 0));
        pigGame.setPlayer2Score(savedValues.getInt("player2Score", 0));
        pigGame.setCurrentTurn(savedValues.getInt("currentTurn", 1));
        pigGame.setCurrentScore(savedValues.getInt("currentScore", 0));
        pigGame.setWinningScore(Integer.valueOf(savedValues.getString("pref_winning_score", "1")));
        pigGame.setUseAI(savedValues.getBoolean("pref_ai_mode", false));
        pigGame.setDieSides(Integer.valueOf(savedValues.getString("pref_die_sides", "6")));
        pigGame.setMaxCpuRolls(Integer.valueOf(savedValues.getString("pref_ai_max_rolls", "10")));

        player1Label.setText(pigGame.getPlayer1Name());
        player2Label.setText(pigGame.getPlayer2Name());
        score1Text.setText(String.format("%d", pigGame.getPlayer1Score()));
        score2Text.setText(String.format("%d", pigGame.getPlayer2Score()));
        setCurrentTurnText();
        currentScoreText.setText(String.format("%d", pigGame.getCurrentScore()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rollDieButton:
                rollDie();
                break;

            case R.id.endTurnButton:
                endTurn();
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // attempt to get an instance of the SettingsFragment object
        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager()
                .findFragmentById(R.id.settings_fragment);

        // if the SettingsFragment object is null, display the appropriate menu
        if (settingsFragment == null) {
            inflater.inflate(R.menu.fragment_pig, menu);
        } else {
            inflater.inflate(R.menu.fragment_pig, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                // Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setCurrentTurnText() {
        if (pigGame.getCurrentTurn() == 1) {
            currentTurnText.setText(pigGame.getPlayer1Name()+"'s turn");
        } else {
            currentTurnText.setText(pigGame.getPlayer2Name()+"'s turn");
        }
    }

    public void rollDie() {
        int roll = pigGame.rollDie();

        switch (roll) {
            case 1:
                currentDieImage.setImageResource(R.drawable.die1);
                break;
            case 2:
                currentDieImage.setImageResource(R.drawable.die2);
                break;
            case 3:
                currentDieImage.setImageResource(R.drawable.die3);
                break;
            case 4:
                currentDieImage.setImageResource(R.drawable.die4);
                break;
            case 5:
                currentDieImage.setImageResource(R.drawable.die5);
                break;
            case 6:
                currentDieImage.setImageResource(R.drawable.die6);
                break;
        }

        currentScoreText.setText(String.format("%d", pigGame.getCurrentScore()));

        if (roll == 1) {
            endTurn();
        }
    }

    public void endTurn() {
        int endingTurn = pigGame.endTurn();

        score1Text.setText(String.format("%d", pigGame.getPlayer1Score()));
        score2Text.setText(String.format("%d", pigGame.getPlayer2Score()));

        if (pigGame.isUseAI()) {
            //Toast.makeText(this, "Player 2 scored " + String.valueOf(pigGame.getCpuTurnScore()) + " points", Toast.LENGTH_SHORT).show();
        }

        if (endingTurn == 2) {
            if (pigGame.isFinalTurn()) {
                if (pigGame.getPlayer1Score() > pigGame.getPlayer2Score()) {
                    //Toast.makeText(this, "Player 1 has won", Toast.LENGTH_LONG).show();
                    newGame();
                    return;
                } else if (pigGame.getPlayer2Score() > pigGame.getPlayer1Score()) {
                    //Toast.makeText(this, "Player 2 has won", Toast.LENGTH_LONG).show();
                    newGame();
                    return;
                }
            }
        }

        currentScoreText.setText(String.format("%d", 0));

        setCurrentTurnText();
    }

    public void newGame() {
        pigGame.newGame();

        score1Text.setText(String.format("%d", pigGame.getPlayer1Score()));
        score2Text.setText(String.format("%d", pigGame.getPlayer2Score()));
        currentScoreText.setText(String.format("%d", pigGame.getCurrentScore()));
        setCurrentTurnText();
        currentDieImage.setImageResource(R.drawable.die1);
    }
}