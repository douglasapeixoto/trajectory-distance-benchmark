package traminer.benchmark.distance.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import traminer.benchmark.distance.DistanceFunction;
import traminer.benchmark.distance.DistancePair;
import traminer.benchmark.distance.TrajectoryTransformationService;
import traminer.io.IOService;
import traminer.io.reader.TrajectoryReader;
import traminer.util.spatial.SpatialUtils;
import traminer.util.spatial.distance.EuclideanDistanceFunction;
import traminer.util.spatial.distance.HaversineDistanceFunction;
import traminer.util.spatial.distance.PointDistanceFunction;
import traminer.util.trajectory.Trajectory;
import traminer.util.trajectory.distance.DISSIMDistanceCalculator;
import traminer.util.trajectory.distance.DTWDistanceCalculator;
import traminer.util.trajectory.distance.EDCDistanceCalculator;
import traminer.util.trajectory.distance.EDRDistanceCalculator;
import traminer.util.trajectory.distance.EDwPDistanceCalculator;
import traminer.util.trajectory.distance.ERPDistanceCalculator;
import traminer.util.trajectory.distance.FrechetDistanceCalculator;
import traminer.util.trajectory.distance.LCSSDistanceCalculator;
import traminer.util.trajectory.distance.LIPDistanceCalculator;
import traminer.util.trajectory.distance.OWDDistanceCalculator;
import traminer.util.trajectory.distance.PDTWDistanceCalculator;
import traminer.util.trajectory.distance.STEDDistanceCalculator;
import traminer.util.trajectory.distance.STLCSSDistanceCalculator;
import traminer.util.trajectory.distance.STLIPDistanceCalculator;
import traminer.util.trajectory.distance.TIDDistanceCalculator;
import traminer.util.trajectory.distance.TrajectoryDistanceFunction;

/**
 * GUI controller class (JavaFX). Handle the events and components 
 * of the GUI {@link TrajectoryDistancesScene.fxml}. Binds the GUI  
 * components with the Java code.
 * 
 * @author douglasapeixoto
 */
public class DistanceBenchmarkGUIController implements Initializable {
	// root component
	@FXML
	private TabPane rootPane;
	
	// Open directories controls 
	@FXML
	private TextField inputDataTxt1;
	@FXML
	private TextField inputDataTxt2;
	@FXML
	private TextField outputDataTxt;

	@FXML
	private CheckBox addNoiseCheck2;
	@FXML
	private CheckBox shiftPointsCheck2;
	@FXML
	private CheckBox addPointsCheck2;
	@FXML
	private CheckBox removePointsCheck2;
	@FXML
	private CheckBox samplingRateCheck2;
	@FXML
	private CheckBox scaleCheck2;
	@FXML
	private CheckBox timeShiftCheck2;
	@FXML
	private CheckBox rotationCheck2;
	@FXML
	private CheckBox translationCheck2;
	
	@FXML
	private TextField addNoiseRateTxt2;
	@FXML
	private TextField addNoiseDistanceTxt2;
	@FXML
	private TextField shiftPointsRateTxt2;
	@FXML
	private TextField shiftPointsDistanceTxt2;
	@FXML
	private TextField addPointsRateTxt2;
	@FXML
	private TextField removePointsRateTxt2;
	@FXML
	private TextField samplingRateTxt2;
	@FXML
	private TextField scaleRateTxt2;
	@FXML
	private TextField timeShiftStartTxt2;
	@FXML
	private TextField rotationAngleTxt2;
	@FXML
	private TextField translationXTxt2;
	@FXML
	private TextField translationYTxt2;
	
	// Parameters info label
	@FXML
	private Label paramsInfoLabel;
	
	// Distance measure select control
	@FXML
	private ChoiceBox<DistanceFunction> distanceMeasureChoiceBox;
	
	// Sort attribute select control
	@FXML
	private ChoiceBox<String> sortResultsChoiceBox;
	
