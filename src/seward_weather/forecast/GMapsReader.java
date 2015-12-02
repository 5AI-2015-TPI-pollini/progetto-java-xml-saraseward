/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seward_weather.forecast;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author Sara
 */
public class GMapsReader {
    Coordinate location;
    private static final String QUERY_STATUS = "/GeocodeResponse/status/text()";
    private static final String QUERY_STATUS_FAIL = "ZERO_RESULT";
    private static final String QUERY_LATITUDE = "/GeocodeResponse/result/geometry/location/lat/text()";
    private static final String QUERY_LONGITUDE = "/GeocodeResponse/result/geometry/location/lng/text()";
    private static final String GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/xml?address=";
    //private Coordinate location=new Coordinate(0,0);

    public GMapsReader (String address, boolean proxyOn){
        Connection c;
        try {
            c = new Connection(toFinalURL(address), proxyOn);
             interpreter(c.getConnection());
             printCoordinate();
        } catch (UnsupportedEncodingException ex) {
             Logger.getLogger(GMapsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }
    
    
    private void printCoordinate(){
        System.out.println(location);
    }
    
    public Coordinate getLocation(){
        return this.location;
    }
    
    
    private void interpreter (InputStream result){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(result);
            
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            
            //Ckecking if found something
            XPathExpression expressionStatus = xpath.compile(QUERY_STATUS);
            String status = (String) expressionStatus.evaluate(doc, XPathConstants.STRING);
            if(status.equals(QUERY_STATUS_FAIL)){
                System.out.println("There's nothing here");
                return;
            }
            
            XPathExpression lat = xpath.compile(QUERY_LATITUDE);
            XPathExpression lon = xpath.compile(QUERY_LONGITUDE);
            NodeList lats = (NodeList) lat.evaluate(doc, XPathConstants.NODESET);
            NodeList lons = (NodeList) lon.evaluate(doc, XPathConstants.NODESET);
            location = new Coordinate(lats.item(0).getNodeValue(), lons.item(0).getNodeValue()); 
            
            /*WeatherReader wR=new WeatherReader(location, proxyOn);
            meteo=wR.getMeteoBellissimo();*/
              
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(GMapsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        
    private String toFinalURL(String address) throws UnsupportedEncodingException{
        String finalUrl=GEOCODE_URL;
        //Replace spaces with +
        finalUrl+=URLEncoder.encode(address, "UTF-8");
        return finalUrl;
    }
    
}
