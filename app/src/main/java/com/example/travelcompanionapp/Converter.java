package com.example.travelcompanionapp;

/**
 * Utility class providing conversion logic for Currency, Fuel/Distance, and Temperature.
 * All methods are static for easy access throughout the application.
 */
public class Converter {

    // --- Currency Conversion Constants (Base: USD) ---
    private static final double USD_TO_AUD = 1.55;
    private static final double USD_TO_EUR = 0.92;
    private static final double USD_TO_JPY = 148.50;
    private static final double USD_TO_GBP = 0.78;

    // --- Fuel and Distance Conversion Constants ---
    private static final double MPG_TO_KML_FACTOR = 0.425144;
    private static final double GALLON_TO_LITER_FACTOR = 3.78541;
    private static final double NAUTICAL_MILE_TO_KM_FACTOR = 1.852;

    // --- Temperature Constants ---
    private static final double TEMP_OFFSET_KELVIN = 273.15;
    private static final double TEMP_SCALE_FAHRENHEIT = 1.8;
    private static final double TEMP_OFFSET_FAHRENHEIT = 32.0;

    /**
     * Converts an amount from one currency to another using USD as an intermediate base.
     *
     * @param amount The value to convert.
     * @param from   The source currency code (e.g., "USD", "AUD", "EUR", "JPY", "GBP").
     * @param to     The target currency code.
     * @return The converted amount.
     */
    public static double convertCurrency(double amount, String from, String to) {
        if (from.equals(to)) return amount;

        // Step 1: Convert source currency to USD (Base)
        double inUSD;
        switch (from) {
            case "AUD": inUSD = amount / USD_TO_AUD; break;
            case "EUR": inUSD = amount / USD_TO_EUR; break;
            case "JPY": inUSD = amount / USD_TO_JPY; break;
            case "GBP": inUSD = amount / USD_TO_GBP; break;
            default:    inUSD = amount; // Assume USD if not matched
        }

        // Step 2: Convert USD to target currency
        switch (to) {
            case "AUD": return inUSD * USD_TO_AUD;
            case "EUR": return inUSD * USD_TO_EUR;
            case "JPY": return inUSD * USD_TO_JPY;
            case "GBP": return inUSD * USD_TO_GBP;
            default:    return inUSD; // Assume USD if not matched
        }
    }

    /**
     * Handles specific conversions for Fuel Efficiency, Volume, and Distance.
     *
     * @param value The value to convert.
     * @param from  The source unit description.
     * @param to    The target unit description.
     * @return The converted value, or -1.0 if the units are incompatible or unknown.
     */
    public static double convertFuelOrDistance(double value, String from, String to) {
        if (from.equals(to)) return value;

        // Fuel Efficiency: MPG <-> km/L
        if (from.equals("Miles per Gallon (mpg)") && to.equals("Kilometers per Liter (km/L)")) {
            return value * MPG_TO_KML_FACTOR;
        }
        if (from.equals("Kilometers per Liter (km/L)") && to.equals("Miles per Gallon (mpg)")) {
            return value / MPG_TO_KML_FACTOR;
        }

        // Volume: Gallon <-> Liter
        if (from.equals("Gallon (US)") && to.equals("Liter")) {
            return value * GALLON_TO_LITER_FACTOR;
        }
        if (from.equals("Liter") && to.equals("Gallon (US)")) {
            return value / GALLON_TO_LITER_FACTOR;
        }

        // Distance: Nautical Mile <-> Kilometer
        if (from.equals("Nautical Mile") && to.equals("Kilometer")) {
            return value * NAUTICAL_MILE_TO_KM_FACTOR;
        }
        if (from.equals("Kilometer") && to.equals("Nautical Mile")) {
            return value / NAUTICAL_MILE_TO_KM_FACTOR;
        }

        return -1.0; // Indicates incompatible units
    }

    /**
     * Converts temperature between Celsius, Fahrenheit, and Kelvin.
     *
     * @param value The temperature value.
     * @param from  The source unit ("Celsius", "Fahrenheit", "Kelvin").
     * @param to    The target unit.
     * @return The converted temperature.
     */
    public static double convertTemperature(double value, String from, String to) {
        if (from.equals(to)) return value;

        // Step 1: Normalize source to Celsius
        double celsius;
        switch (from) {
            case "Fahrenheit":
                celsius = (value - TEMP_OFFSET_FAHRENHEIT) / TEMP_SCALE_FAHRENHEIT;
                break;
            case "Kelvin":
                celsius = value - TEMP_OFFSET_KELVIN;
                break;
            default:
                celsius = value; // Already Celsius
                break;
        }

        // Step 2: Convert Celsius to target unit
        switch (to) {
            case "Fahrenheit":
                return (celsius * TEMP_SCALE_FAHRENHEIT) + TEMP_OFFSET_FAHRENHEIT;
            case "Kelvin":
                return celsius + TEMP_OFFSET_KELVIN;
            default:
                return celsius;
        }
    }
}
