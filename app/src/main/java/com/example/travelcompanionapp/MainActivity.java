package com.example.travelcompanionapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

/**
 * MainActivity handles user interaction for the Travel Companion app.
 * It manages:
 * - category selection
 * - unit spinner updates
 * - input validation
 * - conversion requests
 * - result display
 */
public class MainActivity extends AppCompatActivity {

    // UI components
    private Spinner spinnerCategory;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextInputEditText etInput;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link Java variables with XML views
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);

        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnReset = findViewById(R.id.btnReset);

        // Update source and destination unit spinners when category changes
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // Perform conversion when Convert button is clicked
        btnConvert.setOnClickListener(v -> performConversion());

        // Reset all fields when Reset button is clicked
        btnReset.setOnClickListener(v -> resetFields());
    }

    /**
     * Updates the "from" and "to" spinners based on the selected category.
     *
     * @param categoryPosition selected category index
     */
    private void updateUnitSpinners(int categoryPosition) {
        int unitArrayId;

        // Choose the correct unit list based on selected category
        switch (categoryPosition) {
            case 1:
                unitArrayId = R.array.fuel_distance_units;
                break;
            case 2:
                unitArrayId = R.array.temperature_units;
                break;
            default:
                unitArrayId = R.array.currency_units;
                break;
        }

        // Create adapter from string-array resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                unitArrayId,
                android.R.layout.simple_spinner_item
        );

        // Set dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the same adapter to both spinners
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Reset displayed result whenever category changes
        tvResult.setText(getString(R.string.default_result));
    }

    /**
     * Reads user input, validates it, performs the selected conversion,
     * and displays the formatted result.
     */
    private void performConversion() {
        // Safely get user input and remove leading/trailing spaces
        String inputStr = etInput.getText() != null
                ? etInput.getText().toString().trim()
                : "";

        // Check if input is empty
        if (inputStr.isEmpty()) {
            Toast.makeText(this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Parse numeric input
            double inputValue = Double.parseDouble(inputStr);

            // Read selected category and units
            int categoryPos = spinnerCategory.getSelectedItemPosition();
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();

            // If both selected units are the same, return original value
            if (fromUnit.equals(toUnit)) {
                Toast.makeText(this, R.string.warn_identity_conversion, Toast.LENGTH_SHORT).show();
                tvResult.setText(String.format(Locale.getDefault(), "%.2f", inputValue));
                return;
            }

            // Reject negative values for non-temperature categories
            if (categoryPos != 2 && inputValue < 0) {
                Toast.makeText(this, R.string.error_negative, Toast.LENGTH_SHORT).show();
                return;
            }

            double result = 0;

            // Call the correct conversion method based on category
            switch (categoryPos) {
                case 0:
                    result = Converter.convertCurrency(inputValue, fromUnit, toUnit);
                    break;

                case 1:
                    result = Converter.convertFuelOrDistance(inputValue, fromUnit, toUnit);

                    // Handle unsupported fuel/distance conversions
                    if (result == -1) {
                        Toast.makeText(this, R.string.error_incompatible, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;

                case 2:
                    result = Converter.convertTemperature(inputValue, fromUnit, toUnit);
                    break;
            }

            // Display result rounded to 2 decimal places
            tvResult.setText(String.format(Locale.getDefault(), "%.2f", result));

        } catch (NumberFormatException e) {
            // Handle invalid numeric input
            Toast.makeText(this, R.string.error_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears input, resets category spinner to default,
     * and restores the default result text.
     */
    private void resetFields() {
        etInput.setText("");
        tvResult.setText(getString(R.string.default_result));
        spinnerCategory.setSelection(0);
        Toast.makeText(this, R.string.reset_msg, Toast.LENGTH_SHORT).show();
    }
}