	// Normalization select control
	@FXML
	private ChoiceBox<String> normalizationChoiceBox;
	// Normalization min/max parameters
	@FXML
	private TextField normalizationMinTxt;
	@FXML
	private TextField normalizationMaxTxt;
	
	/** Parameters window components **/
	private Alert paramsWindow = new Alert(AlertType.INFORMATION);
	private AnchorPane rootParams;
	private ChoiceBox<String> ptsDistanceBox;
	private Label paramALabel;
	private Label paramBLabel;
	private TextField paramATxt;
	private TextField paramBTxt;
	
	// Distance function to use - set default
	private DistanceFunction distanceFuncName = 
			DistanceFunction.EUCLIDEAN;
	private TrajectoryDistanceFunction trajectoryDistanceFunc = 
			new EDCDistanceCalculator();
	
	// Datasets to process
	private List<Trajectory> datasetA = null;
	private List<Trajectory> datasetB = null;
	
	// Running time of the distances measurement
	private long runningTime = 0;
	
	// Point distance functions
	private static final String EUCLIDEAN  = "EUCLIDEAN";  // (x, y)
	private static final String GEOGRAPHIC = "GEOGRAPHIC"; // (lat, lon)
	
	// Sort options
	private static final String DATASET_A  = "DATASET-A";
	private static final String DATASET_B  = "DATASET-B";
	private static final String DISTANCE   = "DISTANCE";
	
