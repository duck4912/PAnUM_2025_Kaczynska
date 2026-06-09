package com.example.wartosci_i_jednostki.converters;

import java.util.HashMap;
import java.util.Map;

public class LengthConverter {
    private static final Map<String, Double> toMeter = new HashMap<>();

    static {
        toMeter.put("mm", 0.001);
        toMeter.put("cm", 0.01);
        toMeter.put("in", 0.0254);
        toMeter.put("ft", 0.3048);
        toMeter.put("yd", 0.9144);
        toMeter.put("m", 1.0);
        toMeter.put("km", 1000.0);
    }

    public static double convert(double value, String from, String to) {
        double inMeters = value * toMeter.get(from);
        return inMeters / toMeter.get(to);
    }
    
    public static String[] getUnits() {
        return new String[]{"mm", "cm", "in", "ft", "yd", "m", "km"};
    }
}