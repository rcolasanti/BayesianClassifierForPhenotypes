package bayesianclassifier;

import java.util.ArrayList;

public class BayesianClassifer extends Classifier {
	public static double differance = 0;
	private TheBaysianClassifier bayesianClassifier;

	public BayesianClassifer(){
	}
	

	private int largest(double[] doubleArray) {
		double max = -Double.MAX_VALUE;
		double min = Double.MAX_VALUE;
		int maxElement = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			if (doubleArray[i] > max) {
				maxElement = i;
				max = doubleArray[i];
			}
			if (doubleArray[i] < min) {
				min = doubleArray[i];
			}
		}
		if (BayesianClassifer.differance < Math.abs(max - min)) {
			return maxElement;
		}
		return -1;
	}

	private void teachBayesianClassifier() {
		for (String s : this.teaching) {
			Instance instance = this.instances.get(s);
			String type = instance.getClassType();
			int typeInt = this.classes.get(type);
			
			if (this.toPrint) {
				System.out.print("\n Number of attributes: "
						+ this.bayesianClassifier.getAtributesSize());
			}
			instance.setPointer();
			String attributeID;
			while ((attributeID = instance.nextAttribute()) != null) {
				this.bayesianClassifier.addAtribute(attributeID, typeInt);
			}
		}
		if (this.toPrint) {
			System.out.print("\n");
		}
	}

	private int predictor(Instance instance, TheBaysianClassifier classifier) {
		double[] logSum = new double[this.numberOfClasses];
		String attributeID;
		while ((attributeID = instance.nextAttribute()) != null) {
			if (classifier.containsAtribute(attributeID)) {
				for (int i = 0; i < this.numberOfClasses; i++) {
					double prob = classifier.getProbability(attributeID, i);
					logSum[i] += Math.log(prob);
				}
			}
		}
		for (int i = 0; i < this.numberOfClasses; i++) {
			logSum[i] += Math.log(classifier.getFractionalOccurance(i));
		}
		int guessID = this.largest(logSum);
		return guessID;
	}

	@SuppressWarnings("unused")
	private void rocTest(TheBaysianClassifier classifier,
			ArrayList<String> testSet) {
		for (int i = 0; i < 5000; i += 100) {
			BayesianClassifer.differance = i;
			this.performence = new ClassifierPerformanceAnalysis(
					this.numberOfClasses, this.intToClass);
			for (String s : testSet) {
				Instance instance = this.instances.get(s);
				String type = instance.getClassType();
				int typeID = this.classes.get(type);
				int guessID = this.predictor(instance, classifier);
				if (guessID > -1) {
					this.performence.incrementMatrixValue(typeID, guessID);
				}
			}
			this.performence.calculatePerformence();
			System.out.print(i + ","
					+ this.performence.getBalancedAccuracy()[0]);
			System.out.print("," + this.performence.getElement(0, 0));
			System.out.print("," + this.performence.getElement(1, 1));
			System.out.print("," + this.performence.getElement(0, 1));
			System.out.println("," + this.performence.getElement(1, 0));
		}
	}

	private double testBayesianClassifier(TheBaysianClassifier classifier,
			ArrayList<String> testSet) {
		this.performence = new ClassifierPerformanceAnalysis(
				this.numberOfClasses, this.intToClass);
		for (String s : testSet) {
			Instance instance = this.instances.get(s);
			String type = instance.getClassType();
			int typeID = this.classes.get(type);
			int guessID = this.predictor(instance, classifier);
			if (guessID > -1) {
				this.performence.incrementMatrixValue(typeID, guessID);
			}
		}
		this.performence.calculatePerformence();
		if (this.toPrint) {
			System.out.println();
			this.performence.printConfusionMatrix("\n");
			this.performence.printPerformanceStatistics("\n");
		}
		return this.performence.getBalancedAccuracy()[0];
	}

	private void dumpBehaviour(TheBaysianClassifier classifier,
			ArrayList<String> testSet, String marker) {
		for (String s : testSet) {
			Instance instance = this.instances.get(s);
			String type = instance.getClassType();
			int typeID = this.classes.get(type);
			int guessID = this.predictor(instance, classifier);
			if (typeID != guessID) {
				System.out.println(marker + s + "  "
						+ this.intToClass.get(typeID) + " "
						+ this.intToClass.get(guessID));
			}
		}
	}

	@Override
	public double createClassifier() {
		TheBaysianClassifier bestClassifier = null;
		double max = 0;
		double metric = 0.0;
		int folds = (int) (this.training.size() * this.splitFraction * this.fractionalFolds);
		if (folds <= 0)
			folds = 1;
		if (this.toPrint)
			System.out.println("\nNumber of folds " + folds);
		for (int i = 0; i < folds; i++) {
			if (this.toPrint) {
				System.out.print("Trial " + i);
			}
			this.teaching = new ArrayList<String>();
			this.validation = new ArrayList<String>();
			this.splitTrainingData();
			this.bayesianClassifier = new TheBaysianClassifier(this.intToClass);
			this.teachBayesianClassifier();
			metric = this.testBayesianClassifier(this.bayesianClassifier,
					this.validation);
			if (this.toPrint) {
				System.out.print(String.format(" Balanced accuracy:%.4f    ",
						metric));
				System.out.print("\r");
			}
			if (metric >= max) {
				bestClassifier = this.bayesianClassifier;
				max = metric;
			}
		}
		metric = this.testBayesianClassifier(bestClassifier, this.testing);

		// this.rocTest(bestClassifier,this.testing);

		if (this.toPrint) {
			System.out.println(metric);
			System.out.println(this.testBayesianClassifier(bestClassifier,
					this.validation));
			this.dumpBehaviour(bestClassifier, training, "");
			this.dumpBehaviour(bestClassifier, testing, "*");
		}
		return metric;
	}

	@Override
	public boolean saveClassifier(String fileName) {
		if (this.bayesianClassifier != null) {
			if (this.bayesianClassifier.saveClassifier(fileName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void experimentClassifier(int outRep) {
		int folds = (int) (this.training.size() * this.splitFraction * this.fractionalFolds);
		if (folds <= 0)folds = 1;
		TheBaysianClassifier bestClassifier = null;
		double max = 0;
		double metric = 0.0;
		for (int i = 0; i < folds; i++) {
			this.teaching = new ArrayList<String>();
			this.validation = new ArrayList<String>();
			this.splitTrainingData();
			this.bayesianClassifier = new TheBaysianClassifier(this.intToClass);
			this.teachBayesianClassifier();
			metric = this.testBayesianClassifier(this.bayesianClassifier,
					this.validation);
			if (metric >= max) {
				bestClassifier = this.bayesianClassifier;
				max = metric;
			}
		}
		metric = this.testBayesianClassifier(bestClassifier, this.testing);
		System.out.println(this.getPerformence().getMetrics());
	}

	@Override
	public void initData(java.util.HashMap<String, Instance> instances,
			java.util.ArrayList<String> intToClass,
			java.util.ArrayList<String> training,
			java.util.ArrayList<String> testing,
			java.util.HashMap<String, Integer> classes) {
		// TODO Auto-generated method stub
			this.instances = instances;
			this.intToClass = intToClass;
			this.training = training;
			this.testing =testing;
			this.classes = classes;
			this.numberOfClasses = this.classes.size();
			ABayesianAttribute.setNumberOfClasses(numberOfClasses);
	}

}