	// Normalization options
	private static final String NONE  	 = "NONE";
	private static final String MIN_MAX  = "MIN-MAX";
	private static final String MEAN_STD = "MEAN-STD";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initialize the logic here: 
		// all @FXML variables will have been injected
		createParametersPane();
		startChoiceBoxes();
		handleDistanceParametersSet();
		handleDistanceFunctionChoice();
		handleNormalizationChoice();
	}
	
	/**
	 * Add the list of values of the check boxes
	 */
	public void startChoiceBoxes() {
		// feed Sort Results choice box
		sortResultsChoiceBox.getItems().add(DATASET_A);
		sortResultsChoiceBox.getItems().add(DATASET_B);
		sortResultsChoiceBox.getItems().add(DISTANCE);
		sortResultsChoiceBox.setValue(DATASET_A);
		// feed Normalization choice box
		normalizationChoiceBox.getItems().add(NONE);
		normalizationChoiceBox.getItems().add(MIN_MAX);
		normalizationChoiceBox.getItems().add(MEAN_STD);
		normalizationChoiceBox.setValue(NONE);
		// feed Distance Function choice box
		ObservableList<DistanceFunction> distanceFunctionList = 
				FXCollections.observableArrayList(DistanceFunction.values());
		distanceMeasureChoiceBox.setItems(distanceFunctionList);
		distanceMeasureChoiceBox.setValue(distanceFuncName);
	}
		
	/**
	 * Handle the action of setting up the distance function 
	 * parameters on the dialog window.
	 */
	private void handleDistanceParametersSet() {
		paramsWindow.setOnCloseRequest(new EventHandler<DialogEvent>() {
			@Override
			public void handle(DialogEvent event) {
				// validate fields and create the distance measures
				// get points distance measure
				PointDistanceFunction pointDist = new EuclideanDistanceFunction();
				if (ptsDistanceBox.getValue().equals(EUCLIDEAN)) {
					pointDist = new EuclideanDistanceFunction();
				} else
				if (ptsDistanceBox.getValue().equals(GEOGRAPHIC)) {
					pointDist = new HaversineDistanceFunction();
				}

				try { 
					switch (distanceFuncName) {
						case DISSIM:
							int increment = Integer.parseInt(paramATxt.getText());
							trajectoryDistanceFunc = new DISSIMDistanceCalculator(increment);
						break;
						
						case DTW:
							trajectoryDistanceFunc = new DTWDistanceCalculator(pointDist);
						break;
						
						case EDR:
							double edrThreshold = Double.parseDouble(paramATxt.getText());
							trajectoryDistanceFunc = new EDRDistanceCalculator(edrThreshold, pointDist);
						break;
						
						case EDwP:
							trajectoryDistanceFunc = new EDwPDistanceCalculator(pointDist);
						break;
						
						case ERP:
							double erpThreshold = Double.parseDouble(paramATxt.getText());
							trajectoryDistanceFunc = new ERPDistanceCalculator(erpThreshold);
						break;
						
						case EUCLIDEAN:
							trajectoryDistanceFunc = new EDCDistanceCalculator();
						break;
						
						case FRECHET:
							trajectoryDistanceFunc = new FrechetDistanceCalculator(pointDist);
						break;
						
						case LCSS:
							double lcssDistThreshold = Double.parseDouble(paramATxt.getText());
							int lcssTimeThreshold = Integer.parseInt(paramBTxt.getText());
							trajectoryDistanceFunc = new LCSSDistanceCalculator(
										lcssDistThreshold, lcssTimeThreshold);
						break;
						
						case LIP:
							trajectoryDistanceFunc = new LIPDistanceCalculator(pointDist);
						break;
						
						case OWD:
							trajectoryDistanceFunc = new OWDDistanceCalculator(pointDist);
						break;
						
						case PDTW:
							double reductionRate = Double.parseDouble(paramATxt.getText());
							trajectoryDistanceFunc = new PDTWDistanceCalculator(reductionRate, pointDist);
						break;
						
						case STED:
							trajectoryDistanceFunc = new STEDDistanceCalculator(pointDist);
						break;
						
						case STLCSS:
							double stlcssDistThreshold = Double.parseDouble(paramATxt.getText());
							int stlcssTimeThreshold = Integer.parseInt(paramBTxt.getText());
							trajectoryDistanceFunc = new STLCSSDistanceCalculator(stlcssDistThreshold, stlcssTimeThreshold);
						break;
						
						case STLIP:
							double timePenalty = Double.parseDouble(paramATxt.getText());
							trajectoryDistanceFunc = new STLIPDistanceCalculator(timePenalty, pointDist);
						break;
						
						case TID:
							trajectoryDistanceFunc = new TIDDistanceCalculator(pointDist);
						break;
					}
				} catch (IllegalArgumentException e) {
					event.consume(); // keep parameters window open
					showErrorMessage(e.getMessage());
				} 
				
				// red message on main window
				String paramsInfo = "Parameters: NONE";
				if (paramATxt.isVisible()) {
					paramsInfo = paramALabel.getText() + " " + paramATxt.getText();
				}
				if (paramBTxt.isVisible()) {
					paramsInfo += ", " + 
								 paramBLabel.getText() + " " + paramBTxt.getText();
				}
				paramsInfoLabel.setText(paramsInfo);
				System.out.println(paramsInfo);
			}
		});
	}	

	/**
	 * Handle the action of selecting the normalization method
	 */
	private void handleNormalizationChoice() {
		normalizationChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final String normalization = normalizationChoiceBox.getValue();
				if (normalization.equals(MIN_MAX)) {
					normalizationMinTxt.setDisable(false);
					normalizationMaxTxt.setDisable(false);
				} else {
					normalizationMinTxt.setDisable(true);
					normalizationMaxTxt.setDisable(true);
				}
			}
		});
	}
	
	/**
	 * Handle the action of selecting the distance function
	 */
	private void handleDistanceFunctionChoice() {
		paramsInfoLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				showDistanceParamsWindow();
			}
		});
		distanceMeasureChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showDistanceParamsWindow();
			}
		});
	}
	
	/**
	 * Show the popup window to set up the currently selected
	 *  distance function parameters
	 */
	private void showDistanceParamsWindow() {
		distanceFuncName = distanceMeasureChoiceBox.getValue();
		
		// configure the content of the parameters dialog window
		paramsWindow.setHeaderText("Set " + distanceFuncName + " parameters");
		paramsWindow.getDialogPane().setContent(rootParams);
		
		// clean parameters window
		ptsDistanceBox.setValue("EUCLIDEAN");
		ptsDistanceBox.setDisable(true);
		paramALabel.setText("");
		paramBLabel.setText("");
		paramATxt.setVisible(false);
		paramBTxt.setVisible(false);				
		
		// show parameters of each specific distance function
		switch (distanceFuncName) {
			case DISSIM:
				paramALabel.setText("Time Increment:");
				paramATxt.setText("1");
				paramATxt.setVisible(true);
			break;
			
			case DTW:
				ptsDistanceBox.setDisable(false);
			break;
			
			case EDR:
				ptsDistanceBox.setDisable(false);
				paramALabel.setText("Distance Threshold:");
				paramATxt.setText("0.0");
				paramATxt.setVisible(true);
			break;
			
			case EDwP:
				ptsDistanceBox.setDisable(false);
			break;
			
			case ERP:
				paramALabel.setText("Distance Threshold:");
				paramATxt.setText("0.0");
				paramATxt.setVisible(true);
			break;
			
			case EUCLIDEAN:
				ptsDistanceBox.setDisable(false);
			break;
			
			case FRECHET:
				ptsDistanceBox.setDisable(false);
			break;
			
			case LCSS:
				paramALabel.setText("Distance Threshold:");
				paramATxt.setText("0.001");
				paramATxt.setVisible(true);
				paramBLabel.setText("Time Threshold:");
				paramBTxt.setText("1");
				paramBTxt.setVisible(true);
			break;
			
			case LIP:
				ptsDistanceBox.setDisable(false);
			break;
			
			case OWD:
				ptsDistanceBox.setDisable(false);
			break;
			
			case PDTW:
				ptsDistanceBox.setDisable(false);
				paramALabel.setText("Reduction Rate:");
				paramATxt.setText("3");
				paramATxt.setVisible(true);
			break;
			
			case STED:
				ptsDistanceBox.setDisable(false);
			break;
			
			case STLCSS:
				paramALabel.setText("Distance Threshold:");
				paramATxt.setText("0.001");
				paramATxt.setVisible(true);
				paramBLabel.setText("Time Threshold:");
				paramBTxt.setText("1");
				paramBTxt.setVisible(true);
			break;
			
			case STLIP:
				ptsDistanceBox.setDisable(false);
				paramALabel.setText("Time Penalty:");
				paramATxt.setText("0.5");
				paramATxt.setVisible(true);
			break;
			
			case TID:
				ptsDistanceBox.setDisable(false);
			break;
		}
		// show parameters configuration popup
		paramsWindow.show();
	}
	
	@FXML 
	private void actionStart() {
		// check mandatory fields
		if (!validate()) return;
		
		// read datasets
		String dataPath1 = inputDataTxt1.getText();
		String dataPath2 = inputDataTxt2.getText();
		try {
			datasetA = load(dataPath1);
		} catch (IOException e) {
			showErrorMessage("Unable to open Dataset A location.");
			e.printStackTrace();
		}
		try {
			datasetB = load(dataPath2);
		} catch (IOException e) {
			showErrorMessage("Unable to open Dataset B location.");
			e.printStackTrace();
		}
		
		// run required transformations on each dataset
		doTransformations();
		
		// run distances
		List<DistancePair> distancePairs = getDistances();

		// Normalize distances
		distancePairs = doNormalization(distancePairs);
		
		// sort results
		doSort(distancePairs);

		// save results
		saveResults(distancePairs);
		
		showInfoMessage("Finished!!!\nDistances calculation in: " + runningTime + "ms.");
	}

	@FXML 
	private void actionOpenDataset1() {
    	final DirectoryChooser dirChooser = new DirectoryChooser();
    	dirChooser.setTitle("Open Input Dataset A");
        final File selectedDir = dirChooser.showDialog(
        		rootPane.getScene().getWindow());
        if (selectedDir != null) {
        	String dataPath = selectedDir.getAbsolutePath();
        	inputDataTxt1.setText(dataPath);
        	inputDataTxt1.home();
        }		
	}
	
	@FXML 
	private void actionOpenDataset2() {
    	final DirectoryChooser dirChooser = new DirectoryChooser();
    	dirChooser.setTitle("Open Input Dataset B");
        final File selectedDir = dirChooser.showDialog(
        		rootPane.getScene().getWindow());
        if (selectedDir != null) {
        	String dataPath = selectedDir.getAbsolutePath();
        	inputDataTxt2.setText(dataPath);
        	inputDataTxt2.home();
        }
	}
	
	@FXML 
	private void actionOpenOutputData() {
    	final DirectoryChooser dirChooser = new DirectoryChooser();
    	dirChooser.setTitle("Open Output Directory");
        final File selectedDir = dirChooser.showDialog(
        		rootPane.getScene().getWindow());
        if (selectedDir != null) {
        	String dataPath = selectedDir.getAbsolutePath();
        	outputDataTxt.setText(dataPath);
        	outputDataTxt.home();
        }		
	}
	
	@FXML 
	private void actionAddNoiseCheck2() {
	    if (addNoiseCheck2.isSelected()){
	    	addNoiseRateTxt2.setDisable(false);
	    	addNoiseDistanceTxt2.setDisable(false);
	    } else {
	    	addNoiseRateTxt2.setDisable(true);
	    	addNoiseDistanceTxt2.setDisable(true);
	    }
	}
	
	@FXML 
	private void actionShiftPointsCheck2() {
	    if (shiftPointsCheck2.isSelected()){
	    	shiftPointsRateTxt2.setDisable(false);
	    	shiftPointsDistanceTxt2.setDisable(false);
	    } else {
	    	shiftPointsRateTxt2.setDisable(true);
	    	shiftPointsDistanceTxt2.setDisable(true);
	    }
	}
	
	@FXML 
	private void actionAddPointsCheck2() {
	    if (addPointsCheck2.isSelected()){
	    	addPointsRateTxt2.setDisable(false);
	    } else {
	    	addPointsRateTxt2.setDisable(true);
	    }
	}

	@FXML 
	private void actionRemovePointsCheck2() {
	    if (removePointsCheck2.isSelected()){
	    	removePointsRateTxt2.setDisable(false);
	    } else {
	    	removePointsRateTxt2.setDisable(true);
	    }
	}
	
	@FXML 
	private void actionSamplingRateCheck2() {
	    if (samplingRateCheck2.isSelected()){
	    	samplingRateTxt2.setDisable(false);
	    } else {
	    	samplingRateTxt2.setDisable(true);
	    }
	}
	
	@FXML 
	private void actionScaleCheck2() {
	    if (scaleCheck2.isSelected()){
	    	scaleRateTxt2.setDisable(false);
	    } else {
	    	scaleRateTxt2.setDisable(true);
	    }
	}
	
	@FXML 
	private void actionTimeShiftCheck2() {
	    if (timeShiftCheck2.isSelected()){
	    	timeShiftStartTxt2.setDisable(false);
	    } else {
	    	timeShiftStartTxt2.setDisable(true);
	    }
	}

	@FXML 
	private void actionRotationCheck2() {
	    if (rotationCheck2.isSelected()){
	    	rotationAngleTxt2.setDisable(false);
	    } else {
	    	rotationAngleTxt2.setDisable(true);
	    }
	}
	
	@FXML 
	private void actionTranslationCheck2() {
	    if (translationCheck2.isSelected()){
	    	translationXTxt2.setDisable(false);
	    	translationYTxt2.setDisable(false);
	    } else {
	    	translationXTxt2.setDisable(true);
	    	translationYTxt2.setDisable(true);
	    }
	}
	
	/**
	 * Creates the distance function parameters setup window.
	 */
	private void createParametersPane() {	
		Label ptsDistLabel = new Label("Points Distance Measure:");
		ptsDistLabel.setLayoutX(20.0);
		ptsDistLabel.setLayoutY(10.0);
		
		ptsDistanceBox = new ChoiceBox<>();
		ptsDistanceBox.getItems().add("EUCLIDEAN");
		ptsDistanceBox.getItems().add("GEOGRAPHIC");
		ptsDistanceBox.setLayoutX(20.0);
		ptsDistanceBox.setLayoutY(40.0);
		ptsDistanceBox.setPrefWidth(240.0);
		
		paramALabel = new Label("Parameter_a:");
		paramALabel.setLayoutX(20.0);
		paramALabel.setLayoutY(80.0);
		
		paramBLabel = new Label("Parameter_b:");
		paramBLabel.setLayoutX(20.0);
		paramBLabel.setLayoutY(120.0);
		
		paramATxt = new TextField();
		paramATxt.setLayoutX(158.0);
		paramATxt.setLayoutY(80.0);
		paramATxt.setPrefWidth(100.0);
		
		paramBTxt = new TextField();
		paramBTxt.setLayoutX(158.0);
		paramBTxt.setLayoutY(120.0);
		paramBTxt.setPrefWidth(100.0);

		rootParams = new AnchorPane();
		rootParams.getChildren().addAll(ptsDistLabel, ptsDistanceBox, 
				paramALabel, paramBLabel, paramATxt, paramBTxt);
	}
	
	// This should be in the Model
	/**
	 * Load the trajectory dataset in the given path as
	 * a Stream of trajectory objects.
	 * 
	 * @param dataPath Path to trajectory dataset.
	 * @return The dataset as a trajectory list.
	 * @throws IOException If file not found.
	 */
	private List<Trajectory> load(String dataPath) throws IOException {
		final String dataFormat = IOService.readResourcesFileContent(
				"trajectory-data-format.tddf");
		return TrajectoryReader.readAsStream(dataPath, dataFormat, false, 1)
				.collect(Collectors.toList());
	}
	
	// This should be in the Model
	/**
	 * Calculates the distances between every trajectory in the
	 * Dataset-A to every trajectory in the Dataset-B.
	 * 
	 * @return A list of distances pairs, containing a pair of
	 * trajectories IDs and their distance, the result list
	 * contains (list1.size * list2.size) elements.
	 */
	private List<DistancePair> getDistances() {
		List<DistancePair> distancePairs = 
				new ArrayList<>(datasetA.size() * datasetB.size());
		double distance;
		
		final long startTime = System.currentTimeMillis();
		for (Trajectory t1 : datasetA) {
			String tid1 = t1.getId();
			for (Trajectory t2 : datasetB) {
				String tid2 = t2.getId();
				distance = trajectoryDistanceFunc.getDistance(t1, t2);
				distancePairs.add(new DistancePair(tid1, tid2, distance));
			}
		}
		final long endTime = System.currentTimeMillis();
		runningTime = endTime - startTime;
		
		return distancePairs;
	}
	
	// This should be in the Model
	/**
	 * Run the required transformations on each dataset.
	 * Several transformations may be performed on each
	 * dataset.
	 */
	private void doTransformations() {
		try {	
			// add noise transformation
			if (addNoiseCheck2.isSelected()) {
				double noiseRate = Double.parseDouble(addNoiseRateTxt2.getText());
				double noiseDist = Double.parseDouble(addNoiseDistanceTxt2.getText());
				datasetB = TrajectoryTransformationService
							.addNoise(datasetB, noiseRate, noiseDist);
			}
			// shift points transformation
			if (shiftPointsCheck2.isSelected()) {
				double rate = Double.parseDouble(shiftPointsRateTxt2.getText());
				double dist = Double.parseDouble(shiftPointsDistanceTxt2.getText());
				datasetB = TrajectoryTransformationService
							.shiftPoints(datasetB, rate, dist);
			}
			// add points transformation
			if (addPointsCheck2.isSelected()) {
				double rate = Double.parseDouble(addPointsRateTxt2.getText());
				datasetB = TrajectoryTransformationService
							.addPoints(datasetB, rate);
			}
			// remove points transformation
			if (removePointsCheck2.isSelected()) {
				double rate = Double.parseDouble(removePointsRateTxt2.getText());
				datasetB = TrajectoryTransformationService
							.removePoints(datasetB, rate);
			}
			// change sampling rate transformation
			if (samplingRateCheck2.isSelected()) {
				int rate = Integer.parseInt(samplingRateTxt2.getText());
				datasetB = TrajectoryTransformationService
							.samplingRate(datasetB, rate);
			}
			// change scale transformation
			if (scaleCheck2.isSelected()) {
				double rate = Double.parseDouble(scaleRateTxt2.getText());
				datasetB = TrajectoryTransformationService
							.scale(datasetB, rate);
			}
			// shift time transformation
			if (timeShiftCheck2.isSelected()) {
				int startTime = Integer.parseInt(timeShiftStartTxt2.getText());
				datasetB = TrajectoryTransformationService
							.shiftTime(datasetB, startTime);
			}		
			// rotation transformation
			if (rotationCheck2.isSelected()) {
				double angle = Double.parseDouble(rotationAngleTxt2.getText());
				datasetB = TrajectoryTransformationService
							.rotate(datasetB, angle);
			}
			// translation transformation
			if (translationCheck2.isSelected()) {
				double x = Double.parseDouble(translationXTxt2.getText());
				double y = Double.parseDouble(translationYTxt2.getText());
				datasetB = TrajectoryTransformationService
							.translate(datasetB, x, y);
				
			}
		} catch (IllegalArgumentException e) {
			showErrorMessage("In Dataset B parameters.\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	// This should be in the Model
	/**
	 * Sorts the list of disntace pairs using the user-specified parameter.
	 * @param distancePairs The list to sort.
	 */
	private void doSort(List<DistancePair> distancePairs) {
		final String sortOption = sortResultsChoiceBox.getValue();
		if (sortOption.equals(DATASET_A)) {
			Collections.sort(distancePairs, DistancePair.ID_1_COMPARATOR);
		} else
		if (sortOption.equals(DATASET_B)) {
			Collections.sort(distancePairs, DistancePair.ID_2_COMPARATOR);
		} else 
		if (sortOption.equals(DISTANCE)) {
			Collections.sort(distancePairs, DistancePair.DISTANCE_COMPARATOR);
		}
	}
	
	// This should be in the Model
	/**
	 * Normalize the distance values as required.
	 * 
	 * @param distancePairs The list to normalize.
	 * @return The list of values after normalization.
	 */
	private List<DistancePair> doNormalization(List<DistancePair> distancePairs) {
		final String normalizationOption = normalizationChoiceBox.getValue();
		
		// nothing to do
		if (normalizationOption.equals(NONE)) return distancePairs;
		
		// get the distances values
		List<Double> distances = new ArrayList<>(distancePairs.size());
		for (DistancePair pair : distancePairs) {
			distances.add(pair.getDistance());
		}
		
		// MIN-MAX normalization
		if (normalizationOption.equals(MIN_MAX)) {
			double min;
			double max;
			try {
				min = Double.parseDouble(normalizationMinTxt.getText());
				max = Double.parseDouble(normalizationMaxTxt.getText());
				distances = SpatialUtils.minMaxNormalization(distances, min, max);
			} catch (IllegalArgumentException e) {
				showErrorMessage("Invalid values for Min/Max normalization.");
			}
		} else 
		// MEAN-STD - Z-scores normalization	
		if (normalizationOption.equals(MEAN_STD)) {
			distances = SpatialUtils.meanStdNormalization(distances);
		}
		
		// update values
		List<DistancePair> newPairs = new ArrayList<>(distancePairs.size());
		for (int i=0; i<distances.size(); i++) {
			double normDist = distances.get(i);
			DistancePair pair = distancePairs.get(i);
			newPairs.add(new DistancePair(pair.getTrajectory1ID(), 
										  pair.getTrajectory2ID(),
										  normDist));
		}
		
		return newPairs;
	}
	
	// This should be in the Model
	/**
	 * Save the benchmark results to the user-defined directory.
	 * 
	 * @param distancePairs The list with the results.
	 */
	private void saveResults(List<DistancePair> distancePairs) {
		String fileName   = distanceFuncName + "-benchmark-results.txt";
		String outputPath = outputDataTxt.getText();
		try {
			IOService.writeFile(distancePairs, outputPath, fileName);
		} catch (IOException e) {
			showErrorMessage("Error saving results to output directory.");
		}
	}

	/**
	 * Validates the required parameters.
	 * 
	 * @return True if validation passed.
	 */
	private boolean validate() {
		if (inputDataTxt1.getText().isEmpty()) {
			showErrorMessage("Please, provide a path to dataset A.");
			inputDataTxt1.requestFocus();
			return false;
		}
		if (inputDataTxt2.getText().isEmpty()) {
			showErrorMessage("Please, provide a path to dataset B.");
			inputDataTxt2.requestFocus();
			return false;
		}
		if (outputDataTxt.getText().isEmpty()) {
			showErrorMessage("Please, provide a path to the output data.");
			outputDataTxt.requestFocus();
			return false;
		}
		return true;
	}
	
	/**
	 * Open a simple ERROR alert/dialog with the given message.
	 * 
	 * @param message The message content.
	 */
	private void showErrorMessage(String message){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Message");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.show();
	}
	
	/**
	 * Open a simple INFORMATION alert/dialog with the given message.
	 * 
	 * @param message The message content.
	 */
	private void showInfoMessage(String message){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info Message");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.show();
	}
	
	/****************************
	 * Chart Tab controls 
	 ***************************/
	// Distances chart controls
	@FXML
	private LineChart<String,Number> distanceChart;
	private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis   yAxis = new NumberAxis();
    @FXML
    private TextField chartDataPathTxt;
    @FXML
    private TextField chartSeriesNameTxt;
    
	@FXML 
	private void actionOpenChartData() {
		final FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Distances Data File");
    	final File selectedFile = fileChooser.showOpenDialog(
        		rootPane.getScene().getWindow());
        if (selectedFile != null) {
        	String dataPath = selectedFile.getAbsolutePath();
        	chartDataPathTxt.setText(dataPath);
        	chartDataPathTxt.home();
        }
	}
	
    @FXML
	private void actionLoadDataToChart() {
		// create the chart axis
        xAxis.setLabel("Trajectories");
        yAxis.setLabel("Distance");

        final String dataPath = chartDataPathTxt.getText();
        final String seriesName = chartSeriesNameTxt.getText();
        try {
        	// Load distances data
			List<String> chartData = IOService.readFile(dataPath);
			
			// create a new series for every input data file
			XYChart.Series<String, Number> series = new XYChart.Series<String,Number>();
	        series.setName(seriesName);
	        
	        // add data values to chart series
	        String xVal;
	        double yVal;
	        for (String entry : chartData) {
	        	String[] vals = entry.split(",");
	        	xVal = vals[0] +"-"+ vals[1];
	        	yVal = Double.parseDouble(vals[2]);
	        	// add to chart series
	        	series.getData().add(new XYChart.Data<String,Number>(xVal, yVal));
	        }
	        
	        // add series to chart
	        distanceChart.getData().add(series);
		} catch (IOException e) {
			showErrorMessage("Unable to open data location.");
			e.printStackTrace();
		}
	}
	
    @FXML
    public void actionClearChart() {
    	distanceChart.getData().clear();
    	distanceChart.autosize();
    }

}
