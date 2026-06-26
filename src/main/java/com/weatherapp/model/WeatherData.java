package com.weatherapp.model;

/**
 * A simple domain model representing weather data.
 */
public class WeatherData {
    private final String cityName;
    private final double temperature;
    private final double humidity;
    private final String description;
    private final double latitude;
    private final double longitude;

    public WeatherData(String cityName, double temperature, double humidity, String description, double latitude, double longitude) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.humidity = humidity;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
