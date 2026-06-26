package com.weatherapp;

import com.weatherapp.controller.WeatherController;
import com.weatherapp.service.WeatherApiClient;
import com.weatherapp.ui.WeatherPanel;

import javax.swing.*;

public class WeatherApp {
    public static void main(String[] args) {
        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Apply the native Windows/Mac look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            JFrame frame = new JFrame("Professional Weather App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(450, 400);
            frame.setLocationRelativeTo(null); // Center on screen

            // Dependency Injection
            WeatherApiClient apiClient = new WeatherApiClient();
            WeatherPanel view = new WeatherPanel();
            
            // Connect them using the Controller
            new WeatherController(view, apiClient);

            frame.add(view);
            frame.setVisible(true);
        });
    }
}
