
public interface NaiveBayesClassifier {
	void train(Instance[] trainingData, int v);
	double p_l(Label label);	
	double p_w_given_l(String word, Label label);	
	Label classify(String[] words);
	void show_informative_5words();
}
