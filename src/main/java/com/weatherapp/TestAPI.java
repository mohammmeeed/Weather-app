package com.weatherapp;

import com.weatherapp.service.WeatherApiClient;
import com.weatherapp.model.WeatherData;

public class TestAPI {
    public static void main(String[] args) {
        try {
            System.out.println("Testing Weather API...");
            WeatherApiClient client = new WeatherApiClient();
            WeatherData data = client.getCurrentWeather("Paris");
            System.out.println("SUCCESS!");
            System.out.println("City: " + data.getCityName());
            System.out.println("Temp: " + data.getTemperature());
            System.out.println("Desc: " + data.getDescription());
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
        }
    }
}
