package com.example.backgroundrecorder;

public class StringHelper {
    public static String padNumberWithZeros(int number, int paddingLength) {
        String numberString = String.valueOf(number);

        if (numberString.length() >= paddingLength) {
            return numberString;
        }

        StringBuilder paddedNumber = new StringBuilder();
        int numZeros = paddingLength - numberString.length();

        for (int i = 0; i < numZeros; i++) {
            paddedNumber.append("0");
        }

        paddedNumber.append(numberString);
        return paddedNumber.toString();
    }
}
