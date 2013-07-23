package bayesianclassifier;

public class ClassifierFactory {
	public static Classifier getInstanceType(String type){
		if(type=="bayesian"){
			return new BayesianClassifer();
		}
		return null;
	}
}
