package traminer.benchmark.distance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import traminer.util.trajectory.Trajectory;
import traminer.util.trajectory.transformation.*;

/**
 * Service to perform transformations on a given trajectory dataset.
 * 
 * @author douglasapeixoto
 */
@SuppressWarnings("serial")
public class TrajectoryTransformationService implements Serializable {
	/** The transformation method to use */
	private static TrajectoryTransformation transformation;

	/**
	 * Add random noise to the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param rate Rate of noise to add (0.0 = 0%, 1.0 = 100%).
	 * @param distance Distance threshold for noisy points.
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> addNoise(List<Trajectory> trajectoryList, double rate, double distance) 
			throws IllegalArgumentException {
		transformation = new AddNoiseTransformation(rate, distance);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Randomly shift the points of the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param rate Rate of points to shift (0.0 = 0%, 1.0 = 100%).
	 * @param distance Distance for random shifting points.
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> shiftPoints(List<Trajectory> trajectoryList, double rate, double distance) 
			throws IllegalArgumentException {
		transformation = new RandomShiftTransformation(rate, distance);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Randomly add points to the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param rate The rate of point to add to each trajectory (0.0 = 0%, 1.0 = 100%).
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 */
	public static List<Trajectory> addPoints(List<Trajectory> trajectoryList, double rate) 
			throws IllegalArgumentException {
		transformation = new AddPointsTransformation(rate);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Randomly remove points from the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param rate The rate of point to remove from each trajectory (0.0 = 0%, 1.0 = 100%).
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> removePoints(List<Trajectory> trajectoryList, double rate) 
			throws IllegalArgumentException {
		transformation = new DeletePointsTransformation(rate);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Changes the sampling rate of the trajectory points 
	 * in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param New point's time interval (sampling rate).
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> samplingRate(List<Trajectory> trajectoryList, long rate) 
			throws IllegalArgumentException {
		transformation = new SamplingRateTransformation(rate);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Changes the scale of the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param rate The rate of scale (e.g. 0.5 = 50%, 1.0 = 100%, 2.0 = 200%).
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> scale(List<Trajectory> trajectoryList, double rate) 
			throws IllegalArgumentException {
		transformation = new ScaleTransformation(rate);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Shifts the time-stamp of the trajectory points 
	 * in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param startTime The new start time of the trajectories in the dataset.
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> shiftTime(List<Trajectory> trajectoryList, long startTime) 
			throws IllegalArgumentException {
		transformation = new TimeShiftTransformation(startTime);
		return doTransformation(trajectoryList);
	}

	/**
	 * Rotates the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param angle The angle of rotation, in degrees.
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> rotate(List<Trajectory> trajectoryList, double angle)
			throws IllegalArgumentException {
		transformation = new RotationTransformation(angle);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Translates the trajectories in the given dataset.
	 * 
	 * @param trajectoryList List of trajectories to transform.
	 * @param x X-axis/Longitude translation value. 
	 * @param y Y-axis/Latitude translation value.
	 * 
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 * @throws IllegalArgumentException
	 */
	public static List<Trajectory> translate(List<Trajectory> trajectoryList, double x, double y) 
			throws IllegalArgumentException {
		transformation = new TranslationTransformation(x, y);
		return doTransformation(trajectoryList);
	}
	
	/**
	 * Runs the transformation on the given dataset.
	 * 
	 * @param trajectoryList Dataset to transform.
	 * @return The list with a the copy of the given trajectories 
	 * after transformation.
	 */
	private static List<Trajectory> doTransformation(List<Trajectory> trajectoryList) {
		List<Trajectory> result = new ArrayList<Trajectory>(trajectoryList.size());
		for (Trajectory t : trajectoryList) {
			result.add(transformation.getTransformation(t));
		}
		return result;
	}
}
