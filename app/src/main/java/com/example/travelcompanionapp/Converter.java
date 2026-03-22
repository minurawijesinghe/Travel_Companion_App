package com.example.travelcompanionapp;

/**
 * Converter class handles all unit conversions:
 * - Currency
 * - Fuel efficiency & distance
 * - Temperature
 */
public class Converter {

    // =======================
    // Currency Conversion Rates (Fixed 2026)
    // =======================
    private static final double USD_TO_AUD = 1.55;
    private static final double USD_TO_EUR = 0.92;
    private static final double USD_TO_JPY = 148.50;
    private static final double USD_TO_GBP = 0.78;

    // =======================
    // Fuel & Distance Conversion Factors
    // =======================
    private static final double MPG_TO_KML_FACTOR = 0.425;
    private static final double GALLON_TO_LITER_FACTOR = 3.785;
    private static final double NAUTICAL_MILE_TO_KM_FACTOR = 1.852;

    // =======================
    // Temperature Constants
    // =======================
    private static final double TEMP_OFFSET_KELVIN = 273.15;
    private static final double TEMP_SCALE_FAHRENHEIT = 1.8;
    private static final double TEMP_OFFSET_FAHRENHEIT = 32.0;

    /**
     * Converts currency using USD as an intermediate base.
     *
     * @param amount value to convert
     * @param from   source currency (e.g., "AUD")
     * @param to     target currency (e.g., "USD")
     * @return converted value
     */
    public static double convertCurrency(double amount, String from, String to) {

        // If both units are the same, return original value
        if (from.equals(to)) return amount;

        // Step 1: Convert input currency to USD
        double inUSD;
        switch (from) {
            case "AUD":
                inUSD = amount / USD_TO_AUD;
                break;
            case "EUR":
                inUSD = amount / USD_TO_EUR;
                break;
            case "JPY":
                inUSD = amount / USD_TO_JPY;
                break;
            case "GBP":
                inUSD = amount / USD_TO_GBP;
                break;
            default:
                // If already USD
                inUSD = amount;
        }

        // Step 2: Convert USD to target currency
        switch (to) {
            case "AUD":
                return inUSD * USD_TO_AUD;
            case "EUR":
                return inUSD * USD_TO_EUR;
            case "JPY":
                return inUSD * USD_TO_JPY;
            case "GBP":
                return inUSD * USD_TO_GBP;
            default:
                // If target is USD
                return inUSD;
        }
    }

    /**
     * Converts fuel efficiency, volume, and distance units.
     *
     * @param value input value
     * @param from  source unit
     * @param to    target unit
     * @return converted value or -1.0 if unsupported conversion
     */
    public static double convertFuelOrDistance(double value, String from, String to) {

        // If both units are the same, return original value
        if (from.equals(to)) return value;

        // =======================
        // Fuel Efficiency Conversion
        // =======================
        if (from.equals("Miles per Gallon (mpg)") && to.equals("Kilometers per Liter (km/L)")) {
            return value * MPG_TO_KML_FACTOR;
        }
        if (from.equals("Kilometers per Liter (km/L)") && to.equals("Miles per Gallon (mpg)")) {
            return value / MPG_TO_KML_FACTOR;
        }

        // =======================
        // Volume Conversion
        // =======================
        if (from.equals("Gallon (US)") && to.equals("Liter")) {
            return value * GALLON_TO_LITER_FACTOR;
        }
        if (from.equals("Liter") && to.equals("Gallon (US)")) {
            return value / GALLON_TO_LITER_FACTOR;
        }

        // =======================
        // Distance Conversion
        // =======================
        if (from.equals("Nautical Mile") && to.equals("Kilometer")) {
            return value * NAUTICAL_MILE_TO_KM_FACTOR;
        }
        if (from.equals("Kilometer") && to.equals("Nautical Mile")) {
            return value / NAUTICAL_MILE_TO_KM_FACTOR;
        }

        // Return -1 if conversion is not supported
        return -1.0;
    }

    /**
     * Converts temperature between Celsius, Fahrenheit, and Kelvin.
     * Uses Celsius as an intermediate base.
     *
     * @param value input temperature
     * @param from  source unit
     * @param to    target unit
     * @return converted temperature
     */
    public static double convertTemperature(double value, String from, String to) {

        // If both units are the same, return original value
        if (from.equals(to)) return value;

        // Step 1: Convert input temperature to Celsius
        double celsius;
        switch (from) {
            case "Fahrenheit":
                celsius = (value - TEMP_OFFSET_FAHRENHEIT) / TEMP_SCALE_FAHRENHEIT;
                break;
            case "Kelvin":
                celsius = value - TEMP_OFFSET_KELVIN;
                break;
            default:
                // Assume input is already Celsius
                celsius = value;
        }

        // Step 2: Convert Celsius to target unit
        switch (to) {
            case "Fahrenheit":
                return (celsius * TEMP_SCALE_FAHRENHEIT) + TEMP_OFFSET_FAHRENHEIT;
            case "Kelvin":
                return celsius + TEMP_OFFSET_KELVIN;
            default:
                // Return Celsius if target is Celsius
                return celsius;
        }
    }
}