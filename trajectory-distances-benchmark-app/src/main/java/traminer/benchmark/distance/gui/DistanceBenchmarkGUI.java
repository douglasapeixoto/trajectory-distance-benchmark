package traminer.benchmark.distance.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import traminer.io.log.LogWriter;

/**
 * Starts the GUI and binds the GUI Window with 
 * the FXML scene (JavaFX). GUI start point class.
 * 
 * @author douglasapeixoto
 */
public class DistanceBenchmarkGUI extends Application {

	@Override
	public void start(Stage stage) {		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("DistanceBenchmarkScene.fxml"));
			
			Scene mainScence  = new Scene(root);

	        stage.setTitle("Trajectory Distances Benchmark App");
	        stage.setScene(mainScence);
	        stage.setHeight(580.0);
	        stage.setWidth(940.0);
	        stage.setResizable(false);
	        stage.show();
		} catch (IOException e) {
			System.err.println("Error starting GUI.");
			e.printStackTrace();
		}
	}

	/**
	 * Launch GUI.
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the log4j configuration
		LogWriter.startLog();
		// launch app GUI
		launch(args);
	}
}
