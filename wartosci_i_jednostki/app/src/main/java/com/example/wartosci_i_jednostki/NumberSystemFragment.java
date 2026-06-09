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
import com.example.wartosci_i_jednostki.converters.NumberSystemConverter;

public class NumberSystemFragment extends Fragment {

    private EditText editTextValue;
    private Spinner spinnerFrom, spinnerTo;
    private TextView textViewResult;
    private final String[] systems = {"DEC", "BIN", "BASE-4", "OCT", "HEX"};
    private final int[] radices = {10, 2, 4, 8, 16};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        editTextValue = view.findViewById(R.id.editTextValue);
        spinnerFrom = view.findViewById(R.id.spinnerFrom);
        spinnerTo = view.findViewById(R.id.spinnerTo);
        textViewResult = view.findViewById(R.id.textViewResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, systems);
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

        int fromRadix = radices[spinnerFrom.getSelectedItemPosition()];
        int toRadix = radices[spinnerTo.getSelectedItemPosition()];

        if (!NumberSystemConverter.isValid(input, fromRadix)) {
            textViewResult.setText("Błędne dane dla systemu " + systems[spinnerFrom.getSelectedItemPosition()]);
            textViewResult.setTextColor(android.graphics.Color.RED);
            return;
        }

        try {
            String result = NumberSystemConverter.convert(input, fromRadix, toRadix);
            textViewResult.setText(result);
            textViewResult.setTextColor(android.graphics.Color.BLACK);
        } catch (Exception e) {
            textViewResult.setText("Błąd konwersji");
            textViewResult.setTextColor(android.graphics.Color.RED);
        }
    }
}