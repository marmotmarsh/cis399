package marmot.clickcounter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClickCounter extends AppCompatActivity implements Button.OnClickListener {
    private Button addButton;
    private Button resetButton;
    private TextView counterText;

    private int currentCount;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_counter);

        addButton = (Button) findViewById(R.id.addButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        counterText = (TextView) findViewById(R.id.counterText);
        currentCount = 0;

        addButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("subtotalString", currentCount);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        currentCount = savedValues.getInt("subtotalString", 0);
        counterText.setText(String.format("%d", currentCount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                incrementCount();
                break;
            case R.id.resetButton:
                resetCount();
                break;
        }
    }

    public void incrementCount() {
        currentCount++;
        counterText.setText(String.format("%d", currentCount));
    }

    public void resetCount() {
        currentCount = 0;
        counterText.setText(String.format("%d", currentCount));
    }
}
