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

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    TextInputEditText etInput;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);

        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnReset = findViewById(R.id.btnReset);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int arrayId;
                if (position == 1) {
                    arrayId = R.array.fuel_distance_units;
                } else if (position == 2) {
                    arrayId = R.array.temperature_units;
                } else {
                    arrayId = R.array.currency_units;
                }

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                        arrayId, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerFrom.setAdapter(adapter);
                spinnerTo.setAdapter(adapter);
                tvResult.setText("0.00");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = etInput.getText().toString().trim();

                if (val.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double input = Double.parseDouble(val);
                    int cat = spinnerCategory.getSelectedItemPosition();
                    String from = spinnerFrom.getSelectedItem().toString();
                    String to = spinnerTo.getSelectedItem().toString();

                    if (from.equals(to)) {
                        Toast.makeText(MainActivity.this, "Same units", Toast.LENGTH_SHORT).show();
                        tvResult.setText(String.format(Locale.getDefault(), "%.2f", input));
                        return;
                    }

                    if (cat != 2 && input < 0) {
                        Toast.makeText(MainActivity.this, "No negative values", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double res = 0;
                    if (cat == 0) {
                        res = Converter.convertCurrency(input, from, to);
                    } else if (cat == 1) {
                        res = Converter.convertFuelOrDistance(input, from, to);
                        if (res == -1) {
                            Toast.makeText(MainActivity.this, "Wrong units", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        res = Converter.convertTemperature(input, from, to);
                    }

                    tvResult.setText(String.format(Locale.getDefault(), "%.2f", res));

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
                tvResult.setText("0.00");
                spinnerCategory.setSelection(0);
                Toast.makeText(MainActivity.this, "Reset done", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
