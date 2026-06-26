package com.weatherapp.controller;

import com.weatherapp.model.WeatherData;
import com.weatherapp.service.WeatherApiClient;
import com.weatherapp.ui.WeatherPanel;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class WeatherController {
    
    private final WeatherPanel view;
    private final WeatherApiClient apiClient;

    public WeatherController(WeatherPanel view, WeatherApiClient apiClient) {
        this.view = view;
        this.apiClient = apiClient;
        
        // Listen to the button click from the View
        this.view.getSearchButton().addActionListener(e -> fetchWeather());
        this.view.getViewMapButton().addActionListener(e -> showMapWindow());
    }

    private void fetchWeather() {
        String city = view.getCityInput();
        if (city == null || city.trim().isEmpty()) {
            view.showError("Please enter a city name.");
            return;
        }

        // Show a loading state so the user knows something is happening
        view.setLoading(true);

        // Run the API call in the background using SwingWorker
        SwingWorker<WeatherData, Void> worker = new SwingWorker<WeatherData, Void>() {
            @Override
            protected WeatherData doInBackground() throws Exception {
                // This runs on a background thread. NO UI updates allowed here!
                return apiClient.getCurrentWeather(city);
            }

            @Override
            protected void done() {
                // This runs on the main UI thread when the background task finishes
                view.setLoading(false);
                try {
                    WeatherData data = get();
                    view.updateWeatherDisplay(data);
                } catch (ExecutionException e) {
                    // Extract the actual error thrown by our API Client
                    Throwable cause = e.getCause();
                    view.showError(cause.getMessage());
                } catch (InterruptedException e) {
                    view.showError("The request was interrupted.");
                }
            }
        };
        
        worker.execute();
    }

    private void showMapWindow() {
        WeatherData data = view.getCurrentWeatherData();
        if (data == null) {
            return;
        }
        com.weatherapp.ui.MapWindow.showMap(data);
    }
}
