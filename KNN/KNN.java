import java.util.HashMap;

/**
 * A kNN classification algorithm implementation.
 * 
 */

public class KNN {

	/**
	 * In this method, you should implement the kNN algorithm. You can add 
	 * other methods in this class, or create a new class to facilitate your
	 * work. If you create other classes, DO NOT FORGET to include those java
   * files when preparing your code for hand in.
   *
	 * Also, Please DO NOT MODIFY the parameters or return values of this method,
   * or any other provided code.  Again, create your own methods or classes as
   * you need them.
	 * 
	 * @param trainingData
	 * 		An Item array of training data
	 * @param testData
	 * 		An Item array of test data
	 * @param k
	 * 		The number of neighbors to use for classification
	 * @return
	 * 		The object KNNResult contains classification accuracy, 
	 * 		category assignment, etc.
	 */
	public KNNResult classify(Item[] trainingData, Item[] testData, int k) {

		KNNResult result = new KNNResult();
		double[][] distanceMatrix = createDistanceMatrix(testData,trainingData);
		result.nearestNeighbors = deriveNearestNeighbors(trainingData,testData.length,distanceMatrix,k);
		result.categoryAssignment = assignLabels(result.nearestNeighbors,trainingData);
		result.accuracy = computeAccuracy(result.categoryAssignment,testData);

		return result;
	}


	private double computeAccuracy(String[] categoryAssignment, Item[] testData) {
		double accuracyCount = 0;
		double totalCount = categoryAssignment.length;
		 for(int i=0;i<categoryAssignment.length;i++){
			 if(testData[i].category.equals(categoryAssignment[i])){
				 accuracyCount++;
			 }
		 }
		return (accuracyCount/totalCount);
	}


	private String[] assignLabels(String[][] nearestNeighbors,
			Item[] trainingData) {
		String[] labels = new String[nearestNeighbors.length];
		String[] priority = {"nation","machine","fruit"};
		HashMap<String,String> dataToLabelMap = createDataLabelMap(trainingData);
		for(int i=0;i<nearestNeighbors.length;i++){
		int[] votes = new int[3];
		for(int j=0;j<nearestNeighbors[i].length;j++){
			String dataName = dataToLabelMap.get(nearestNeighbors[i][j]);
			if(dataName.equals(priority[0])){
				votes[0]++;
			} else if(dataName.equals(priority[1])){
				votes[1]++;
			} else if(dataName.equals(priority[2])){
				votes[2]++;
			}
			
		}
		/*
		 * nation,machine,fruit
		 */
		if(votes[0]>votes[1]){
			if(votes[0]>votes[2]){
				labels[i] = priority[0];
			}else if(votes[0]<votes[2]){
				labels[i] = priority[2];
			} else {
				labels[i] = priority[0];
				
			}
		} else if(votes[0]<votes[1]){
			if(votes[1]>votes[2]){
				labels[i] = priority[1];
			} else if(votes[1]<votes[2]){
				labels[i] = priority[2];
			} else {
				labels[i]=priority[1];
			}
		} else {
			if(votes[0]>votes[2]){
			labels[i]=priority[0];} else if(votes[0]<votes[2])
			{labels[i]=priority[2];
			}else{
				labels[i]=priority[0];
			}		 
		}
		
		}
		
		return labels;
	}


	private HashMap<String, String> createDataLabelMap(Item[] trainingData) {
		HashMap<String,String> map = new HashMap<String,String>();
		for(Item item:trainingData){
			map.put(item.name, item.category);
		}
		return map;
	}


	private String[][] deriveNearestNeighbors(Item[] trainingData,int sizeTestData,
			double[][] distanceMatrix, int k) {
		int sizeTrainingData = trainingData.length;
		String[] dataNames = null;
		String[][] kNearestNeighbors = new String[sizeTestData][k];

		for(int i=0;i<sizeTestData;i++){
			dataNames = getDataNames(trainingData);
			int minIndex =0;
			double minValue =0;
			for(int j=0; j<k;j++){
				minIndex = j;
				minValue = distanceMatrix[i][j];
				for(int l= j+1; l<sizeTrainingData;l++){
					if(distanceMatrix[i][l]<minValue){
						minIndex = l;
						minValue
						= distanceMatrix[i][l];
					}				
				}
				String temp = dataNames[j];
				dataNames[j] = dataNames[minIndex];
				dataNames[minIndex] = temp;
				double tempDistance = distanceMatrix[i][j];
				distanceMatrix[i][j]=distanceMatrix[i][minIndex];
				distanceMatrix[i][minIndex]=tempDistance;
			}
			for(int m=0;m<k;m++){
				kNearestNeighbors[i][m] = dataNames[m];
				
			}
		}
		
		
		
		return kNearestNeighbors;
	}

	private String[] getDataNames(Item[] trainingData) {
		int sizeTestData = trainingData.length;
		String[] dataNames = new String[sizeTestData];
		
		for(int i=0;i< sizeTestData ;i++){
			dataNames[i] = trainingData[i].name;
			
		}
		
		return dataNames;
	}

	private double[][] createDistanceMatrix(Item[] testData, Item[] trainingData) {
		 int sizeTrainingData = trainingData.length;
		 int sizeTestData = testData.length;
		 double[][] distanceMatrix = new double[sizeTestData][sizeTrainingData];
		 
		 for(int i =0;i<sizeTestData;i++){
			 for(int j=0;j<sizeTrainingData;j++){
				 distanceMatrix[i][j] = calculateDistance(testData[i].features,trainingData[j].features);
			 }
		 }
		return distanceMatrix;
	}

	private double calculateDistance(double[] feature1, double[] feature2) {
		double Sum = 0.0;
        for(int i=0;i<feature1.length;i++) {
           Sum = Sum + Math.pow((feature1[i]-feature2[i]),2.0);
        }
        return Math.sqrt(Sum);
	}

	
	
}
