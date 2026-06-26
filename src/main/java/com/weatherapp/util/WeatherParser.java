package com.weatherapp.util;

import com.weatherapp.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class responsible for safely parsing raw JSON strings from the Weather API.
 */
public class WeatherParser {

    /**
     * Parses the raw JSON response into a WeatherData object.
     * 
     * @param jsonResponse The raw JSON string returned by the API
     * @return A populated WeatherData object
     * @throws JSONException If the JSON is completely malformed or missing critical structural nodes
     */
    public static WeatherData parse(String jsonResponse) throws JSONException {
        // 1. Convert the raw string into the root JSONObject
        JSONObject rootObject = new JSONObject(jsonResponse);

        // 2. Extract Top-Level Primitives (with a fallback)
        String cityName = rootObject.optString("name", "Unknown City");

        // 3. Extract Nested Objects (The "main" block)
        JSONObject mainBlock = rootObject.getJSONObject("main");
        
        // We use optDouble so the app doesn't crash if temp/humidity are temporarily unavailable
        double temp = mainBlock.optDouble("temp", 0.0);
        double humidity = mainBlock.optDouble("humidity", 0.0);

        // 4. Extract Data from JSONArrays (The "weather" block)
        String description = "No description available";
        
        // Check if the array exists to avoid NullPointerExceptions
        if (rootObject.has("weather") && !rootObject.isNull("weather")) {
            JSONArray weatherArray = rootObject.getJSONArray("weather");
            
            // Ensure the array is not empty before trying to access index 0
            if (weatherArray.length() > 0) {
                JSONObject firstWeatherItem = weatherArray.getJSONObject(0);
                description = firstWeatherItem.optString("description", "No description");
            }
        }

        // Extract coordinates (coord block)
        double lat = 0.0;
        double lon = 0.0;
        if (rootObject.has("coord") && !rootObject.isNull("coord")) {
            JSONObject coordBlock = rootObject.getJSONObject("coord");
            lat = coordBlock.optDouble("lat", 0.0);
            lon = coordBlock.optDouble("lon", 0.0);
        }

        // 5. Construct and return our clean Domain Model
        return new WeatherData(cityName, temp, humidity, description, lat, lon);
    }
}
