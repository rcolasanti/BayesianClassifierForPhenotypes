package bayesianclassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Classifier {
	protected boolean toPrint =false;
	protected double fractionalFolds = (double)1/3;
	protected double splitFraction = 0.5;

	
	protected HashMap<String, Instance> instances;
	protected ArrayList<String> intToClass;
	protected ArrayList<String> training;
	protected ArrayList<String> testing;
	protected ArrayList<String> teaching;
	protected ArrayList<String> validation;
	protected HashMap<String,Integer> classes;
	protected int numberOfClasses;
	protected ClassifierPerformanceAnalysis performence;
	public Classifier(){
		
	}
	public abstract void initData(
			HashMap<String, Instance> instances,
			ArrayList<String> intToClass,
			ArrayList<String> training,
			ArrayList<String> testing,
			HashMap<String,Integer> classes
	);

	protected void splitTrainingData(){
		Random rand = new Random();
		int sizeOfList = this.training.size();
		ArrayList<Integer>randomList = new ArrayList<Integer>();
		for(int i=0; i<sizeOfList; i++){
			randomList.add(i);
		}
		int numberOfTest = (int)(sizeOfList*this.splitFraction );
		for(int i=0; i<numberOfTest; i++){
			int j = rand.nextInt(randomList.size());
			int choice = randomList.remove(j);
			String instance = this.training.get(choice);
			this.teaching.add(instance);
		}
		while (randomList.size()>0){
			int choice = randomList.remove(0);
			String instance = training.get(choice);
			this.validation.add(instance);			
		}
	}
	
	public abstract double createClassifier();
	
	public abstract void experimentClassifier(int outRep);
	
	public abstract boolean saveClassifier(String filename);
	
	public double getSplitFraction() {
		return splitFraction;
	}

	public void setSplitFraction(double splitFraction) {
		this.splitFraction = splitFraction;
	}

	public ClassifierPerformanceAnalysis getPerformence() {
		return performence;
	}
	
	public void setToPrint(boolean toPrint) {
		this.toPrint = toPrint;
	}

	public double getFolds() {
		return fractionalFolds;
	}

	public void setFolds(double folds) {
		this.fractionalFolds = folds;
	}

}
