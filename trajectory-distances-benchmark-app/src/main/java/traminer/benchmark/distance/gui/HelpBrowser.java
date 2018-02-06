package traminer.benchmark.distance.gui;

import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * A JavaFX browser component, to show the application's Help web page.
 * Page content is read as a HTML file.
 * 
 * @author douglasapeixoto
 */
public class HelpBrowser extends Region {
	// the JafaFX browser component and processor
	private WebView browser = new WebView();
    private WebEngine webEngine = browser.getEngine();
    // component window dimensions
    private double height;
    private double width;
    
    /**
     * Creates a new browser for displaying help information.
     * 
     * @param content The HTML Web content.
     * @param height  Browser window's height.
     * @param width   Browser window's width.
     */
    public HelpBrowser(String content, double height, double width) {
    	if (height < 0 || width < 0) {
    		throw new IllegalArgumentException(
    				"Browser window dimensions must be positive.");
    	}
    	this.height = height;
    	this.width  = width;
        //apply the styles
        getStyleClass().add("browser");
        // load the web page (from HTML file in local resources)
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
            	//add the web view to the scene
                getChildren().add(browser);
            }
        });
        webEngine.loadContent(content);
    }
 
    @Override 
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override 
    protected double computePrefWidth(double height) {
        return this.height;
    }
 
    @Override 
    protected double computePrefHeight(double width) {
        return this.width;
    }
}
