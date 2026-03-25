### Conversion Categories
*   **Money:** You can convert between USD, AUD, EUR, JPY, and GBP. I used a "Base Currency" logic (converting everything to USD first) to make the math more efficient.
*   **On the Road (Fuel & Distance):** 
    *   Check fuel efficiency (MPG to km/L).
    *   Convert between Gallons and Liters.
    *   Convert Nautical Miles to Kilometers.
*   **Weather (Temperature):** Supports Celsius, Fahrenheit, and Kelvin.

### Key Features
*   **Clean UI:** I used Material Design cards and custom backgrounds to make it look modern and professional.
*   **Smart Pickers:** When you change the category at the top, the unit lists below automatically update so you don't accidentally try to convert USD to Celsius.
*   **Result Formatting:** No long decimals—everything is rounded to two decimal places so it's easy to read.
*   **One-Tap Reset:** The 'Clear' button resets everything so you can start a new calculation instantly.

## Handling Errors & Edge Cases
I spent a lot of time making sure the app doesn't crash if the user does something unexpected:
*   **Empty Inputs:** If you forget to enter a number, a Toast message pops up to let you know.
*   **Same-Unit Check:** If you try to convert USD to USD, the app tells you it's the same unit instead of wasting time on math.
*   **Invalid Characters:** I added a `try-catch` block so if someone tries to enter something that isn't a number, the app handles it gracefully.
*   **Safety First:** The app blocks negative numbers for money and distance (since they don't make sense), but allows them for temperature.

## Behind the Scenes
*   **MainActivity.java:** This is where I handle all the buttons, dropdowns, and user input.
*   **Converter.java:** I moved all the math logic here to keep the code organized.
*   **XML Layouts:** I used a mix of `CoordinatorLayout` and `MaterialCardView` for the design.

## How to Try It
1. Open this project in **Android Studio**.
2. Hit the "Run" button to launch it on an emulator or your phone.
3. (Requires Android 7.0 or higher).
