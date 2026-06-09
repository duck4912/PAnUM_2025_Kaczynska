package com.example.wartosci_i_jednostki.converters;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    // Rates relative to 1 PLN
    private static final Map<String, Double> rates = new HashMap<>();

    static {
        rates.put("PLN", 1.0);
        rates.put("USD", 0.25);  // 1 PLN = 0.25 USD
        rates.put("EUR", 0.23);  // 1 PLN = 0.23 EUR
        rates.put("GBP", 0.20);  // 1 PLN = 0.20 GBP
        rates.put("JPY", 37.0);  // 1 PLN = 37.0 JPY
    }

    public static double convert(double amount, String from, String to) {
        double amountInPln = amount / rates.get(from);
        return amountInPln * rates.get(to);
    }
    
    public static String[] getCurrencies() {
        return rates.keySet().toArray(new String[0]);
    }
}