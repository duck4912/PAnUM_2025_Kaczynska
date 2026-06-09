package com.example.wartosci_i_jednostki.converters;

import java.util.HashMap;
import java.util.Map;

public class AreaConverter {
    private static final Map<String, Double> toSqMeter = new HashMap<>();

    static {
        toSqMeter.put("mm²", 1e-6);
        toSqMeter.put("cm²", 1e-4);
        toSqMeter.put("m²", 1.0);
        toSqMeter.put("km²", 1e6);
        toSqMeter.put("ar", 100.0);
        toSqMeter.put("hektar", 10000.0);
    }

    public static double convert(double value, String from, String to) {
        double inSqMeters = value * toSqMeter.get(from);
        return inSqMeters / toSqMeter.get(to);
    }
    
    public static String[] getUnits() {
        return new String[]{"mm²", "cm²", "m²", "km²", "ar", "hektar"};
    }
}