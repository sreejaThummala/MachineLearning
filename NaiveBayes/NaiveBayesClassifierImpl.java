import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	static double spamProbability = 0;
	static double hamProbability = 0;
	int spamCount = 0;
	int hamCount =0;
	public double smoothnessFactor = 0.00001;
	public Map<String,Integer> spamFrequencyMap; 
	public Map<String,Integer> hamFrequencyMap;
	Set<String> vocabulary = null;
	@Override
	public void train(Instance[] trainingData, int v) {
		Map<String,Integer> frequencyMap = new HashMap<String,Integer>();
		spamFrequencyMap = new HashMap<String,Integer>();
		hamFrequencyMap = new HashMap<String,Integer>();
		int documents = trainingData.length;
		
		int spamDocuments = 0;
		int hamDocuments = 0;
		for(Instance instance: trainingData){
			//System.out.println(instance.label);
			String[] words = instance.words;
			Label label = instance.label;
			switch(label){
			case SPAM:
				spamDocuments++;
				break;
			case HAM:
				hamDocuments++;
				break;
			}
			for(String word:words){
				Integer integer = null;
				integer = frequencyMap.get(word);
				if(integer==null){
						frequencyMap.put(word, 1);
						} else{
						frequencyMap.put(word, integer+1);
						}
			
				switch(label){
				case SPAM:
					integer = spamFrequencyMap.get(word);
					if(integer==null){
						spamFrequencyMap.put(word, 1);
					} else{
						spamFrequencyMap.put(word, integer+1);
					}
					
					spamCount++;
					break;
				case HAM:
					integer = hamFrequencyMap.get(word);
					if(integer==null){
						hamFrequencyMap.put(word, 1);
					} else{
						hamFrequencyMap.put(word, integer+1);
					}
					hamCount++;
					break;
				}	
			}
		}
		
		//calculate probabilities
		//spam probability
		//System.out.println(spamFrequencyMap.get("friday"));
		//System.out.println(hamFrequencyMap.get("friday"));
		spamProbability = ((double)spamDocuments)/((double)documents);
		hamProbability = ((double)hamDocuments)/((double)documents);
		
		//System.out.println(spamCount);
		//ham probability 
		
		//word probability
		//spam probability
		
		 vocabulary = frequencyMap.keySet();
		 
		 /*
		for(String word: vocabulary){
			double spamprobability = ( (double)(spamFrequencyMap.get(word)==null?0:s
			pamFrequencyMap.get(word))+smoothnessFactor )/ 
			((double) spamCount + (vocabSize)*smoothnessFactor) ;
			spamWordProbability.put(word, spamprobability);
			//System.out.println(hamFrequencyMap);
			double hamprobability = ( (double)(hamFrequencyMap.get(word)==null?0:hamFrequencyMap.get(word))+smoothnessFactor )/ ((double) hamCount + (vocabSize)*smoothnessFactor);
			hamWordProbability.put(word, hamprobability);
			//ham probability
		}*/
		//ham probability
		 //System.out.println(p_w_given_l("great", Label.HAM));
		 //System.out.println(p_w_given_l("friday",Label.SPAM));
	
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(SPAM) or P(HAM)
	 */
	@Override
	public double p_l(Label label) {
		double probability = 0;
		switch(label){
		case SPAM:
			return spamProbability;
		case HAM:
			return hamProbability;
		}
		return probability;
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|SPAM) or P(word|HAM)
	 */
	@Override
	public double p_w_given_l(String word, Label label) {
		double probability = 0;
		 int vocabSize = vocabulary.size();
		switch(label){
		case SPAM:
			return ((double)(spamFrequencyMap.get(word)==null?0:spamFrequencyMap.get(word))+smoothnessFactor )/ ((double) spamCount + (vocabSize)*smoothnessFactor);
		case HAM:
			return ( (double)(hamFrequencyMap.get(word)==null?0:hamFrequencyMap.get(word))+smoothnessFactor )/ ((double) hamCount + (vocabSize)*smoothnessFactor);
		}
		return probability;
	}
	
	/**
	 * Classifies an array of words as either SPAM or HAM. 
	 */
	@Override
	public Label classify(String[] words) {
		Label classification = null;
		double spamProbabilitySum = 0;
		double hamProbabilitySum = 0;
		for(String word:words){
			double spamWordProbability = Math.log(p_w_given_l(word, Label.SPAM));
			spamProbabilitySum += spamWordProbability;
			double hamWordProbability = Math.log(p_w_given_l(word, Label.HAM));
			hamProbabilitySum += hamWordProbability;		
		}
		//System.out.println(p_l(Label.HAM));
		//System.out.println(p_l(Label.SPAM));
		
		spamProbabilitySum += Math.log(p_l(Label.SPAM));
		hamProbabilitySum += Math.log(p_l(Label.HAM));
		if(spamProbabilitySum>hamProbabilitySum)
			classification = Label.SPAM;
		if(hamProbabilitySum>spamProbabilitySum)
			classification = Label.HAM;
		if(hamProbabilitySum==spamProbabilitySum)
			classification = Label.SPAM;
		return classification;
	}
	
	/**
	 * Print out 5 most informative words.
	 */
	public void show_informative_5words() {
		Map<String,Double> informativeness = new HashMap<String,Double>();
		for(String word:vocabulary){
			double wordInformativeness = 0;
			double spamProbability = p_w_given_l(word, Label.SPAM);
			double hamProbability = p_w_given_l(word, Label.HAM);
			if(spamProbability<hamProbability)
			{
				wordInformativeness = hamProbability/spamProbability;
			} else{
				wordInformativeness = spamProbability/hamProbability;
			}
			//System.out.println(wordInformativeness+" "+word);
			 informativeness.put(word, wordInformativeness);
			 
		}
		String[] top5 = findTop5(informativeness);
		for(int i=0;i<5;i++){
			System.out.println("("+(i+1)+")"+top5[i]+" "+informativeness.get(top5[i]));
		}
	}

	private String[] findTop5(Map<String, Double> informativeness) {
		
		int k =5;
		String[] top = new String[k];
		int size = informativeness.size();
		Object[] valueArray = (informativeness.values().toArray());
		Object[] keyArray = (informativeness.keySet().toArray());
		int maxIndex =0;
		double maxValue =0;
		for(int j=0; j<k;j++){
			maxIndex = j;
			maxValue = ((Double)valueArray[j]).doubleValue();
			for(int l= j+1; l<size;l++){
				if(((Double)valueArray[l]).doubleValue()>maxValue){
					maxIndex = l;
					maxValue = ((Double)(valueArray[l])).doubleValue();
					
				}
				
			}
			Object tempDouble = valueArray[j];
			Object tempString = keyArray[j];
			valueArray[j] = valueArray[maxIndex];
			keyArray[j] = keyArray[maxIndex];
			valueArray[maxIndex] = tempDouble;
			keyArray[maxIndex] = tempString;
	}
	for(int i=0;i<k;i++){
			top[i] = (String)keyArray[i];
	}
	return top;
	}
	
}
