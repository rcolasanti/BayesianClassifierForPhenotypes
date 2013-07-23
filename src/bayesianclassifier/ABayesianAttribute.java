package bayesianclassifier;


public class ABayesianAttribute {
	private static int numberOfClasses;
	private static double minimumProbability = 0.000001;
	private String name;
	private int[] classCount; 
	private int total;
	
	public ABayesianAttribute(String name){
		this.name = name;
		this.total=0;
		classCount =  new int[ABayesianAttribute.getNumberOfClasses()];
	}

	public String getName() {
		return name;
	}
	
	public int getCount(int index) {
		return this.classCount[index];
	}
	
	public void incrementCount(int index){
		this.classCount[index]++;
		this.total++;
	}

	public String getProbabilitiesToString(){
		String s="";
		for(int i=0; i<this.classCount.length; i++){
			s = s+"\t"+this.getProbability(i);
		}
		return s;
	}
	
	public double getProbability(int index) {
		if(this.classCount[index]<1)return ABayesianAttribute.getMinimumProbability();
		return (double)(this.classCount[index]/(double)this.total);
	}

	public static int getNumberOfClasses() {
		return numberOfClasses;
	}

	public static void setNumberOfClasses(int numberOfClasses) {
		ABayesianAttribute.numberOfClasses = numberOfClasses;
	}
	public static double getMinimumProbability() {
		return minimumProbability;
	}
	public static void setMinimumProbability(double minimumProbability) {
		ABayesianAttribute.minimumProbability = minimumProbability;
	}

	public int getTotal() {
		return this.total;
	}

}
