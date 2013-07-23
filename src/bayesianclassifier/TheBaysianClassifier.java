package bayesianclassifier;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import bayesianclassifier.ABayesianAttribute;

public class TheBaysianClassifier {
	private HashMap<String, ABayesianAttribute> aBayesianAttributes;
	private ArrayList<String> listOfClasses;
	private int[] countClasses;
	private int total;
	private int minCount=1;
	private double weight =0.0000001;
	private double assumedProbability = 0.5;
	
	public TheBaysianClassifier(ArrayList<String> listOfClasses){
		this.listOfClasses = listOfClasses;
		this.aBayesianAttributes = new java.util.HashMap<String, ABayesianAttribute>();
		this.countClasses = new int[ABayesianAttribute.getNumberOfClasses()];
		for(int i=0; i<this.countClasses.length;i++){
			this.countClasses[i]=0;
		}
		this.total=0;
	}
	
	public void addAtribute(String attributeName, int type){
		ABayesianAttribute aBayesianAttribute;
		if(aBayesianAttributes.containsKey(attributeName)){
			aBayesianAttribute = aBayesianAttributes.get(attributeName);
		}else{
			aBayesianAttribute = new ABayesianAttribute(attributeName);
			aBayesianAttributes.put(attributeName, aBayesianAttribute);
		}
		aBayesianAttribute.incrementCount(type);
		this.countClasses[type]++;
		this.total++;
	}
	
	public boolean containsAtribute(String attributeName){
		if(aBayesianAttributes.containsKey(attributeName)){
			return true;
		}
		return false;
	}
		
	public double getProbability(String attributeName,int type){
		ABayesianAttribute attribute = aBayesianAttributes.get(attributeName);
		double basicProb = (double)attribute.getCount(type)/this.countClasses[type];
		int totals = attribute.getTotal();
		double adjustedProb = ((this.weight*this.assumedProbability)+(totals*basicProb))/(weight+totals);
		return adjustedProb;
	}

	public double getFractionalOccurance(int type){
		return (double)this.countClasses[type]/this.total;
	}
	
	public int getAtributesSize(){
		return this.aBayesianAttributes.size();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	
	public boolean saveClassifier(String fileName){
		try {
			File outFile = new File(fileName);
			FileWriter fileWriter = new FileWriter(outFile);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write("Classifier\tBaysian\n"); // identify classifier type
			
			//write class identifiers
			writer.write("Classes");
			for(String s: this.listOfClasses){
				writer.write("\t"+s);
			}
			writer.write("\n");
			
			writer.write("FractionalOccurance");
			for(int i=0; i<this.listOfClasses.size();i++){
				writer.write("\t"+this.getFractionalOccurance(i));
			}
			writer.write("\n");
			for (Entry<String, ABayesianAttribute> entry : this.aBayesianAttributes.entrySet()) {
				String attributeID = entry.getKey();
				ABayesianAttribute attribute = entry.getValue();
				writer.write(attributeID);
				writer.write(attribute.getProbabilitiesToString());
				writer.write("\n");
			}			
			
			writer.close();
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
