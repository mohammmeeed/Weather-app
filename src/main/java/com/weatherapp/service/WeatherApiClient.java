package com.weatherapp.service;

import com.weatherapp.model.WeatherData;
import com.weatherapp.util.WeatherParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service class responsible for communicating with the Weather REST API.
 */
public class WeatherApiClient {
    
    // Using OpenWeatherMap API for this example
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    
    // Hardcoded for demo purposes. In production, read from System.getenv("WEATHER_API_KEY")
    // NOTE: This is a placeholder. You will need a valid OpenWeather API key to fetch real data!
    private static final String API_KEY = "90201aa5d72f08b1b1b89fa2914d1b69"; 
    
    // Timeouts in milliseconds
    private static final int CONNECT_TIMEOUT = 5000; // 5 seconds
    private static final int READ_TIMEOUT = 5000;    // 5 seconds

    /**
     * Fetches the current weather for a specific city.
     * 
     * @param city The name of the city (e.g., "Berlin")
     * @return A parsed WeatherData object
     * @throws Exception If a network or server error occurs
     */
    public WeatherData getCurrentWeather(String city) throws Exception {
        
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API Key is missing! Check your configuration.");
        }

        // URL encode the city to handle spaces (e.g. "New York")
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        // Build the endpoint URL with query parameters (using units=metric for Celsius)
        String urlString = BASE_URL + "?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("Accept", "application/json");

            int status = connection.getResponseCode();

            if (status < 200 || status >= 300) {
                handleErrorResponse(status);
            }

            String jsonResponse = readResponseStream(connection);
            
            // Delegate to our Parser
            return WeatherParser.parse(jsonResponse);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readResponseStream(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private void handleErrorResponse(int status) throws Exception {
        if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new Exception("401 Unauthorized: Invalid API Key. Please replace the placeholder key.");
        } else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new Exception("404 Not Found: The city was not recognized by the API.");
        } else if (status >= 500) {
            throw new Exception("500 Server Error: The Weather API is currently down.");
        } else {
            throw new Exception("Server returned HTTP response code: " + status);
        }
    }
}
