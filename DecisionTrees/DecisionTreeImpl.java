import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 * 
 * You must add code for the 4 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {

	private static DecTreeNodeImpl root;
	private Map<String, List<String>> attributeListMap;
	private Map<Integer, String> attributeIndex;
	private Map<String, Integer> reverseAttributeIndex;
	private int id = 0;
	private final String ATTRIBUTE1 = "status of existing checking account";
	private final String ATTRIBUTE2 = "credit history";
	private final String ATTRIBUTE3 = "purpose";
	private final String ATTRIBUTE4 = "savings account/bonds";
	private final String ATTRIBUTE5 = "duration in month";
	private final String ATTRIBUTE6 = "credit amount";
	private final String ATTRIBUTE7 = "foreign worker";

	/**
	 * Answers static questions about decision trees.
	 */
	DecisionTreeImpl() {
		// no code necessary
		// this is void purposefully
	}

	/**
	 * Build a decision tree given only a training set.
	 * 
	 * @param train
	 *            the training set
	 */
	DecisionTreeImpl(DataSet train) {
		createAttributeIndex();
		createAttributeListMap();
		createReverseAttributeIndex();
		
		List<Instance> examples = train.instances;
		List<Integer> attributes = getInitialAttributes();
		String defaultLabel = majorityValue(examples);

		root = new DecTreeNodeImpl();
		root.setExamples(examples);
		root.setParentAttributeValue("Root");
		root.setParent(null);
		root.setNodeID(id);
		((DecTreeNodeImpl) root).setNodeRoot(true);
		
		decisionTreeLearning(examples, attributes, defaultLabel,
				(DecTreeNodeImpl) root);
	}

	private double getMidpoint(int i, List<Instance> examples) {
		int attributeMin = Integer.MAX_VALUE;
		int attributeMax = Integer.MIN_VALUE;
		int currentAttribute = 0;

		for (Instance instance : examples) {
			List<String> instanceAttributes = instance.attributes;

			currentAttribute = Integer.parseInt(instanceAttributes.get(i));
			

			if (currentAttribute <= attributeMin) {
				attributeMin = currentAttribute;
			}
			if (currentAttribute > attributeMax) {
				attributeMax = currentAttribute;
			}


		}

		double midAttribute = (double) (attributeMin + attributeMax)
				* (double) 0.5;
		return midAttribute;
	}
	
	private void createReverseAttributeIndex() {
		reverseAttributeIndex = new HashMap<String, Integer>();
		reverseAttributeIndex.put(ATTRIBUTE1, 0);
		reverseAttributeIndex.put(ATTRIBUTE2, 1);
		reverseAttributeIndex.put(ATTRIBUTE3, 2);
		reverseAttributeIndex.put(ATTRIBUTE4, 3);
		reverseAttributeIndex.put(ATTRIBUTE5, 4);
		reverseAttributeIndex.put(ATTRIBUTE6, 5);
		reverseAttributeIndex.put(ATTRIBUTE7, 6);
	}

	private void createAttributeListMap() {
		attributeListMap = new HashMap<String, List<String>>();
		List<String> attributeList0 = new LinkedList<String>();
		attributeList0.add("A11");
		attributeList0.add("A12");
		attributeList0.add("A13");
		attributeList0.add("A14");
		attributeListMap.put(ATTRIBUTE1, attributeList0);
		List<String> attributeList1 = new LinkedList<String>();
		attributeList1.add("A20");
		attributeList1.add("A21");
		attributeList1.add("A22");
		attributeList1.add("A23");
		attributeList1.add("A24");
		attributeListMap.put(ATTRIBUTE2, attributeList1);
		List<String> attributeList2 = new LinkedList<String>();
		attributeList2.add("A30");
		attributeList2.add("A31");
		attributeList2.add("A32");
		attributeList2.add("A33");
		attributeList2.add("A34");
		attributeList2.add("A35");
		attributeList2.add("A36");
		attributeList2.add("A37");
		attributeList2.add("A38");
		attributeList2.add("A39");
		attributeList2.add("A310");
		attributeListMap.put(ATTRIBUTE3, attributeList2);
		List<String> attributeList3 = new LinkedList<String>();
		attributeList3.add("A41");
		attributeList3.add("A42");
		attributeList3.add("A43");
		attributeList3.add("A44");
		attributeList3.add("A45");
		attributeListMap.put(ATTRIBUTE4, attributeList3);
		List<String> attributeList4 = new LinkedList<String>();
		attributeList4.add("A");
		attributeList4.add("B");
		attributeListMap.put(ATTRIBUTE5, attributeList4);
		List<String> attributeList5 = new LinkedList<String>();
		attributeList5.add("A");
		attributeList5.add("B");
		attributeListMap.put(ATTRIBUTE6, attributeList5);
		List<String> attributeList6 = new LinkedList<String>();
		attributeList6.add("A71");
		attributeList6.add("A72");
		attributeListMap.put(ATTRIBUTE7, attributeList6);

	}

	private void createAttributeIndex() {
		attributeIndex = new HashMap<Integer, String>();
		attributeIndex.put(0, ATTRIBUTE1);
		attributeIndex.put(1, ATTRIBUTE2);
		attributeIndex.put(2, ATTRIBUTE3);
		attributeIndex.put(3, ATTRIBUTE4);
		attributeIndex.put(4, ATTRIBUTE5);
		attributeIndex.put(5, ATTRIBUTE6);
		attributeIndex.put(6, ATTRIBUTE7);

	}

	private void decisionTreeLearning(List<Instance> examples,
			List<Integer> attributes, String defaultLabel, DecTreeNodeImpl node) {

		if (examples == null || examples.size() == 0) {
			node.setNodeLeaf(true);
			node.setLabel(defaultLabel);

		} else if (areSameClass(examples)) {
			node.setNodeLeaf(true);
			node.setLabel(examples.get(0).label);

		} else if (attributes == null || attributes.size() == 0) {
			String defLabel = majorityValue(examples);
			node.setNodeLeaf(true);
			node.setLabel(defLabel);

		} else {
			int bestAttribute = chooseAttribute(examples, attributes);
			String attribute = attributeIndex.get(bestAttribute);
			node.setAttribute(attribute);

			String defLabel = majorityValue(examples);

			attributes.remove(attributes.indexOf(bestAttribute));
			Map<String, List<Instance>> sets = divideSetsOnAttributes(examples,
					bestAttribute);
			
			

			for (String possibleAttribute : attributeListMap.get(attribute)) {
				
				List<Instance> examplesPossibleAttribute = sets
						.get(possibleAttribute);
				
				DecTreeNodeImpl child = new DecTreeNodeImpl();
				node.addChild(child);
				child.setExamples(examplesPossibleAttribute);
				child.setNodeID(++id);
				child.setParent(node);
				child.setParentAttributeValue(possibleAttribute);
				
				LinkedList<Integer> tempAttribute = new LinkedList<Integer>();
				for (int i = 0; i < attributes.size(); i++) {
					tempAttribute.add(attributes.get(i));
				}
				
				decisionTreeLearning(examplesPossibleAttribute, tempAttribute,
						defLabel, child);

			}
		}

	}


	private String majorityValue(List<Instance> examples) {
		int[] labelVotes = { 0, 0 };
		String majorityValue = null;
		for (Instance example : examples) {
			if (example.label.equals("1")) {
				labelVotes[0]++;
			}
			if (example.label.equals("2")) {
				labelVotes[1]++;
			}
		}
		if (labelVotes[0] >= labelVotes[1]) {
			majorityValue = "1";
		}
		if (labelVotes[1] > labelVotes[0]) {
			majorityValue = "2";
		}

		return majorityValue;

	}

	private int chooseAttribute(List<Instance> examples,
			List<Integer> attributes) {

		double maxGain = 0.0;
		int choosenAttributeIndex = attributes.get(0);
		for (int attributeIndex : attributes) {
			double tempGain = calculateGain(attributeIndex, examples);
			if (tempGain > maxGain) {
				

				maxGain = tempGain;
				choosenAttributeIndex = attributeIndex;
			}
			if(tempGain == maxGain){
				if(attributeIndex>choosenAttributeIndex)
					choosenAttributeIndex = attributeIndex;
			}

		}
		return choosenAttributeIndex;
	}

	private double calculateGain(int choosenAttributeIndex,
			List<Instance> examples) {
		double mutualInformation = 0.0;
		double size = examples.size();

		Map<String, List<Instance>> sets = divideSetsOnAttributes(examples,
				choosenAttributeIndex);

		for (String parameter : attributeListMap.get(attributeIndex
				.get(choosenAttributeIndex))) {
			double specificAttributeValueExampleSize = 0;
			if (sets.get(parameter) != null) {

				specificAttributeValueExampleSize = sets.get(parameter).size();
				double entropySpecificAttribute = getInformation(sets
						.get(parameter));
				mutualInformation += (specificAttributeValueExampleSize / size)
						* entropySpecificAttribute;
				
			}
		}
	
		return getInformation(examples) - mutualInformation;
	}

	public List<Double> getProbabilities(Double[] distribution) {
		List<Double> listProbabilites = new LinkedList<Double>();
		double total = 0;
		for (Double value : distribution) {
			total += value.doubleValue();
		}
		for (Double value : distribution) {
			double probability = value.doubleValue()/total;
			listProbabilites.add(probability);
			
		}

		return listProbabilites;
	}

	private double calculateEntropy(Double[] distribution) {
		List<Double> probabilities = getProbabilities(distribution);
		double entropy = 0.0;
		
		for (Double data : probabilities) {
			double temp = data.doubleValue();
			if(temp!=0){
			entropy += ((double) -1) * log2(temp) * temp;}
			
			// System.out.println(entropy+"entropy");
		}
		return entropy;
	}

	private double getInformation(List<Instance> set) {
		
		Double[] distribution = { 0.0, 0.0 };
		for (Instance example : set) {
			// System.out.println(example.label);
			if (example.label.equals("1")) {
				distribution[0]++;
			} else {
				if (example.label.equals("2")) {
					distribution[1]++;
				}
			}
		}
		
		return calculateEntropy(distribution);

	}

	private double log2(double value) {
		return Math.log(value) / Math.log(2);
	}

	private Map<String, List<Instance>> divideSetsOnAttributes(
			List<Instance> examples, int attributeIndex) {
		double midpoint = 0.0;
		if(isQualitative(attributeIndex)){
			midpoint = getMidpoint(attributeIndex, examples);
		}
		
		Map<String, List<Instance>> finalSets = new HashMap<String, List<Instance>>();
		for (Instance example : examples) {
			String attributeValue = example.attributes.get(attributeIndex);
			if(isQualitative(attributeIndex)){
				attributeValue = discritize(midpoint,attributeValue);
			}

			if (!finalSets.containsKey(attributeValue)) {
				List<Instance> newSet = new LinkedList<Instance>();
				newSet.add(example);

				finalSets.put(attributeValue, newSet);

			} else {
				List<Instance> set = finalSets.get(attributeValue);
				set.add(example);
				finalSets.put(attributeValue, set);
			}
		}

		return finalSets;
	}

	private String discritize(double midpoint, String attributeValue) {
		int value = Integer.parseInt(attributeValue);
		if (value <= midpoint) {
			return "A";
		}
		if (value > midpoint) {
			return "B";
		}
		return null;
	}

	private boolean isQualitative(int attributeIndex) {
		
		return attributeIndex == 4||attributeIndex ==5;
	}

	private boolean areSameClass(List<Instance> examples) {
		boolean areSameClass = true;
		String label = examples.get(0).label;
		for (Instance instance : examples) {
			
				
			
				if (!label.equals(instance.label)) {
					areSameClass = false;
					break;
				}
			

		}
		return areSameClass;
	}

	private List<Integer> getInitialAttributes() {
		List<Integer> attributes = new LinkedList<Integer>();

		for (int i = 0; i < 7; i++) {
			attributes.add(new Integer(i));
		}

		return attributes;
	}

	

	
	/**
	 * Build a decision tree given a training set then prune it using a tuning
	 * set.
	 * 
	 * @param train
	 *            the training set
	 * @param tune
	 *            the tuning set
	 */
	DecisionTreeImpl(DataSet train, DataSet tune) {
		this(train);
		DecTreeNodeImpl originalTree = root;
		DecTreeNodeImpl maxNodeToPrune = null;
		List<Instance> tuneSetExamples = tune.instances;
		double accuracy = calAccuracy(originalTree, tuneSetExamples);
		double prunedAccuracy = 0.0;
		double previousIterAccuracy = 0.0;

		while (previousIterAccuracy != accuracy) {
			previousIterAccuracy = accuracy;
			accuracy = calAccuracy(originalTree, tuneSetExamples);
			Stack<DecTreeNodeImpl> bottomUpNodes = null;
			if (!originalTree.isLeaf()) {
				bottomUpNodes = getBottomUpNodes(originalTree);
			}

			while (bottomUpNodes != null && bottomUpNodes.size() > 0) {

				DecTreeNodeImpl nodeToPrune = bottomUpNodes.pop();

				DecTreeNodeImpl prunedNode = getPruned(nodeToPrune);

				prunedAccuracy = calAccuracy(originalTree, tuneSetExamples);

				if (prunedAccuracy >= accuracy) {
					maxNodeToPrune = nodeToPrune;
					accuracy = prunedAccuracy;
				}
				getBackOriginalTree(nodeToPrune, prunedNode);

			}
			prunedAccuracy = calAccuracyRootPruned(tuneSetExamples);
			if (prunedAccuracy >= accuracy) {
				pruneRoot();
				accuracy = prunedAccuracy;
			} else {
				getPruned(maxNodeToPrune);
			}
		}

	}

	private void pruneRoot() {
		root.setNodeLeaf(true);
		root.setLabel(majorityValue(root.getExamples()));

	}

	private double calAccuracyRootPruned(List<Instance> tuneSetExamples) {
		String defLabel = majorityValue(root.getExamples());

		int countCorrect = 0;
		int total = tuneSetExamples.size();

		for (int i = 0; i < tuneSetExamples.size(); i++) {
			if (tuneSetExamples.get(i).label.equals(defLabel)) {
				countCorrect++;
			}
		}

		double accuracy = (double) countCorrect / (double) total;
		return accuracy;

	}

	private void getBackOriginalTree(DecTreeNodeImpl nodeToPrune,
			DecTreeNodeImpl prunedNode) {
		DecTreeNodeImpl parentNode = nodeToPrune.getParent();

		List<DecTreeNode> children = parentNode.getChildren();
		children.set(children.indexOf(prunedNode), nodeToPrune);

	}

	private DecTreeNodeImpl getPruned(DecTreeNodeImpl nodeToPrune) {

		DecTreeNodeImpl parentNode = nodeToPrune.getParent();
		DecTreeNodeImpl prunedNode = new DecTreeNodeImpl();

		List<DecTreeNode> children = parentNode.getChildren();

		prunedNode.setNodeLeaf(true);
		// System.out.println(nodeToPrune.parentAttributeValue+" "+"parent");
		prunedNode.setAttribute(nodeToPrune.getAttribute());
		prunedNode.setExamples(nodeToPrune.getExamples());
		prunedNode.setNodeID(nodeToPrune.getNodeID());
		prunedNode.setParent((DecTreeNodeImpl) nodeToPrune.getParent());
		prunedNode.setParentAttributeValue(nodeToPrune.parentAttributeValue);

		String defLabel = majorityValue(nodeToPrune.getExamples());
		prunedNode.setLabel(defLabel);
		// System.out.println(prunedNode.parentAttributeValue+" "+"afterPrune");
		children.set(children.indexOf(nodeToPrune), prunedNode);

		return prunedNode;
	}

	private Stack<DecTreeNodeImpl> getBottomUpNodes(DecTreeNodeImpl originalTree) {

		Stack<DecTreeNodeImpl> bottomUpNodes = new Stack<DecTreeNodeImpl>();
		Queue<DecTreeNode> currentLevelQueue = new LinkedList<DecTreeNode>();
		Queue<DecTreeNode> previousLevelQueue = new LinkedList<DecTreeNode>();

		DecTreeNodeImpl currentNode = originalTree;
		
		addInternalChildren(previousLevelQueue, currentNode.getChildren());
		for (DecTreeNode child : previousLevelQueue) {
			bottomUpNodes.add((DecTreeNodeImpl) child);
		}
		while (previousLevelQueue != null && previousLevelQueue.size() != 0) {
			while (previousLevelQueue.size() != 0) {
				addInternalChildren(currentLevelQueue,
						((DecTreeNodeImpl) previousLevelQueue.poll())
								.getChildren());
			}

			for (DecTreeNode child : currentLevelQueue) {
				bottomUpNodes.add((DecTreeNodeImpl) child);
			}
			while (currentLevelQueue.size() != 0) {
				previousLevelQueue.add(currentLevelQueue.poll());
			}
		}

		return bottomUpNodes;
	}

	private void addInternalChildren(Queue<DecTreeNode> internalNodes,
			List<DecTreeNode> children) {

		for (int i = 0; i < children.size(); i++) {
			DecTreeNodeImpl child = (DecTreeNodeImpl) children.get(i);
			if (!child.isLeaf()) {
				internalNodes.add(child);
			}
		}
	}

	private double calAccuracy(DecTreeNodeImpl node,
			List<Instance> tuneSetExamples) {
		DecTreeNodeImpl tempNode = root;
		root = node;
		String[] classification = _classify(tuneSetExamples);

		int countCorrect = 0;
		int total = tuneSetExamples.size();

		for (int i = 0; i < tuneSetExamples.size(); i++) {
			if (tuneSetExamples.get(i).label.equals(classification[i])) {
				countCorrect++;
			}
		}

		double accuracy = (double) countCorrect / (double) total;
		root = tempNode;

		return accuracy;
	}

	@Override
	/**
	 * Evaluates the learned decision tree on a test set.
	 * @return the label predictions for each test instance 
	 * 	according to the order in data set list
	 */
	public String[] classify(DataSet test) {

		List<Instance> testData = test.instances;
		String[] classifications = _classify(testData);
		
		
		return classifications;
	}

	private String[] _classify(List<Instance> testData) {
		String[] classifications = new String[testData.size()];

		for (int i = 0; i < testData.size(); i++) {
			Instance data = testData.get(i);
			List<String> attributes = data.attributes;

			DecTreeNodeImpl node = root;

			while (!node.isLeaf()) {
				int index = reverseAttributeIndex.get(node.getAttribute());
			
				node = getNextNode(node, index, attributes);

			}
			classifications[i] = node.getLabel();

		}

		return classifications;
	}

	private DecTreeNodeImpl getNextNode(DecTreeNodeImpl node, int index,
			List<String> attributes) {
		List<DecTreeNode> children = node.getChildren();
		String attributeValue = attributes.get(index);
		double midPoint = 0.0;
		if(isQualitative(index)){
		midPoint = getMidpoint(index, node.getExamples());
		attributeValue = discritize(midPoint, attributeValue);}
		

		for (DecTreeNode child : children) {

			if (child.parentAttributeValue.equals(attributeValue)) {
				
				return (DecTreeNodeImpl) child;
			}
		}
		return null;

	}

	@Override
	/**
	 * Prints the tree in specified format. It is recommended, but not
	 * necessary, that you use the print method of DecTreeNode.
	 * 
	 * Example:
	 * Root {Existing checking account?}
	 *   A11 (2)
	 *   A12 {Foreign worker?}
	 *     A71 {Credit Amount?}
	 *       A (1)
	 *       B (2)
	 *     A72 (1)
	 *   A13 (1)
	 *   A14 (1)
	 *         
	 */
	public void print() {
		root.print(0);
	}

}
