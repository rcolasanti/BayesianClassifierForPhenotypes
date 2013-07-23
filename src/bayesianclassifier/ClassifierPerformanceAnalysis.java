package bayesianclassifier;

import java.util.ArrayList;

public class ClassifierPerformanceAnalysis {
	private int matrixSize;
	/* Confusion matrix 
	 * http://en.wikipedia.org/wiki/Confusion_matrix
	 * Tabular representation of the performance of the classifier
	 * against a test set of data
	*/
	private int[][] confusionMatrix;
	private int totalCorrect =0;
	private int total =0;
	private int[] sumPredicted;
	private int[] sumActual;
	private int[] truePositives;
	private int[] trueNegative;
	private int[] falsePositive;
	private int[] falseNegative;
	private double[] sensitvity;
	private double[] specificity;
	private double[] precision;
	private double[] fMeasure;
	private double[] balancedAccuracy;
	private double errorRate;
	private double accuracy;
	private ArrayList<String> listOfClasses;
	
	public ClassifierPerformanceAnalysis(int matrixSize,ArrayList<String> listOfClasses){
		this.matrixSize = matrixSize;
		this.listOfClasses =listOfClasses;
		this.confusionMatrix = new int[this.matrixSize][this.matrixSize];
		for(int i=0; i<this.matrixSize;i++){
			for(int j=0; j< this.matrixSize;j++){
				this.confusionMatrix[i][j]=0;
			}
		}
		this.sumPredicted= new int[this.matrixSize];
		this.sumActual= new int[this.matrixSize];
		this.truePositives= new int[this.matrixSize];
		this.trueNegative= new int[this.matrixSize];
		this.falsePositive= new int[this.matrixSize];
		this.falseNegative= new int[this.matrixSize];
		this.sensitvity = new double[this.matrixSize];
		this.specificity= new double[this.matrixSize];
		this.precision= new double[this.matrixSize];
		this.fMeasure= new double[this.matrixSize];
		this.balancedAccuracy= new double[this.matrixSize];
	}

	private void calculateTypeStats(int type){
		this.truePositives[type]=this.confusionMatrix[type][type];
		for(int a =0; a< this.matrixSize; a++){
			this.sumActual[type]+=this.confusionMatrix[a][type];
		}
		for(int p =0; p< this.matrixSize; p++){
			this.sumPredicted[type]+=this.confusionMatrix[type][p];
		}
		this.falseNegative[type]= this.sumActual[type]-this.truePositives[type];
		this.falsePositive[type]= this.sumPredicted[type]-this.truePositives[type];
		this.trueNegative[type]= this.total-this.truePositives[type]-this.falseNegative[type]-this.falsePositive[type];
		this.sensitvity[type] = (double)this.truePositives[type]/this.sumPredicted[type];
		int sumNegative = this.total-this.sumPredicted[type];
		this.specificity[type] = (double)this.trueNegative[type]/sumNegative;
		this.precision[type]=(double)this.truePositives[type]/this.sumActual[type];
		this.fMeasure[type]= 2.0*((double)this.truePositives[type]/(this.sumActual[type]+this.sumPredicted[type]));
		this.balancedAccuracy[type]=(this.sensitvity[type]+this.specificity[type])/2.0;
	}
	
	private void calculateMatrixStats(){
		// [a = actual] [p= predicted]
		for(int a=0; a<matrixSize; a++){
			/* The cross diagonal (i==j) of the confusion matrix  
			*  Contains all of the true predictions
			*/
			this.totalCorrect+=this.confusionMatrix[a][a];
			for(int p=0; p<matrixSize; p++){
				this.total +=this.confusionMatrix[a][p];
			}
			falseNegative[a]-=truePositives[a];
		}
		int totalIncorrect = total-totalCorrect;
		this.errorRate = (double)totalIncorrect /total;
		this.accuracy = (double)(totalCorrect)/total;			
	}
	
	public int getMatrixSize() {
		return matrixSize;
	}

	public int[][] getConfusionMatrix() {
		return confusionMatrix;
	}

	public int getTotalCorrect() {
		return totalCorrect;
	}

	public int getTotal() {
		return total;
	}

	public int[] getSumPredicted() {
		return sumPredicted;
	}

