package bayesianclassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


class IntegerData {
	int data;
	public IntegerData() {
		this.data = 1;
	}
}

class Instance {
	private String id;
	private String classType;
	private ArrayList<String> atributes;
	private int pointer;

	public Instance(String id, String classType) {
		this.id = id;
		this.classType = classType;
		this.atributes = new ArrayList<String>();
		this.pointer = 0;
	}

	public String getId() {
		return id;
	}

	public String getClassType() {
		return classType;
	}

	public void setPointer() {
		this.pointer = 0;
	}

	public void addAttribute(String attribute) {
		this.atributes.add(attribute);
	}

	public String nextAttribute() {
		if (this.pointer >= this.atributes.size()) {
			this.pointer = 0;
			return null;
		}
		String result = this.atributes.get(this.pointer);
		this.pointer++;
		return result;
	}

}


public class ClassifierConstructor {
	private boolean toPrint = false;
	private HashMap<String, Instance> instances;
	private HashMap<String, IntegerData> classTypes;
	private HashMap<String,Integer> classes;
	private ArrayList<String> intToClass;
	private ArrayList<String> training;
	private ArrayList<String> testing;
	private HashMap<String,ArrayList<String>> sortedInstances;
	private double testingFraction = (1.0/3.0);
	private Classifier classifier;

	public ClassifierConstructor() {
		this.instances = new HashMap<String, Instance>();
		this.classTypes = new HashMap<String, IntegerData>();
		this.classes = new HashMap<String,Integer>();
		this.sortedInstances = new HashMap<String,ArrayList<String>>();
		this.intToClass = new ArrayList<String>();
	}

	private boolean addInstance(String id, String classType, String[] attributes) {
		if (this.instances.containsKey(id)) {
			return false;
		}
		Instance newInstance = new Instance(id, classType);
		for (String s : attributes) {
			newInstance.addAttribute(s);
		}
		this.instances.put(id, newInstance);
		return true;
	}

	private void splitEachClassData(ArrayList<String> list){
		Random rand = new Random();
		int sizeOfList = list.size();
		ArrayList<Integer>randomList = new ArrayList<Integer>(list.size());
		for(int i=0; i<sizeOfList; i++){
			randomList.add(i);
		}
		int numberOfTest = (int)(sizeOfList*this.testingFraction);
		for(int i=0; i<numberOfTest; i++){
			int j = rand.nextInt(randomList.size());
			int choice = randomList.remove(j);
			String instance = list.get(choice);
			this.testing.add(instance);
		}
		while (randomList.size()>0){
			int choice = randomList.remove(0);
			String instance = list.get(choice);
			this.training.add(instance);			
		}
	}
	
	private void splitData(){
		this.training =  new ArrayList<String>();
		this.testing = new ArrayList<String>();
		for (Entry<String, ArrayList<String>> entry : this.sortedInstances.entrySet()) {
		    this.splitEachClassData(entry.getValue());
		}
	}
	 
	private void sortInastances(String instance, String classType){
		if(this.sortedInstances.containsKey(classType)==false){
			this.sortedInstances.put(classType,new ArrayList<String>());
		}
		this.sortedInstances.get(classType).add(instance);
		if(this.toPrint)this.printClassLists();
	}
	
	private void printClassLists(){
		System.out.print(" Class type count ");
		for (Entry<String, ArrayList<String>> entry : this.sortedInstances.entrySet()) {
			System.out.print(entry.getKey() +"("+ entry.getValue().size()+")   ");
		}
		System.out.print("\r");
	}

	private void parseData(InstanceData instance) {
		while(instance.readInstance()){
			if (this.classes.containsKey(instance.getInstanceClass())) { // is the class in the chosen classes
				this.addInstance(instance.getInstanceID(),instance.getInstanceClass(),instance.getInstanceAtributes());
				this.sortInastances(instance.getInstanceID(),instance.getInstanceClass());
			}
		}
	}

	private void chooseClassTypes(String classTypes) {
		String delims = ",";
		String[] tokens = classTypes.split(delims);
		for (int i=0; i<tokens.length; i++) {
			this.classes.put(tokens[i], i);
			this.intToClass.add(tokens[i]);
		}
	}
	
	private void scanClassSet(InstanceData instance) {
		while(instance.readInstance()){
			if (this.classTypes.containsKey(instance.getInstanceClass())) {
				IntegerData i = this.classTypes.get(instance.getInstanceClass());
				i.data++;
			} else {
				this.classTypes.put(instance.getInstanceClass(), new IntegerData());
			}
		}
		this.listClasses();
	}

	private void listClasses() {
		for (Entry<String, IntegerData> entry : this.classTypes.entrySet()) {
			String attributeID = entry.getKey();
			IntegerData i = entry.getValue();
			System.out.print(" '" + attributeID + "':" + i.data);
		}
		System.out.print("]\r");
	}

	public void setToPrint(boolean toPrint) {
		this.toPrint = toPrint;
	}


	public boolean experiment(String fileName,InstanceData data,Classifier classifier, String types, int reps) {
		if(data.openFile(fileName)){
			for(int i=0; i<reps; i++){
				this.chooseClassTypes(types);
				this.parseData(data);
				this.splitData();
				this.classifier = classifier;
				this.classifier.initData(this.instances,this.intToClass,this.training,this.testing,this.classes);
				this.classifier.experimentClassifier(i);
			}
			return true;
		}
		return false;
	}
	

	public boolean createClassifier(String dataFile,InstanceData data,Classifier classifier, String classifierFile, String types) {
		if(data.openFile(dataFile)){
			if(this.toPrint)System.out.println(" Class type identifiers: "+types);
			this.chooseClassTypes(types);
			this.parseData(data);
			this.splitData();
			if(this.toPrint)System.out.println("\n Testing size:"+this.testing.size()+"  Training size:"+this.training.size());
			this.classifier = classifier;
			this.classifier.initData(this.instances,this.intToClass,this.training,this.testing,this.classes);
			this.classifier.createClassifier();
			this.classifier.getPerformence().printConfusionMatrix("\n");
			this.classifier.getPerformence().printPerformanceStatistics("\n");
			this.classifier.saveClassifier(classifierFile);
			return true;		
		}
		return false;
	}

	public boolean constructTestClassifier(String fileName,InstanceData data,Classifier classifier,String types){
		if(data.openFile(fileName)){
			if(this.toPrint)System.out.println(" Class type identifiers: "+types);
			this.chooseClassTypes(types);
			this.parseData(data);
			this.splitData();
			if(this.toPrint)System.out.println("\n Testing size:"+this.testing.size()+"  Training size:"+this.training.size());
			this.classifier = classifier;
			this.classifier.initData(this.instances,this.intToClass,this.training,this.testing,this.classes);
			this.classifier.setToPrint(this.toPrint);
			this.classifier.createClassifier();
			this.classifier.getPerformence().printConfusionMatrix("\n");
			this.classifier.getPerformence().printPerformanceStatistics("\n");
			return true;		
		}
		return false;
	}

	public boolean scanClasses(String fileName, String fileType) {
		InstanceData data = InstanceDataFactory.getInstanceType(fileType);
		if(data.openFile(fileName)){
			this.scanClassSet(data);
			if(this.toPrint)System.out.println();
			return true;
		}
		return false;
	}
	public double getTestingFraction() {
		return testingFraction;
	}

	public void setTestingFraction(double testingFraction) {
		this.testingFraction = testingFraction;
	}

}
