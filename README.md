# Trajectory Distance Measures Benchmark Tool
----------
A Benchmark System for Trajectory Similarity/Distance Measures with the following functionalities:

-	**Choose well-suited techniques.** Each technique has
distinct capabilities. This tool will serve as a practical
guideline for how to select well-suited trajectory distance
measure on particular application scenarios.

- **Guide to select appropriate parameters.** Our tool also allows users to vary configurable parameters and visualize their effects. Through empirical observations, the users can select the appropriate parameter configuration for their applications.

- **Reduce the development complexity.** Due to the number and complexity of approaches, it might be challenging and time-consuming for users to understand and implement all those techniques. Our application comes with a library containing all described distance measures and transformations, and makes it easy to add new features and visualize the results. Therefore, using our tool as a reusable framework, developers can reduce development effort.

To support these functionalities, we design our tool with
three main features: **(i)** trajectory data transformation module, **(ii)** re-implement state-of-the-art trajectory distance measures within a common framework, **(iii)** a mean to evaluate these techniques with different parameters using a GUI. 

The following image shows the application main GUI window. 

![img](https://github.com/douglasapeixoto/trajectory-distance-benchmark/blob/master/app-window.png)


## Transformations
----------

- **Add Noise:**  Add some noise to the given trajectory.
				-- *Noise Rate:* Rate of noise to add (0.0 = 0%, 1.0 = 100%).
				--	*Noise Distance:* Distance threshold for noisy points.

- **Add Points:** Add some extra points to the given trajectory.
		-- *Add Rate:* Rate of points to add (0.0 = 0%, 1.0 = 100%).

- **Remove Points:** Delete some points from the given trajectory.
		-- *Delete Rate:* Rate of points to delete (0.0 = 0%, 1.0 = 100%).

- **Random Shift:** Randomly shift some of the trajectory points.
     -- *Shift Rate:* The rate of points to shift (0.0 = 0%, 1.0 = 100%).
     -- *Shift Distance:* Distance for random shifting points.

- **Rotation:** Rotates the whole trajectory by a given angle.
		-- *Rotation Angle:* In degrees, e.g. 30, 60, 135, etc.

- **Sampling Rate:**  Changes the sampling rate of the trajectory points. Make the time interval between every sample point of the trajectory equals a given rate.
		-- *Sampling Rate:* New time interval for the trajectory sample points .
		
- **Scale:** Changes the scale of a trajectory by a given ratio.
		-- *Scale Ratio:* The new scale ratio (0.5 = 50%, 1.0 = 100%, 2.0 = 200%, and so on).

- **Time Shift:** Shifts the time period of a trajectory,  make it starts from a new time ``t = startTime``.
		-- *Start Time:* The new trajectory's start time.
		
- **Translation:**  Translates a trajectory on the XY axis for the given translation values.
	- *Translation X:* Translation value over the X axis, may be positive or negative.
	- *Translation Y:* Translation value over the Y axis,  may be positive or negative.



## Distance/Similarity Measures
----------

- **DISSIM:** Dissimilarity distance function.
	-- Frentzos, Elias, Kostas Gratsias, and Yannis Theodoridis.  "Index-based most similar trajectory search.", ICDE 2007.

- **DTW:** Dynamic Time Warping for time series.
	-- Yi, B-K and Jagadish, HV and Faloutsos, Christos. "Efficient retrieval of similar time sequences under time warping". In ICDE (1998).
	--Keogh, Eamonn J and Pazzani, Michael J. "Scaling up dynamic time warping for datamining applications." In ACM SIGKDD (2000).
	-- Keogh, Eamonn, and Chotirat Ann Ratanamahatana. "Exact indexing of dynamic time warping." In Knowledge and information systems (2005).

- **EDC:** Euclidean Distance for 2D Point Series (Trajectories).

- **EDR:** Edit Distance on Real sequences.
	--Chen, Lei, M. Tamer Özsu, and Vincent Oria. "Robust and fast similarity search for moving object trajectories." In. ACM SIGMOD, 2005. 

- **EDwP:** Edit Distance with Projections.
	-- Ranu, Sayan, P. Deepak, Aditya D. Telang, Prasad Deshpande, and Sriram Raghavan. "Indexing and matching trajectories under inconsistent sampling rates.", ICDE, 2015.

- **ERP:** Edit distance with Real Penalty.
	-- Chen, Lei, and Raymond Ng. "On the marriage of lp-norms and edit distance." In. VLDB Endowment, 2004.

- **Frechet:** Trajectory Distance measure.
	-- Buchin, Kevin, Maike Buchin, and Yusu Wang. "Exact algorithms for partial curve matching via the Fréchet distance." In. ACM-SIAM, 2009.
	-- Alt, Helmut, and Michael Godau. "Computing the Fréchet distance between two polygonal curves." International Journal of Computational Geometry & Application, 1995. 

- **LCSS:** Largest Common Subsequence distance.
	-- Vlachos, Michail, George Kollios, and Dimitrios Gunopulos. "Discovering similar multidimensional trajectories." ICDE, 2002. 

- **LIP:** Locality In-between Polylines - trajectory distance measure.
	-- Pelekis, Nikos, Ioannis Kopanakis, Gerasimos Marketos, Irene Ntoutsi, Gennady Andrienko, and Yannis Theodoridis. "Similarity search in trajectory databases." In IEEE International Symposium on Temporal Representation and Reasoning, 2007.

- **OWD:** One Way Distance trajectory distance measure.
	-- Lin, Bin, and Jianwen Su. "Shapes based trajectory queries for moving objects." In ACM international workshop on Geographic information systems, 2005. 

- **PDTW:** Trajectory distance measure.
	-- Keogh, Eamonn J., and Michael J. Pazzani. "Scaling up dynamic time warping for datamining applications." In ACM SIGKDD, 2000. 

- **STED:** Spatial-Temporal Edit Distance.
	-- Yuan, Yihong, and Martin Raubal. "Measuring similarity of mobile phone user trajectories – a Spatio-temporal Edit Distance method." In International Journal of Geographical Information Science, 2014. 

- **STLCSS:** Spatial-Temporal Largest Common Subsequence distance.
	-- Vlachos, Michail, Dimitrios Gunopulos, and George Kollios. "Robust similarity measures for mobile object trajectories." In IEEE Database and Expert Systems Applications, 2002.

- **STLIP:** Spatial-Temporal Locality In-between Polylines.
	-- Pelekis, Nikos, Ioannis Kopanakis, Gerasimos Marketos, Irene Ntoutsi, Gennady Andrienko, and Yannis Theodoridis. "Similarity search in trajectory databases." In IEEE International Symposium on Temporal Representation and Reasoning, 2007.

- **TID:** Transformation Innovation Distance.
