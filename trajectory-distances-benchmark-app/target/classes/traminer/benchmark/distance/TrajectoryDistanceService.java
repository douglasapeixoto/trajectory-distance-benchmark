package traminer.benchmark.distance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import traminer.benchmark.distance.DistancePair;
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
 * Application Model to calculate distances between two 
 * trajectory datasets using several methods.
 * 
 * @author douglasapeixoto
 */
@SuppressWarnings("serial")
public class TrajectoryDistanceService implements Serializable {
	/** The distance function to use */
	private static TrajectoryDistanceFunction trajectoryDistance;

	public static List<DistancePair> getDISSIM(List<Trajectory> datasetA, List<Trajectory> datasetB, int increment) {
		trajectoryDistance = new DISSIMDistanceCalculator(increment);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getDTW(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new DTWDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getEDC(List<Trajectory> datasetA, List<Trajectory> datasetB) {
		trajectoryDistance = new EDCDistanceCalculator();
		return getDistances(datasetA, datasetB);
	}

	public static List<DistancePair> getEDR(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist, double threshold) {
		trajectoryDistance = new EDRDistanceCalculator(threshold, pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getEDwP(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new EDwPDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getERP(List<Trajectory> datasetA, List<Trajectory> datasetB, double threshold) {
		trajectoryDistance = new ERPDistanceCalculator(threshold);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getFrechet(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new FrechetDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getLCSS(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist, double distanceThreshold, long timeThreshold) {
		trajectoryDistance = new LCSSDistanceCalculator(distanceThreshold, timeThreshold);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getLIP(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new LIPDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getOWD(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new OWDDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}

	public static List<DistancePair> getPDTW(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist, double n) {
		trajectoryDistance = new PDTWDistanceCalculator(n, pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getSTED(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new STEDDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}

	public static List<DistancePair> getSTLCSS(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist, double distanceThreshold, long timeThreshold) {
		trajectoryDistance = new STLCSSDistanceCalculator(distanceThreshold, timeThreshold);
		return getDistances(datasetA, datasetB);
	}

	public static List<DistancePair> getSTLIP(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist, double k) {
		trajectoryDistance = new STLIPDistanceCalculator(k, pointDist);
		return getDistances(datasetA, datasetB);
	}
	
	public static List<DistancePair> getTID(List<Trajectory> datasetA, List<Trajectory> datasetB, PointDistanceFunction pointDist) {
		trajectoryDistance = new TIDDistanceCalculator(pointDist);
		return getDistances(datasetA, datasetB);
	}	
	
	/**
	 * Calculates the distances between every trajectory in the
	 * first list to every trajectory in the second list.
	 * 
	 * @return A list of distances pairs, containing a pair of
	 * trajectories IDs and their distance, the result list
	 * contains (list1.size * list2.size) elements.
	 */
	private static List<DistancePair> getDistances(List<Trajectory> datasetA, List<Trajectory> datasetB) {
		List<DistancePair> distancePairs = 
				new ArrayList<>(datasetA.size() * datasetB.size());
		double distance;
		for (Trajectory t1 : datasetA) {
			String tid1 = t1.getId();
			for (Trajectory t2 : datasetB) {
				String tid2 = t2.getId();
				distance = trajectoryDistance.getDistance(t1, t2);
				distancePairs.add(new DistancePair(tid1, tid2, distance));
			}
		}
		return distancePairs;
	}
}
