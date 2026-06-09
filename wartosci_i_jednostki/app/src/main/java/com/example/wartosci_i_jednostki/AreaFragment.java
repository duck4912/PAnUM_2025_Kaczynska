package com.example.wartosci_i_jednostki;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.wartosci_i_jednostki.converters.AreaConverter;
import java.util.Locale;

public class AreaFragment extends Fragment {

    private EditText editTextValue;
    private Spinner spinnerFrom, spinnerTo;
    private TextView textViewResult;
    private String[] units;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        editTextValue = view.findViewById(R.id.editTextValue);
        spinnerFrom = view.findViewById(R.id.spinnerFrom);
        spinnerTo = view.findViewById(R.id.spinnerTo);
        textViewResult = view.findViewById(R.id.textViewResult);
        
        editTextValue.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        units = AreaConverter.getUnits();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convert();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        editTextValue.addTextChangedListener(watcher);
        
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerFrom.setOnItemSelectedListener(itemSelectedListener);
        spinnerTo.setOnItemSelectedListener(itemSelectedListener);

        return view;
    }

    private void convert() {
        String input = editTextValue.getText().toString().trim();
        if (input.isEmpty()) {
            textViewResult.setText("");
            return;
        }

        try {
            double value = Double.parseDouble(input);
            String from = units[spinnerFrom.getSelectedItemPosition()];
            String to = units[spinnerTo.getSelectedItemPosition()];
            double result = AreaConverter.convert(value, from, to);
            textViewResult.setText(String.format(Locale.getDefault(), "%.6f", result));
            textViewResult.setTextColor(android.graphics.Color.BLACK);
        } catch (NumberFormatException e) {
            textViewResult.setText("Błędna liczba");
            textViewResult.setTextColor(android.graphics.Color.RED);
        }
    }
}