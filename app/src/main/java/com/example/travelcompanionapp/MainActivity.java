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

public class MainActivity extends AppCompatActivity {

    // Define the components we need to use
    private Spinner spinnerCategory, spinnerFrom, spinnerTo;
    private TextInputEditText etInput;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link Java variables to the XML layout elements
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);
        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnReset = findViewById(R.id.btnReset);

        // Listen for when the category changes to update the unit lists
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Determine which units to show based on what category is picked
                int unitArrayId;
                if (position == 1) { // Fuel/Distance
                    unitArrayId = R.array.fuel_distance_units;
                } else if (position == 2) { // Temp
                    unitArrayId = R.array.temperature_units;
                } else { // Currency
                    unitArrayId = R.array.currency_units;
                }

                // Set up the adapters for the spinners
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                        unitArrayId, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                
                spinnerFrom.setAdapter(adapter);
                spinnerTo.setAdapter(adapter);
                
                // Clear the result text when changing categories
                tvResult.setText(getString(R.string.default_result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not used
            }
        });

        // Set up what happens when the convert button is clicked
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = etInput.getText().toString().trim();
                
                // Check if the input is empty
                if (inputStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.error_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double inputValue = Double.parseDouble(inputStr);
                    int categoryPos = spinnerCategory.getSelectedItemPosition();
                    String fromUnit = spinnerFrom.getSelectedItem().toString();
                    String toUnit = spinnerTo.getSelectedItem().toString();

                    // Basic validation for negative values where they don't make sense
                    if ((categoryPos == 0 || categoryPos == 1) && inputValue < 0) {
                        Toast.makeText(MainActivity.this, R.string.error_negative, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double result = 0;
                    // Logic to decide which conversion method to call
                    if (categoryPos == 0) {
                        result = Converter.convertCurrency(inputValue, fromUnit, toUnit);
                    } else if (categoryPos == 1) {
                        result = Converter.convertFuelOrDistance(inputValue, fromUnit, toUnit);
                        if (result == -1) {
                            Toast.makeText(MainActivity.this, R.string.error_incompatible, Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else if (categoryPos == 2) {
                        result = Converter.convertTemperature(inputValue, fromUnit, toUnit);
                    }

                    // Display the final result with 2 decimal places
                    tvResult.setText(String.format(Locale.getDefault(), "%.2f", result));

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.error_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Clear button functionality
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
                tvResult.setText(getString(R.string.default_result));
                spinnerCategory.setSelection(0);
                Toast.makeText(MainActivity.this, R.string.reset_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
