package com.weatherapp.ui;

import com.weatherapp.model.WeatherData;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapWindow {

    public static void showMap(WeatherData data) {
        String cityName = data.getCityName();
        double temp = data.getTemperature();
        double humidity = data.getHumidity();
        String desc = data.getDescription();
        double lat = data.getLatitude();
        double lon = data.getLongitude();

        // 1. Generate the HTML content with Leaflet.js map and weather info bar
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Map - " + cityName + "</title>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <!-- Leaflet CSS -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />\n" +
                "    <style>\n" +
                "        html, body {\n" +
                "            height: 100%;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background-color: #0F172A;\n" +
                "            color: #F8FAFC;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        #map {\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "            position: absolute;\n" +
                "            top: 0;\n" +
                "            left: 0;\n" +
                "            z-index: 1;\n" +
                "        }\n" +
                "        /* Floating Weather Bar at the top */\n" +
                "        .weather-bar {\n" +
                "            position: absolute;\n" +
                "            top: 20px;\n" +
                "            left: 50%;\n" +
                "            transform: translateX(-50%);\n" +
                "            z-index: 1000;\n" +
                "            background: rgba(30, 41, 59, 0.95);\n" +
                "            border: 1px solid #334155;\n" +
                "            border-radius: 12px;\n" +
                "            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.5), 0 8px 10px -6px rgba(0, 0, 0, 0.5);\n" +
                "            padding: 15px 30px;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            gap: 25px;\n" +
                "            backdrop-filter: blur(8px);\n" +
                "        }\n" +
                "        .weather-item {\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "        }\n" +
                "        .weather-label {\n" +
                "            font-size: 11px;\n" +
                "            text-transform: uppercase;\n" +
                "            letter-spacing: 0.05em;\n" +
                "            color: #94A3B8;\n" +
                "            margin-bottom: 2px;\n" +
                "        }\n" +
                "        .weather-value {\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            color: #F8FAFC;\n" +
                "        }\n" +
                "        .city-name {\n" +
                "            color: #38BDF8; /* Accent blue */\n" +
                "        }\n" +
                "        .divider {\n" +
                "            width: 1px;\n" +
                "            height: 30px;\n" +
                "            background-color: #334155;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <div class=\"weather-bar\">\n" +
                "        <div class=\"weather-item\">\n" +
                "            <span class=\"weather-label\">Ville</span>\n" +
                "            <span class=\"weather-value city-name\">" + cityName + "</span>\n" +
                "        </div>\n" +
                "        <div class=\"divider\"></div>\n" +
                "        <div class=\"weather-item\">\n" +
                "            <span class=\"weather-label\">Température</span>\n" +
                "            <span class=\"weather-value\">" + String.format("%.1f", temp) + " °C</span>\n" +
                "        </div>\n" +
                "        <div class=\"divider\"></div>\n" +
                "        <div class=\"weather-item\">\n" +
                "            <span class=\"weather-label\">Humidité</span>\n" +
                "            <span class=\"weather-value\">" + (int)humidity + "%</span>\n" +
                "        </div>\n" +
                "        <div class=\"divider\"></div>\n" +
                "        <div class=\"weather-item\">\n" +
                "            <span class=\"weather-label\">Météo</span>\n" +
                "            <span class=\"weather-value\" style=\"text-transform: capitalize;\">" + desc + "</span>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div id=\"map\"></div>\n" +
                "\n" +
                "    <!-- Leaflet JS -->\n" +
                "    <script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\"></script>\n" +
                "    <script>\n" +
                "        // Initialize map centered at latitude/longitude\n" +
                "        var map = L.map('map', {zoomControl: false}).setView([" + lat + ", " + lon + "], 12);\n" +
                "\n" +
                "        // Add Zoom control at bottom right\n" +
                "        L.control.zoom({ position: 'bottomright' }).addTo(map);\n" +
                "\n" +
                "        // Add OpenStreetMap tile layer (dark theme: CartoDB Dark Matter)\n" +
                "        L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {\n" +
                "            attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors &copy; <a href=\"https://carto.com/attributions\">CARTO</a>',\n" +
                "            subdomains: 'abcd',\n" +
                "            maxZoom: 20\n" +
                "        }).addTo(map);\n" +
                "\n" +
                "        // Custom marker icon using standard Leaflet marker styled with custom popup\n" +
                "        var marker = L.marker([" + lat + ", " + lon + "]).addTo(map);\n" +
                "        marker.bindPopup(\"<b>" + cityName + "</b><br>" + String.format("%.1f", temp) + " °C, " + desc + "\").openPopup();\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        // 2. Write content to a temp HTML file
        try {
            File tempFile = File.createTempFile("weather-map-", ".html");
            tempFile.deleteOnExit(); // Clean up on exit
            
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(htmlContent);
            }

            // 3. Open the file in the default system browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(tempFile.toURI());
            } else {
                System.err.println("Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
