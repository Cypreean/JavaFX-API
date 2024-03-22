package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Service service;
    private String jsonWeather;
    private Double rate1;
    private Double rate2;
    private String country;
    private String city;
    private String currency;
    private JLabel currencylabel = new JLabel();
    private JLabel currencylabel2 = new JLabel();
    private JTextArea locationLabel = new JTextArea();

    private final JFXPanel jfxPanel = new JFXPanel();
    JPanel panel = new JPanel();

    public MainFrame(Service service, String jsonWeather, Double rate1, Double rate2) {
        this.city = service.getCity();
        this.country = service.getCountry();
        this.currency = service.getCurrency();
        this.jsonWeather = jsonWeather;
        this.rate1 = rate1;
        this.rate2 = rate2;
        this.service = service;
        SwingUtilities.invokeLater(this::initialize);
    }

    private void initialize() {
        this.setLayout(new BorderLayout());
        createScene();
        this.add(panel, BorderLayout.NORTH);
        this.add(jfxPanel, BorderLayout.CENTER);
        this.setSize(1200, 800);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void createScene() {
        Platform.runLater(this::run);
    }
    private void initwiki(WebEngine webEngine) {
        webEngine.load("https://en.wikipedia.org/wiki/"+city);
        System.out.println("https://en.wikipedia.org/wiki/"+city);
    }

    private void run() {
        WebView webView = new WebView();
        jfxPanel.setScene(new Scene(webView));

        panel.setLayout(new GridLayout(1, 4));


        locationLabel = new JTextArea();
        updatelocationinfo();


        JScrollPane scrollPane = new JScrollPane(locationLabel);
        scrollPane.setPreferredSize(new Dimension(1200, 100));


        panel.add(scrollPane);

        currencylabel = new JLabel();
        currencylabel2 = new JLabel();
        panel.add(currencylabel);
        panel.add(currencylabel2);
        updatecurrencyinfo();
        JButton button = new JButton("Change Data");
        button.addActionListener(e -> {
            String newCity = JOptionPane.showInputDialog("Enter new city");
            String newCountry = JOptionPane.showInputDialog("Enter new country");
            String newCurrency = JOptionPane.showInputDialog("Enter new currency");

            this.city = newCity;
            this.country = newCountry;
            this.currency = newCurrency;

            service = new Service(newCountry);
            String newWeatherJson = service.getWeather(newCity);
            Double newRate1 = service.getRateFor(newCurrency);
            Double newRate2 = service.getNBPRate();

            this.jsonWeather = newWeatherJson;
            this.rate1 = newRate1;
            this.rate2 = newRate2;
            this.service = service;

            updatelocationinfo();
            updatecurrencyinfo();

            Platform.runLater(() -> initwiki(webView.getEngine()));
            panel.revalidate();
            panel.repaint();

        });

        panel.add(button);


        panel.revalidate();
        panel.repaint();


        panel.setPreferredSize(new Dimension(1200, 200));


        WebEngine webEngine = webView.getEngine();
        initwiki(webEngine);

    }
    private void updatecurrencyinfo() {
        currencylabel.setText("Currency: " + currency + " Rate: " + rate1);
        currencylabel2.setText("Currency: PLN Rate: " + rate2);

    }
    private void updatelocationinfo(){
        String formattedJson = jsonWeather.replaceAll("[{}\"]", "");
        String loc = formattedJson.substring(formattedJson.indexOf("name")).split(",")[0].split(":")[1].replace("\"", "");
        String sky = formattedJson.substring(formattedJson.indexOf("description")).split(",")[0].split(":")[1].replace("\"", "");
        Double temp = Double.valueOf(formattedJson.substring(formattedJson.indexOf("temp")).split(",")[0].split(":")[1].replace("\"", ""))-273.15;
        Double pressure = Double.valueOf(formattedJson.substring(formattedJson.indexOf("pressure")).split(",")[0].split(":")[1].replace("\"", ""));
        Double humidity = Double.valueOf(formattedJson.substring(formattedJson.indexOf("humidity")).split(",")[0].split(":")[1].replace("\"", ""));
        Double wind = Double.valueOf(formattedJson.substring(formattedJson.indexOf("speed")).split(",")[0].split(":")[1].replace("\"", ""));
        locationLabel.setText("Location: " + loc + "\n" +
                "Sky: " + sky + "\n" +
                "Temperature: " + temp + "°C\n" +
                "Pressure: " + pressure + "hPa\n" +
                "Humidity: " + humidity + "%\n" +
                "Wind: " + wind + "m/s");
    }
}