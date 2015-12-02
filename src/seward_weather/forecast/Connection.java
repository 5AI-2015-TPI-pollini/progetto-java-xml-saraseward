package  seward_weather.forecast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Sara
 */
public class Connection {
    private String url;
    private InputStream output;
    private String proxyAddress, proxyPort, authUser, authPassword;
    private final Boolean proxyOn;
    
    /**
     *
     * @param url Contains the correct http address to reach the Google API.
     * @param proxyOn Is a flag, if it's true we need to use a proxy to connect to the internet. 
     */
    public Connection(String url, boolean proxyOn){
        
        this.url = url;
        this.proxyOn=proxyOn;
        
        connect();
    }
    
    private void connect(){
        try {
            URL URLInput = new URL(url);
            System.out.println(URLInput);
            URLConnection ConnectionInput = URLInput.openConnection();
            ConnectionInput.connect();
            output = new BufferedInputStream(ConnectionInput.getInputStream());
            
            if (proxyOn==true){
                System.out.println("Setting up the proxy...");
                //proxyOn=true;
                setProxy();
                connect();
            }
            return;
        } catch (MalformedURLException ex) {
            System.out.println("Invalid URL.");
            output = null;
            exit(1);
            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
                System.out.println("Connection impossible. Check it.");
                exit(1);
            }
        
    }
    
    /**
     *
     * @return The variable "output" of type InputStream. It contains the InputStream from the ConnectionInput.
     */
    public InputStream getConnection(){
        return output;
    }
    
    private void setProxy(){
        readSettings();
        System.setProperty("useProxy", "true");
        System.setProperty("http.proxyHost", proxyAddress);
        System.setProperty("http.proxyPort", proxyPort);
                
        
        Authenticator.setDefault(
           new Authenticator() {
              @Override
              public PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(
                       authUser, authPassword.toCharArray());
              }
           }
        );
        
    }
    
    private void readSettings(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("proxyconfig.txt"));
            proxyAddress = br.readLine();
            System.out.println("proxy: " + proxyAddress);
            proxyPort = br.readLine();
            authUser = br.readLine();
            authPassword = br.readLine();
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Proxy configuration file is missing!");
            exit(1);
        } catch (IOException ex) {
            System.out.println("Connection impossible. Check it.");
        }
    }
}
