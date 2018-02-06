package traminer.benchmark.distance;

/**
 * List of trajectory distance functions.
 * 
 * @author douglasapeixoto
 */
public enum DistanceFunction {
	DISSIM,		// Dissimilarity measure
	DTW,		// Dynamic time warping
	EUCLIDEAN,	// Euclidean distance EDC
	EDR,		// Edit distance
	EDwP,		// Edit distance with projection
	ERP,		// Edit distance with real penalty
	FRECHET,	// Frechet distance
	LCSS,		// Largest common sub-sequence
	LIP,		// Locality In-between Polylines
	OWD,		// One Way distance
	PDTW,		// Piecewise Dynamic time warping
	STED,		// Spatial-temporal edit distance
	STLCSS,		// Spatial-temporal Largest common sub-sequence
	STLIP,		// Spatial-Temporal Locality In-between Polylines
	TID			// Transformation Innovation Distance
}