	public int[] getSumActual() {
		return sumActual;
	}

	public int[] getTruePositives() {
		return truePositives;
	}

	public int[] getTrueNegative() {
		return trueNegative;
	}

	public int[] getFalsePositive() {
		return falsePositive;
	}

	public int[] getFalseNegative() {
		return falseNegative;
	}

	public double[] getSensitvity() {
		return sensitvity;
	}

	public double[] getRecall() {
		return sensitvity;
	}

	public double[] getSpecificity() {
		return specificity;
	}

	public double[] getPrecision() {
		return precision;
	}

	public double[] getfMeasure() {
		return fMeasure;
	}

	public double[] getBalancedAccuracy() {
		return balancedAccuracy;
	}

	public double getErrorRate() {
		return errorRate;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public int getElement(int x, int y){
		return this.confusionMatrix[x][y];
	}
	
	public void calculatePerformence(){
		this.calculateMatrixStats();
		for(int i=0; i<this.matrixSize; i++){
			this.calculateTypeStats(i);
		}
	}
	
	public void incrementMatrixValue(int x, int y){
		if((x<this.matrixSize)&&(y<this.matrixSize)&&(x>=0)&&(y>=0)){
			this.confusionMatrix[x][y]++;
		}
	}
	
	public void setMatrixValue(int x, int y, int val){
		if((x<this.matrixSize)&&(y<this.matrixSize)&&(x>=0)&&(y>=0)){
			this.confusionMatrix[x][y]=val;
		}
	}	

	public void printConfusionMatrix(String splitter){
		System.out.println("Confusion Matrix:");
		System.out.print("\t");
		for(int p=0; p<matrixSize; p++){
			System.out.print("\t"+this.listOfClasses.get(p));
		}
		System.out.print(splitter);
		for(int a=0; a<matrixSize; a++){
			System.out.print("\t"+this.listOfClasses.get(a));
			for(int p=0; p<matrixSize; p++){
				System.out.print("\t"+this.confusionMatrix[a][p]);
			}
			System.out.print(splitter);
		}
		System.out.print(splitter);
	}
	
	private void printStatistic(String name,double[] stat, String splitter){
		System.out.print(name);
		for(int i=0; i<stat.length; i++){
			String s = String.format(this.listOfClasses.get(i)+":%.2f",stat[i]);
			System.out.print("\t"+s);
		}
		System.out.print(splitter);
	}
	
	public String getMetrics(){
		String output ="Confusion Matrix \t";
		for(int a=0; a<matrixSize; a++){
			output+="\t"+this.listOfClasses.get(a);
			for(int p=0; p<matrixSize; p++){
				output+="\t"+this.confusionMatrix[a][p];
			}
		}
		String s = String.format("%.2f",this.getErrorRate());
		output+="\t Error \t"+s;
		s = String.format("%.2f",this.getAccuracy());
		output+="\t Accuracy \t"+s;
		output+="\t Precision";
		for(int a=0; a<matrixSize; a++){
			output+="\t"+this.getPrecision()[a];
		}
		output+="\t Specificity";
		for(int a=0; a<matrixSize; a++){
			output+="\t"+this.getSpecificity()[a];
		}
		output+="\t fMeasure";
		for(int a=0; a<matrixSize; a++){
			output+="\t"+this.getfMeasure()[a];
		}
		output+="\t Balanced accuracy";
		for(int a=0; a<matrixSize; a++){
			output+="\t"+this.getBalancedAccuracy()[a];
		}
		return output;
	}
	
	
	public void printPerformanceStatistics(String splitter){
		String s = String.format("%.2f",this.getErrorRate());
		System.out.print("Error          \t"+s+splitter);
		s = String.format("%.2f",this.getAccuracy());
		System.out.print("Accuracy       \t"+s+splitter);
		printStatistic("Recall           ",this.getRecall(),splitter);
		printStatistic("Precision        ",this.getPrecision(),splitter);
		printStatistic("Specificity      ",this.getSpecificity(),splitter);
		printStatistic("fMeasure         ",this.getfMeasure(),splitter);
		printStatistic("Balanced accuracy",this.getBalancedAccuracy(),splitter);
	}
}
