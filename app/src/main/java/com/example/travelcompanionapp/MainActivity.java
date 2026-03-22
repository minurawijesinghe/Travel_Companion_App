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
 * Main Activity for the Travel Companion App.
 * Handles user interactions for converting currency, fuel efficiency, distance, and temperature.
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private Spinner spinnerCategory, spinnerFrom, spinnerTo;
    private TextInputEditText etInput;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
    }

    /**
     * Finds and initializes all UI components from the layout.
     */
    private void initializeViews() {
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);
    }

    /**
     * Sets up click and selection listeners for the UI components.
     */
    private void setupListeners() {
        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnReset = findViewById(R.id.btnReset);

        // Update units when the category selection changes
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

        // Handle conversion button click
        btnConvert.setOnClickListener(v -> performConversion());

        // Handle reset button click
        btnReset.setOnClickListener(v -> resetFields());
    }

    /**
     * Updates the 'From' and 'To' spinners based on the selected category.
     *
     * @param categoryPosition The index of the selected category.
     */
    private void updateUnitSpinners(int categoryPosition) {
        int unitArrayId;
        
        // Map category position to the appropriate string array resource
        switch (categoryPosition) {
            case 1: // Fuel & Distance
                unitArrayId = R.array.fuel_distance_units;
                break;
            case 2: // Temperature
                unitArrayId = R.array.temperature_units;
                break;
            default: // Currency (default)
                unitArrayId = R.array.currency_units;
                break;
        }

        // Create and set the adapter for both unit spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                unitArrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Reset result display when category changes
        tvResult.setText(getString(R.string.default_result));
    }

    /**
     * Validates input and performs the conversion using the Converter utility class.
     */
    private void performConversion() {
        String inputStr = etInput.getText() != null ? etInput.getText().toString().trim() : "";

        // 1. Validation: Check if input is empty
        if (inputStr.isEmpty()) {
            showToast(R.string.error_empty);
            return;
        }

        try {
            double inputValue = Double.parseDouble(inputStr);
            int categoryPos = spinnerCategory.getSelectedItemPosition();
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();

            // 2. Validation: Identity Conversion (Same units selected)
            if (fromUnit.equals(toUnit)) {
                showToast(R.string.warn_identity_conversion);
                tvResult.setText(String.format(Locale.getDefault(), "%.2f", inputValue));
                return;
            }

            // 3. Validation: Prevent negative values for non-temperature conversions
            if (categoryPos != 2 && inputValue < 0) {
                showToast(R.string.error_negative);
                return;
            }

            double result = 0;

            // 4. Routing: Call appropriate conversion logic based on category
            switch (categoryPos) {
                case 0: // Currency
                    result = Converter.convertCurrency(inputValue, fromUnit, toUnit);
                    break;
                case 1: // Fuel/Distance
                    result = Converter.convertFuelOrDistance(inputValue, fromUnit, toUnit);
                    if (result == -1) {
                        showToast(R.string.error_incompatible);
                        return;
                    }
                    break;
                case 2: // Temperature
                    result = Converter.convertTemperature(inputValue, fromUnit, toUnit);
                    break;
            }

            // 5. Display Result: Format to 2 decimal places
            tvResult.setText(String.format(Locale.getDefault(), "%.2f", result));

        } catch (NumberFormatException e) {
            showToast(R.string.error_invalid);
        }
    }

    /**
     * Resets all input fields and selections to their default states.
     */
    private void resetFields() {
        etInput.setText("");
        tvResult.setText(getString(R.string.default_result));
        spinnerCategory.setSelection(0);
        showToast(R.string.reset_msg);
    }

    /**
     * Utility method to show a short toast message.
     *
     * @param resId The resource ID of the string to display.
     */
    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
