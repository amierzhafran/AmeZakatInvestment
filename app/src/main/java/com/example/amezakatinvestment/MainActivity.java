package com.example.amezakatinvestment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.amezakatinvestment.R;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etValue;
    private Spinner spinnerType;
    private Button btnCalculate, btnClear, btnAbout;
    private TextView tvTotalValue, tvZakatPayable, tvTotalZakat, tvUrufNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        initViews();
        setupSpinner();
        setListeners();
        setupTextWatchers();
    }

    private void setupTextWatchers() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateZakat(false); // Silent calculation
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        etWeight.addTextChangedListener(tw);
        etValue.addTextChangedListener(tw);
    }

    private void initViews() {
        etWeight = findViewById(R.id.etWeight);
        etValue = findViewById(R.id.etValue);
        spinnerType = findViewById(R.id.spinnerType);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        btnAbout = findViewById(R.id.btnAbout);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);
        tvUrufNotice = findViewById(R.id.tvUrufNotice);
    }

    private void setupSpinner() {
        String[] types = getResources().getStringArray(R.array.gold_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(adapter);

        // Update uruf notice when selection changes
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    tvUrufNotice.setText(R.string.notice_uruf_keep);
                } else {
                    tvUrufNotice.setText(R.string.notice_uruf_wear);
                }
                calculateZakat(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setListeners() {
        btnCalculate.setOnClickListener(v -> calculateZakat(true));
        btnClear.setOnClickListener(v -> clearFields());
        btnAbout.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        });
    }

    private void calculateZakat(boolean showToast) {
        String weightStr = etWeight.getText().toString().trim();
        String valueStr = etValue.getText().toString().trim();
        int selectedPosition = spinnerType.getSelectedItemPosition();

        if (weightStr.isEmpty() || valueStr.isEmpty()) {
            if (showToast) {
                if (weightStr.isEmpty()) etWeight.setError(getString(R.string.error_weight_empty));
                if (valueStr.isEmpty()) etValue.setError(getString(R.string.error_value_empty));
            }
            resetOutputs();
            return;
        }

        double weight, value;
        try {
            weight = Double.parseDouble(weightStr);
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            resetOutputs();
            return;
        }

        if (weight <= 0 || value <= 0) {
            resetOutputs();
            return;
        }

        // Clear errors
        etWeight.setError(null);
        etValue.setError(null);

        // Calculations based on the provided instructions:
        // X = 85 for keep, 200 for wear
        double uruf = (selectedPosition == 0) ? 85.0 : 200.0;

        // 1. Total value of gold: weight * value
        double totalValue = weight * value;

        // 2. Zakat payable weight: (weight - X)
        // Note: If (weight - X) is negative, it should be 0
        double zakatPayableWeight = Math.max(0, weight - uruf);

        // 3. Total gold value that is zakat payable: (weight - X) * value
        double zakatPayableValue = zakatPayableWeight * value;

        // 4. Total zakat: 2.5% * (zakat payable value)
        double totalZakat = zakatPayableValue * 0.025;

        // Display outputs
        tvTotalValue.setText(String.format("RM %.2f", totalValue));
        tvZakatPayable.setText(String.format("RM %.2f", zakatPayableValue));
        tvTotalZakat.setText(String.format("RM %.2f", totalZakat));

        if (showToast) {
            if (zakatPayableWeight == 0) {
                Toast.makeText(this, R.string.msg_no_zakat, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.msg_calculate_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetOutputs() {
        tvTotalValue.setText("RM 0.00");
        tvZakatPayable.setText("RM 0.00");
        tvTotalZakat.setText("RM 0.00");
    }

    private void clearFields() {
        etWeight.setText("");
        etValue.setText("");
        spinnerType.setSelection(0);
        tvTotalValue.setText("RM 0.00");
        tvZakatPayable.setText("RM 0.00");
        tvTotalZakat.setText("RM 0.00");
        etWeight.setError(null);
        etValue.setError(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            shareApp();
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        String appUrl = getString(R.string.app_url);
        String shareText = String.format(getString(R.string.share_text), appUrl);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}