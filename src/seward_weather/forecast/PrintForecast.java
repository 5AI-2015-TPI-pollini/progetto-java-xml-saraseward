/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seward_weather.forecast;

/**
 *
 * @author Sara
 */
public class PrintForecast {
    private Double temperatureValue;
    private String humidity;
    private String clouds;

    public PrintForecast(String temperatureValue, String humidity, String clouds) {
        this.temperatureValue = toCelsius(Double.parseDouble(temperatureValue));
        this.humidity = humidity;
        this.clouds = clouds;
    }
    
    private Double toCelsius(Double temperature)
    {
        double K=273.15; //0°C = 273,15 K
        return (temperature-K); //Per convertire da Kelvin a Celsius, basta sottrarre K. 
                                //es. Quanti gradi Celsius sono 373,15 Kelvin? Sottraggo K, ottengo 100°C
    }
    
    @Override
    public String toString(){
        if(temperatureValue != null){
            return "Your weather forecast: \n"
                + "Temperature: " + temperatureValue + "°C \n"
                + "Humidity: " + humidity + "% \n"
                + "Clouds: " + clouds + "\n";
            
        }
        else{
            return "Values are null.";
        }
        
    }
}

