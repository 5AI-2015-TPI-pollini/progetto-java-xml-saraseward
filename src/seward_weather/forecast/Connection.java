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
    
    
    public Connection(String url, boolean proxyOn){
        //setProxy();
        this.url = url;
        this.proxyOn=proxyOn;
        //readSettings();
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
                System.out.println("I will try to set up proxy.");
                //proxyOn=true;
                setProxy();
                connect();
            }
            return;
        } catch (MalformedURLException ex) {
            System.out.println("There was a problem creating the connection. Sorry.");
            output = null;
            exit(1);
            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
                System.out.println("I can't connect to internet. Check your connection and try again.");
                ex.printStackTrace();
                exit(1);
            }
        
    }
    
    public InputStream getConnection(){
        return output;
    }
    
    private void setProxy(){
        readSettings();
        System.setProperty("useProxy", "true");
        System.err.println("impostato");
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
            System.out.println("Proxy configuration file is missing!"+"\n"+"Can't connect to Internet.");
            exit(1);
        } catch (IOException ex) {
            System.out.println("I can't connect to internet. Check your connection and try again.");
        }
    }
}
