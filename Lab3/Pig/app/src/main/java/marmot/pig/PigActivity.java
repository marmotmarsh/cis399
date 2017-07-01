package marmot.pig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PigActivity extends AppCompatActivity implements Button.OnClickListener,
        EditText.OnEditorActionListener {
    // Gather Widgets and Things
    private EditText player1Text;
    private EditText player2Text;
    private TextView score1Text;
    private TextView score2Text;
    private TextView currentTurnText;
    private ImageView currentDieImage;
    private TextView currentScoreText;
    private Button rollDieButton;
    private Button endTurnButton;
    private Button newGameButton;

    // Other Objects
    private PigGame pigGame;

    // Shared Information
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig);

        player1Text = (EditText) findViewById(R.id.player1Text);
        player2Text = (EditText) findViewById(R.id.player2Text);
        score1Text = (TextView) findViewById(R.id.scoreText1);
        score2Text = (TextView) findViewById(R.id.scoreText2);
        currentTurnText = (TextView) findViewById(R.id.currentTurnText);
        currentDieImage = (ImageView) findViewById(R.id.currentDieImage);
        currentScoreText = (TextView) findViewById(R.id.currentScoreText);
        rollDieButton = (Button) findViewById(R.id.rollDieButton);
        endTurnButton = (Button) findViewById(R.id.endTurnButton);
        newGameButton = (Button) findViewById(R.id.newGameButton);

        pigGame = new PigGame();

        player1Text.setOnEditorActionListener(this);
        player2Text.setOnEditorActionListener(this);

        rollDieButton.setOnClickListener(this);
        endTurnButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
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

        pigGame = new PigGame();

        pigGame.setPlayer1Name(savedValues.getString("player1Text", "Player 1"));
        pigGame.setPlayer2Name(savedValues.getString("player2Text", "Player 2"));
        pigGame.setPlayer1Score(savedValues.getInt("player1Score", 0));
        pigGame.setPlayer2Score(savedValues.getInt("player2Score", 0));
        pigGame.setCurrentTurn(savedValues.getInt("currentTurn", 1));
        pigGame.setCurrentScore(savedValues.getInt("currentScore", 0));

        String win = savedValues.getString("pref_winning_score", "17");
        Log.d("WINNING_SCORE", String.valueOf(win));
        //pigGame.setWinningScore(win);

        player1Text.setText(pigGame.getPlayer1Name());
        player2Text.setText(pigGame.getPlayer2Name());
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

            case R.id.newGameButton:
                newGame();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            switch (view.getId()) {
                case R.id.player1Text:
                    pigGame.setPlayer1Name(player1Text.getText().toString());
                    break;

                case R.id.player2Text:
                    pigGame.setPlayer2Name(player2Text.getText().toString());
                    break;
            }
            setCurrentTurnText();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                // Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                // Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "This is a blurb about the app. This app is about a pig.", Toast.LENGTH_LONG).show();
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

        if (endingTurn == 1) {
            score1Text.setText(String.format("%d", pigGame.getPlayer1Score()));
        } else {
            score2Text.setText(String.format("%d", pigGame.getPlayer2Score()));

            if (pigGame.isFinalTurn()) {
                if (pigGame.getPlayer1Score() > pigGame.getPlayer2Score()) {
                    Toast.makeText(this, "Player 1 has won", Toast.LENGTH_LONG).show();
                    newGame();
                    return;
                } else if (pigGame.getPlayer2Score() > pigGame.getPlayer1Score()) {
                    Toast.makeText(this, "Player 2 has won", Toast.LENGTH_LONG).show();
                    newGame();
                    return;
                }
            }
        }

        currentScoreText.setText(String.format("%d", 0));

        setCurrentTurnText();

        Log.d("WINNING_SCORE", String.valueOf(pigGame.getWinningScore()));
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