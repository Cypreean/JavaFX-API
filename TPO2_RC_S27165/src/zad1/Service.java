/**
 *
 *  @author Ratyński Cyprian S27165
 *
 */

package zad1;


import jdk.jshell.execution.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Locale;

public class Service {
    private String country;
    private String city;
    private String currency;

    public Service(String country) {
        this.country = country;
    }
    public String getWeather(String city) {
        this.city = city;
        URL url = null;
        try {
             url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&appid=45718398d43870d799a34de89819811a");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String s = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            String line;
            while((line = in.readLine()) != null)
                s += line;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);
        return s;
    }
    public Double getRateFor(String currency) {
        this.currency = currency;
        Currency currency1 = null;
        for (String iso3: Locale.getISOCountries()) {
            Locale locale = new Locale("", iso3);
            if (locale.getDisplayCountry().equals(country)) {
                currency1 = Currency.getInstance(locale);
            }
        }
        URL url = null;
        try {
            url = new URL("https://v6.exchangerate-api.com/v6/708f7324837d1830e94fc727/latest/"+currency1);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String s = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            String line;
            while((line = in.readLine()) != null)
                s += line;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            s = s.substring(s.indexOf(currency));
            String result[] = s.split(",");
            s= result[0].split(":")[1];
            System.out.println(s);
        }
          catch (Exception e){
                s = "1.0";
            }

        return Double.parseDouble(s);
    }
    public Double getNBPRate() {

        Currency currency1 = null;
        for (String iso3: Locale.getISOCountries()) {
            Locale locale = new Locale("", iso3);
            if (locale.getDisplayCountry().equals(country)) {
                currency1 = Currency.getInstance(locale);
            }
        }

        URL url = null;
        try {
            url = new URL("http://api.nbp.pl/api/exchangerates/tables/A/?format=json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String TabelaA = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            String line;
            while((line = in.readLine()) != null)
                TabelaA += line;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        URL url2 = null;
        try {
            url2 = new URL("http://api.nbp.pl/api/exchangerates/tables/B/?format=json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String TabelaB = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"))) {
            String line;
            while((line = in.readLine()) != null)
                TabelaB += line;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        URL url3 = null;
        try {
            url3 = new URL("http://api.nbp.pl/api/exchangerates/tables/C/?format=json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String TabelaC = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url3.openStream(), "UTF-8"))) {
            String line;
            while((line = in.readLine()) != null)
                TabelaC += line;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean found = false;
        String kurs = "";
        if(TabelaA.contains(currency1.toString())){
            found = true;
            kurs = TabelaA.substring(TabelaA.indexOf(currency1.toString()));
            kurs = kurs.split("}")[0].split(":")[1];
            System.out.println(kurs);

        }
        if(TabelaB.contains(currency1.toString())&&!found){
            kurs = TabelaB.substring(TabelaB.indexOf(currency1.toString()));
            kurs = kurs.split("}")[0].split(":")[1];
            System.out.println(kurs);
            found = true;
        }
        if(TabelaC.contains(currency1.toString()) && !found){
            kurs = TabelaC.substring(TabelaC.indexOf(currency1.toString()));
            kurs = kurs.split("}")[0].split(":")[1];
            System.out.println(kurs);
        }
        if (!found){
            kurs = "1.0";
        }
        return Double.parseDouble(kurs);
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public String getCurrency() {
        return currency;
    }
}  
