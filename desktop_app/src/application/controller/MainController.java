package application.controller;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;

import application.model.SensorReading;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import reactor.core.publisher.Flux;

public class MainController implements Initializable  {
	
    @FXML
    private Button readButton;

    @FXML
    private Button pauseButton;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<Number, Number> lineChart;

    
    final int WINDOW_SIZE = 10;
    
  
    
    Deque<SensorReading> data = new ArrayDeque<SensorReading>();
    
    XYChart.Series<Number,Number> series = new XYChart.Series<Number, Number>();

    Service<Void> updateGraph = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new Task<Void>() {

				@Override
				protected Void call() throws Exception {
			    	return null;
				}	
			};
		}
	};
	
	
	Service<Void> getServerData = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					try {
			    		ParameterizedTypeReference<ServerSentEvent<String>> typeRef = new ParameterizedTypeReference<ServerSentEvent<String>>() {};
						final Flux<ServerSentEvent<String>> stream= WebClient.create("http://172.28.134.181:8080").get().uri("/")
					            .accept().retrieve().bodyToFlux(typeRef);
			    	    stream.subscribe(sse -> {
				        	SensorReading reading =  new SensorReading(Double.parseDouble((sse.data())));
				        	data.push(reading);
				        	System.out.println(sse.data());
						

				        });
						TimeUnit.MINUTES.sleep(10);

						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    	return null;
				}	
			};
		}
	};
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        series.setName("DHT sensor reading");
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(9);
		
		
	}
    	
	
	

    
    
  

    @FXML
    void readData() {
    	getServerData.start();  
    	getServerData.setOnSucceeded(e ->{
    		getServerData.restart();
    	});
    	lineChart.getData().add(series);
    	updateGraph.start();
        lineChart.getXAxis().setAnimated(false);
        xAxis.setAutoRanging(false);
    	updateGraph.setOnSucceeded(e -> {
			if (!data.isEmpty() && SensorReading.isPlaying() == true)
			{
				SensorReading sensorReading = new SensorReading (data.pop());
				if (series.getData().size() > WINDOW_SIZE) {
					xAxis.setLowerBound(sensorReading.getReadingNumber()-9);
					xAxis.setUpperBound(sensorReading.getReadingNumber()+1);
				}
			    series.getData().add(new XYChart.Data<Number, Number> (sensorReading.getReadingNumber(),sensorReading.getValue()));
			    

			}
			updateGraph.restart();
	
    	});
    	
    	}
    
    @FXML
    void play(ActionEvent event) {
    	data.clear();
     	SensorReading.setPlaying(true);

    }
    
    @FXML
    void pauseShow(ActionEvent event) {
    	SensorReading.setPlaying(false);
    	
    }

	    	
    
 	
}


