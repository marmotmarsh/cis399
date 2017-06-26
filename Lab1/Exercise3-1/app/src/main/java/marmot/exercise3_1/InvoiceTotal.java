package marmot.exercise3_1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class InvoiceTotal extends AppCompatActivity implements TextView.OnEditorActionListener {
    private EditText subtotalField;
    private TextView discountPercentField;
    private TextView discountAmountField;
    private TextView totalField;

    private SharedPreferences savedValues;

    private float subtotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Creating", "Starting this whole thang");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_total);
        subtotalField = (EditText) findViewById(R.id.subtotalField);
        discountPercentField = (TextView) findViewById(R.id.discountPercentField);
        discountAmountField = (TextView) findViewById(R.id.discountAmountField);
        totalField = (TextView) findViewById(R.id.totalField);

        subtotalField.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putFloat("subtotalString", subtotal);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        subtotal = savedValues.getFloat("subtotalString", .2f);
        subtotalField.setText(String.format("%.2f", subtotal));
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {
        Log.d("Calculating", "Entering the calculator");
        String subtotalString = subtotalField.getText().toString();

        if (subtotalString.equals("")) {
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString);
        }
        double discountPercent;

        if (subtotal >= 200.0) {
            discountPercent = 0.2;
        } else if (subtotal >= 100.0) {
            discountPercent = 0.1;
        } else {
            discountPercent = 0;
        }

        double discountAmount = subtotal * discountPercent;
        double total = subtotal - discountAmount;

        String discountPercentString = String.format("%.0f", discountPercent*100) + "%";
        discountPercentField.setText(discountPercentString);
        String discountAmountString = "$" + String.format("%.2f", discountAmount);
        discountAmountField.setText(discountAmountString);
        String totalString = "$" + String.format("%.2f", total);
        totalField.setText(totalString);
        Log.d("NewSubtotal", String.format("%.2f", subtotal));
        subtotalField.setText("");
        subtotalField.setText(String.format("%.2f", subtotal));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent e) {
        Log.d("Editing", "Entering edit for subtotal");
        if ((actionId == EditorInfo.IME_ACTION_DONE) || (e.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            calculateAndDisplay();
        }
        return false;
    }
}
