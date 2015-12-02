/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seward_weather.forecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sara
 */
public class Seward_WeatherForecast {

    
    public static String meteoBellissimo;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        try {
            System.out.println("Benvenuto! Inserisci l'indirizzo (Via/Nome/Numero/Città o provincia):");
            //Leggo input
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String address = br.readLine();
            
            boolean proxyOn = false;
            boolean temp=false;
            System.out.println("C'è un proxy? (S/N)");
            //Leggo input           
            String answer = br.readLine();
            while (temp==false){
            if (answer.equalsIgnoreCase("S")){
                proxyOn=true;
                temp=true;
            }
            else if (answer.equalsIgnoreCase("N")){
                proxyOn=false;
                temp=true;
            }
            else {
                System.out.println("E' possibile rispondere solo con S o solo con N.");
            }
            }
            
             /*new Thread(new Runnable(){
                @Override
                public void run(){*/
                    GMapsReader addressReader = new GMapsReader(address, proxyOn);
                    WeatherReader wR=new WeatherReader(addressReader.getLocation(), proxyOn);
                    meteoBellissimo=wR.getMeteoBellissimo();
                    System.out.println(meteoBellissimo);
               /* }
            }).start();*/
        } catch (IOException ex) {
            Logger.getLogger(Seward_WeatherForecast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
