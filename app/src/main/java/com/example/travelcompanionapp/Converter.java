package com.example.travelcompanionapp;

// This class has all the math for the conversions
public class Converter {

    // Conversion rates for currency (USD is the base)
    private static final double USD_TO_AUD = 1.55;
    private static final double USD_TO_EUR = 0.92;
    private static final double USD_TO_JPY = 148.50;
    private static final double USD_TO_GBP = 0.78;

    // Currency conversion method
    public static double convertCurrency(double amount, String from, String to) {
        if (from.equals(to)) return amount;

        // Convert the input amount to USD first
        double inUSD;
        if (from.equals("AUD")) {
            inUSD = amount / USD_TO_AUD;
        } else if (from.equals("EUR")) {
            inUSD = amount / USD_TO_EUR;
        } else if (from.equals("JPY")) {
            inUSD = amount / USD_TO_JPY;
        } else if (from.equals("GBP")) {
            inUSD = amount / USD_TO_GBP;
        } else {
            inUSD = amount; // It's already USD
        }

        // Then convert from USD to the target currency
        if (to.equals("AUD")) return inUSD * USD_TO_AUD;
        if (to.equals("EUR")) return inUSD * USD_TO_EUR;
        if (to.equals("JPY")) return inUSD * USD_TO_JPY;
        if (to.equals("GBP")) return inUSD * USD_TO_GBP;
        
        return inUSD;
    }

    // Distance and Fuel logic
    public static double convertFuelOrDistance(double value, String from, String to) {
        if (from.equals(to)) return value;

        // Fuel Efficiency
        if (from.equals("Miles per Gallon (mpg)") && to.equals("Kilometers per Liter (km/L)")) {
            return value * 0.425;
        } 
        if (from.equals("Kilometers per Liter (km/L)") && to.equals("Miles per Gallon (mpg)")) {
            return value / 0.425;
        }

        // Volume logic
        if (from.equals("Gallon (US)") && to.equals("Liter")) return value * 3.785;
        if (from.equals("Liter") && to.equals("Gallon (US)")) return value / 3.785;

        // Distance logic
        if (from.equals("Nautical Mile") && to.equals("Kilometer")) return value * 1.852;
        if (from.equals("Kilometer") && to.equals("Nautical Mile")) return value / 1.852;

        return -1; // Wrong units
    }

    // Temperature logic
    public static double convertTemperature(double value, String from, String to) {
        if (from.equals(to)) return value;

        double celsius;
        // Turn everything into Celsius first
        if (from.equals("Fahrenheit")) {
            celsius = (value - 32) / 1.8;
        } else if (from.equals("Kelvin")) {
            celsius = value - 273.15;
        } else {
            celsius = value;
        }

        // Convert Celsius to the final unit
        if (to.equals("Fahrenheit")) return (celsius * 1.8) + 32;
        if (to.equals("Kelvin")) return celsius + 273.15;
        
        return celsius;
    }
}
