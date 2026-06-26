package com.weatherapp.ui;

import com.weatherapp.model.WeatherData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherPanel extends JPanel {

    // --- Design System Colors ---
    private static final Color BG_MAIN = new Color(15, 23, 42);       // #0F172A
    private static final Color BG_CARD = new Color(30, 41, 59);       // #1E293B
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252); // #F8FAFC
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184); // #94A3B8
    private static final Color ACCENT_BLUE = new Color(56, 189, 248);   // #38BDF8
    private static final Color BORDER_COLOR = new Color(51, 65, 85);    // #334155

    private JTextField txtCity;
    private JButton btnSearch;
    private JPopupMenu suggestionPopup;
    private boolean disableSuggestions = false;
    private JButton btnViewMap;
    private WeatherData currentWeatherData;

    private static final List<String> SUGGESTED_CITIES = Arrays.asList(
        "Paris", "London", "Londres", "New York", "Tokyo", "Berlin", "Rome", "Madrid", 
        "Sydney", "Toronto", "Montreal", "Montréal", "Casablanca", "Alger", "Tunis", 
        "Le Caire", "Cairo", "Moscou", "Moscow", "Pékin", "Beijing", "Mumbai", 
        "Rio de Janeiro", "Los Angeles", "Chicago", "San Francisco", "Miami", "Seattle", 
        "Boston", "Washington", "Vancouver", "Barcelone", "Barcelona", "Munich", 
        "München", "Frankfurt", "Francfort", "Vienna", "Vienne", "Zurich", "Zürich", 
        "Amsterdam", "Brussels", "Bruxelles", "Geneva", "Genève", "Dubai", "Dubaï", 
        "Singapore", "Singapour", "Bangkok", "Seoul", "Séoul", "Hong Kong", "Shanghai", 
        "Cape Town", "Le Cap", "Nairobi", "Dakar", "Abidjan", "Marrakech", "Agadir", 
        "Fez", "Fès", "Tangier", "Tanger", "Oran", "Constantine", "Sousse", "Monastir", 
        "Nice", "Lyon", "Marseille", "Toulouse", "Bordeaux", "Lille", "Strasbourg", "Nantes"
    );

    // Dynamic Labels for Main Card
    private JLabel lblMainCity;
    private JLabel lblMainTemp;
    private JLabel lblMainDesc;
    
    // Dynamic Labels for Small Cards
    private JLabel lblHumidityValue;
    private JLabel lblStatusValue;

    public WeatherPanel() {
        suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);

        setLayout(new BorderLayout(0, 20));
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(20, 30, 30, 30));

        // Build UI
        add(createTopNavigation(), BorderLayout.NORTH);
        add(createMainDashboard(), BorderLayout.CENTER);
    }

    private JPanel createTopNavigation() {
        JPanel navPanel = new JPanel(new BorderLayout(15, 0));
        navPanel.setBackground(BG_MAIN);
        navPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); 

        JLabel lblAppTitle = new JLabel("MeteoDash Pro");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblAppTitle.setForeground(TEXT_PRIMARY);

        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.setOpaque(false);

        txtCity = new JTextField("Paris");
        txtCity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCity.setBackground(BG_CARD);
        txtCity.setForeground(TEXT_PRIMARY);
        txtCity.setCaretColor(TEXT_PRIMARY);
        txtCity.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        txtCity.setPreferredSize(new Dimension(200, 40));
        
        // Auto-suggestions listener
        txtCity.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showSuggestionsLater();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showSuggestionsLater();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showSuggestionsLater();
            }
        });

        // Allow user to hit Enter to trigger search
        txtCity.addActionListener(e -> {
            disableSuggestions = true;
            suggestionPopup.setVisible(false);
            btnSearch.doClick();
            disableSuggestions = false;
        });

        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setBackground(ACCENT_BLUE);
        btnSearch.setForeground(BG_MAIN);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchBox.add(txtCity, BorderLayout.CENTER);
        searchBox.add(btnSearch, BorderLayout.EAST);

        navPanel.add(lblAppTitle, BorderLayout.WEST);
        navPanel.add(searchBox, BorderLayout.EAST);

        return navPanel;
    }

    private JPanel createMainDashboard() {
        JPanel dashboard = new JPanel(new GridBagLayout());
        dashboard.setBackground(BG_MAIN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // --- Left Side: Main Weather Card ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6; 
        gbc.weighty = 1.0;
        gbc.gridheight = 2; 
        
        JPanel mainCard = createMainWeatherCard();
        dashboard.add(mainCard, gbc);

        // --- Right Side: Small Detail Cards ---
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4; 
        gbc.weighty = 0.5;
        gbc.gridheight = 1;
        
        JPanel humidityCard = createSmallDetailCard("Humidity");
        lblHumidityValue = (JLabel) humidityCard.getClientProperty("valueLabel");
        dashboard.add(humidityCard, gbc);

        gbc.gridy = 1;
        JPanel statusCard = createSmallDetailCard("System Status");
        lblStatusValue = (JLabel) statusCard.getClientProperty("valueLabel");
        lblStatusValue.setText("Online");
        lblStatusValue.setFont(new Font("Segoe UI", Font.BOLD, 28)); // slightly smaller for long text
        lblStatusValue.setForeground(new Color(74, 222, 128)); // Green color

        // Create View Map Button
        btnViewMap = new JButton("Voir la carte");
        btnViewMap.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnViewMap.setBackground(ACCENT_BLUE);
        btnViewMap.setForeground(BG_MAIN);
        btnViewMap.setFocusPainted(false);
        btnViewMap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btnViewMap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewMap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnViewMap.setVisible(false); // Hidden by default

        statusCard.add(Box.createRigidArea(new Dimension(0, 15)));
        statusCard.add(btnViewMap);

        dashboard.add(statusCard, gbc);

        return dashboard;
    }

    private JPanel createMainWeatherCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));

        lblMainCity = new JLabel("Enter a city...");
        lblMainCity.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblMainCity.setForeground(TEXT_PRIMARY);
        lblMainCity.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMainTemp = new JLabel("-- °C");
        lblMainTemp.setFont(new Font("Segoe UI", Font.BOLD, 72));
        lblMainTemp.setForeground(ACCENT_BLUE);
        lblMainTemp.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMainDesc = new JLabel("WAITING FOR DATA");
        lblMainDesc.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblMainDesc.setForeground(TEXT_SECONDARY);
        lblMainDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblMainCity);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblMainTemp);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblMainDesc);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel createSmallDetailCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel("--%");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(TEXT_PRIMARY);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);
        
        // Store reference to value label so we can update it later
        card.putClientProperty("valueLabel", lblValue);

        return card;
    }

    // --- Exposed Methods for the Controller ---

    public JButton getSearchButton() {
        return btnSearch;
    }

    public JButton getViewMapButton() {
        return btnViewMap;
    }

    public WeatherData getCurrentWeatherData() {
        return currentWeatherData;
    }

    public String getCityInput() {
        return txtCity.getText();
    }

    public void setLoading(boolean isLoading) {
        suggestionPopup.setVisible(false);
        btnSearch.setEnabled(!isLoading);
        btnSearch.setText(isLoading ? "..." : "Search");
        if (isLoading) {
            lblMainCity.setText("Fetching...");
            lblMainTemp.setText("-- °C");
            lblMainDesc.setText("...");
            lblHumidityValue.setText("--%");
            lblStatusValue.setText("Loading");
            lblStatusValue.setForeground(new Color(250, 204, 21)); // Yellow
            if (btnViewMap != null) {
                btnViewMap.setVisible(false);
            }
        }
    }

    public void updateWeatherDisplay(WeatherData data) {
        this.currentWeatherData = data;
        lblMainCity.setText(data.getCityName());
        lblMainTemp.setText(String.format("%.1f °C", data.getTemperature()));
        lblMainDesc.setText(data.getDescription().toUpperCase());
        lblHumidityValue.setText((int)data.getHumidity() + "%");
        
        lblStatusValue.setText("Success");
        lblStatusValue.setForeground(new Color(74, 222, 128)); // Green
        
        if (btnViewMap != null) {
            btnViewMap.setVisible(true);
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        lblMainCity.setText("Error");
        lblMainTemp.setText("-- °C");
        lblMainDesc.setText("REQUEST FAILED");
        lblHumidityValue.setText("--%");
        
        lblStatusValue.setText("Error");
        lblStatusValue.setForeground(new Color(248, 113, 113)); // Red

        if (btnViewMap != null) {
            btnViewMap.setVisible(false);
        }
    }

    private void showSuggestions() {
        if (disableSuggestions) {
            return;
        }

        String text = txtCity.getText().trim();
        suggestionPopup.setVisible(false);
        suggestionPopup.removeAll();

        if (text.isEmpty()) {
            return;
        }

        List<String> matches = new ArrayList<>();
        for (String city : SUGGESTED_CITIES) {
            if (city.toLowerCase().startsWith(text.toLowerCase())) {
                matches.add(city);
            }
        }

        // If no startsWith matches, try contains matches
        if (matches.isEmpty()) {
            for (String city : SUGGESTED_CITIES) {
                if (city.toLowerCase().contains(text.toLowerCase()) && !city.equalsIgnoreCase(text)) {
                    matches.add(city);
                }
            }
        }

        if (matches.isEmpty()) {
            return;
        }

        int limit = Math.min(matches.size(), 6);
        for (int i = 0; i < limit; i++) {
            String match = matches.get(i);
            JLabel item = new JLabel(match);
            item.setOpaque(true);
            item.setBackground(BG_CARD);
            item.setForeground(TEXT_PRIMARY);
            item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            item.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Custom hover effect with premium colors
            item.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    item.setBackground(new Color(30, 58, 138)); // Modern dark blue/indigo selection background
                    item.setForeground(ACCENT_BLUE); // Bright cyan/blue selection text
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    item.setBackground(BG_CARD);
                    item.setForeground(TEXT_PRIMARY);
                }
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    disableSuggestions = true;
                    txtCity.setText(match);
                    disableSuggestions = false;
                    suggestionPopup.setVisible(false);
                    btnSearch.doClick();
                }
            });
            suggestionPopup.add(item);
        }

        // Style the popup menu container
        suggestionPopup.setBackground(BG_CARD);
        suggestionPopup.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE, 1));
        
        // Show below txtCity
        suggestionPopup.show(txtCity, 0, txtCity.getHeight());
        txtCity.requestFocusInWindow();
    }

    private void showSuggestionsLater() {
        SwingUtilities.invokeLater(this::showSuggestions);
    }
}
