/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherforecast;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seward_weather.forecast.*;


import static seward_weather.forecast.Seward_WeatherForecast.*;
import seward_weather.forecast.WeatherReader;
/**
 *
 * @author Sara
 */
public final class WeatherForecast extends Application {

    public WeatherForecast() throws Exception {
        this.start(new Stage());
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather Forecast");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text title = new Text("Weather Forecast");
        title.setId("title");
        //title.setFont(Font.font("Calibri Light", FontWeight.EXTRA_LIGHT, 20));
        grid.add(title, 0, 0, 2, 1);

        Label address = new Label("Address:");
        grid.add(address, 0, 1);

        TextField addressTextField = new TextField();
        grid.add(addressTextField, 1, 1);
        
        
        Label proxyLbl = new Label("Proxy? (Y/N)");
        grid.add(proxyLbl, 0, 2);

        TextField proxyTextField = new TextField();
        grid.add(proxyTextField, 1, 2);
        
        Button btnCalcola = new Button("Calcola");
        HBox hbCalcola = new HBox(10);
        hbCalcola.setAlignment(Pos.BOTTOM_LEFT);
        hbCalcola.getChildren().add(btnCalcola);
        grid.add(hbCalcola, 1, 3);

        /*Label result = new Label(" ");
        grid.add(result, 0, 2);*/
        
        final Text meteo = new Text();
        meteo.setId("result");
        grid.add(meteo, 1, 4);
        
        btnCalcola.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {   
            String address=addressTextField.getText();
            String proxy = proxyTextField.getText();
            boolean temp=false;
            boolean tempProxyOn=true;
            while (temp==false){
            if (proxy.equalsIgnoreCase("S")){
                tempProxyOn=true;
                temp=true;
            }
            else if (proxy.equalsIgnoreCase("N")){
                tempProxyOn=false;
                temp=true;
            }
            else {
                System.out.println("You can only answer with Y (YES) or N (NO).");
            }
            }
            
            final boolean proxyOn=tempProxyOn;
            
            new Thread(new Runnable(){
                @Override
                public void run(){
                    GMapsReader addressReader = new GMapsReader(address, proxyOn);
                    WeatherReader wR=new WeatherReader(addressReader.getLocation(), proxyOn);
                    meteoBellissimo=wR.getMeteoBellissimo();
                   
                    //meteoBellissimo="CCiao";
                    Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                    //Imposti il valore della grafica
                    meteo.setText(meteoBellissimo);
                            }
                    });
                }
            }).start();
            }
        });

        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(WeatherForecast.class.getResource("stylesheet.css").toExternalForm());
        primaryStage.show();
            
        }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
