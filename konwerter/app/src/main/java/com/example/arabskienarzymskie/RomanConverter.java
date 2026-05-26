package com.example.arabskienarzymskie;

import java.util.TreeMap;
import java.util.regex.Pattern;

public class RomanConverter {
    private static final TreeMap<Integer, String> arabicToRomanMap = new TreeMap<>();
    private static final String ROMAN_REGEX = "^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

    static {
        arabicToRomanMap.put(1000, "M");
        arabicToRomanMap.put(900, "CM");
        arabicToRomanMap.put(500, "D");
        arabicToRomanMap.put(400, "CD");
        arabicToRomanMap.put(100, "C");
        arabicToRomanMap.put(90, "XC");
        arabicToRomanMap.put(50, "L");
        arabicToRomanMap.put(40, "XL");
        arabicToRomanMap.put(10, "X");
        arabicToRomanMap.put(9, "IX");
        arabicToRomanMap.put(5, "V");
        arabicToRomanMap.put(4, "IV");
        arabicToRomanMap.put(1, "I");
    }

    public String arabicToRoman(int number) {
        if (number < 1 || number > 3999) {
            return "Zakres: 1-3999";
        }
        Integer l = arabicToRomanMap.floorKey(number);
        if (l == null) return "";
        if (number == l) {
            return arabicToRomanMap.get(number);
        }
        return arabicToRomanMap.get(l) + arabicToRoman(number - l);
    }

    public int romanToArabic(String roman) {
        if (roman == null || roman.isEmpty() || !isValidRoman(roman)) {
            return -1;
        }

        int result = 0;
        int lastValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = getValue(roman.charAt(i));
            if (currentValue < lastValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }
            lastValue = currentValue;
        }
        return result;
    }

    public boolean isValidRoman(String roman) {
        return Pattern.matches(ROMAN_REGEX, roman) && !roman.isEmpty();
    }

    private int getValue(char r) {
        switch (r) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }
}
