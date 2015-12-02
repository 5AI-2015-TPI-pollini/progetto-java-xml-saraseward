/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seward_weather.forecast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 *
 * @author Sara
 */
public class WeatherReader {
    private final Coordinate location;
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?mode=xml";
    private static String APIKEY;
    
    private static final String QUERY_TEMPERATURE_VALUE = "/current/temperature/@value";
    private static final String QUERY_HUMIDITY="/current/humidity/@value";
    private static final String QUERY_CLOUDS="/current/clouds/@name";
    private String meteoBellissimo;

    public WeatherReader (Coordinate location, boolean proxyOn){
        this.location = location;
        readSettings();
        Connection c=new Connection(toFinalURL(), proxyOn);
         System.out.println("weatherreader connessione sexy");
        interpreter(c.getConnection());
        System.out.println("ho finito la costruuzione");
    }
    
    public String getMeteoBellissimo() {
        return meteoBellissimo;
    }

    public void setMeteoBellissimo(String meteoBellissimo) {
        this.meteoBellissimo = meteoBellissimo;
    }
    
    private void interpreter (InputStream result){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(result);
            
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            
            //Temperature: value, max, min
            XPathExpression currentTemperatureValue = xpath.compile(QUERY_TEMPERATURE_VALUE);
            String tempValue = (String) currentTemperatureValue.evaluate(doc, XPathConstants.STRING);
            
            //Humidity [%]
            XPathExpression currentHumidity = xpath.compile(QUERY_HUMIDITY);
            String tempHumidity = (String) currentHumidity.evaluate(doc, XPathConstants.STRING);
            
            //Clouds (sky is clear,...)      
            XPathExpression currentClouds = xpath.compile(QUERY_CLOUDS);
            String tempClouds = (String) currentClouds.evaluate(doc, XPathConstants.STRING);
            
            MeteoBello currentMeteo=new MeteoBello(tempValue, tempHumidity, tempClouds);
            
            
            this.setMeteoBellissimo(currentMeteo.toString());
            
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(GMapsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private InputStream getConnection (){
        try{
            URL urlWeather = new URL(toFinalURL());
            System.out.println(urlWeather);
            URLConnection urlConnectionWeather = urlWeather.openConnection();
            InputStream result=new BufferedInputStream(urlConnectionWeather.getInputStream());
            return result;
        } catch (MalformedURLException ex) {
            Logger.getLogger(WeatherReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WeatherReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    return null;
    }
    
    private String toFinalURL(){
        return WEATHER_URL + 
                "&lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&appid=" + APIKEY;
    }
    
    private void readSettings(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("apikey.txt"));
            APIKEY = br.readLine();
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Weather configuration file is missing!"+"\n"+"Can't check weather conditions.");
            exit(2);
        } catch (IOException ex) {
           System.out.println("There was a problem retrieving data from Open Weather. Sorry!");
           System.out.println("Here some data for nerds:");
           
           exit(2);
        }
    }
    
}
