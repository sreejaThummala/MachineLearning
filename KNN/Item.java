public class Item {
	
	// The category of the instance
	public String category;
	
	// The name of the instance
	public String name;
	
	// The feature vector of the instance
	public double[] features;
	
	public Item() {
		// The length of a feature vector is 3
		features = new double[3];
	}
	
	public Item(String category, String name, double[] features) {
		this.category = category;
		this.name = name;
		this.features = features;
	}
}
