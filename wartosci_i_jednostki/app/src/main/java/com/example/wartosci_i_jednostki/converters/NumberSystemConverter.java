package com.example.wartosci_i_jednostki.converters;

import java.math.BigInteger;

public class NumberSystemConverter {

    public static String convert(String value, int fromRadix, int toRadix) throws NumberFormatException {
        if (value == null || value.isEmpty()) return "";
        
        // Handle real numbers if possible, otherwise stick to integers for complex bases
        if (value.contains(".")) {
            String[] parts = value.split("\\.");
            String intPart = parts[0];
            String fracPart = parts.length > 1 ? parts[1] : "";
            
            double decimalValue = 0;
            if (!intPart.isEmpty()) {
                decimalValue = Long.parseLong(intPart, fromRadix);
            }
            
            for (int i = 0; i < fracPart.length(); i++) {
                int digit = Character.digit(fracPart.charAt(i), fromRadix);
                if (digit == -1) throw new NumberFormatException("Invalid digit for radix");
                decimalValue += digit / Math.pow(fromRadix, i + 1);
            }
            
            return formatDecimalToRadix(decimalValue, toRadix);
        } else {
            BigInteger decimalValue = new BigInteger(value, fromRadix);
            return decimalValue.toString(toRadix).toUpperCase();
        }
    }

    private static String formatDecimalToRadix(double decimalValue, int radix) {
        long intPart = (long) decimalValue;
        double fracPart = decimalValue - intPart;
        
        String intPartStr = Long.toString(intPart, radix).toUpperCase();
        if (fracPart == 0) return intPartStr;
        
        StringBuilder sb = new StringBuilder(intPartStr);
        sb.append(".");
        
        for (int i = 0; i < 10; i++) { // limit to 10 decimal places
            fracPart *= radix;
            int digit = (int) fracPart;
            sb.append(Integer.toString(digit, radix).toUpperCase());
            fracPart -= digit;
            if (fracPart == 0) break;
        }
        
        return sb.toString();
    }
    
    public static boolean isValid(String value, int radix) {
        if (value.isEmpty()) return true;
        String validChars = "0123456789ABCDEF".substring(0, radix) + ".";
        for (char c : value.toUpperCase().toCharArray()) {
            if (validChars.indexOf(c) == -1) return false;
        }
        // Check if there's more than one dot
        if (value.indexOf('.') != value.lastIndexOf('.')) return false;
        return true;
    }
}