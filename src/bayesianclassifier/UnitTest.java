package bayesianclassifier;

public class UnitTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassifierConstructor constructor = new ClassifierConstructor();
		constructor.setToPrint(false);
		Classifier classifier = ClassifierFactory.getInstanceType("bayesian");
		classifier.setToPrint(false);
		//constructor.constructTestClassifier(args[0],InstanceDataFactory.getInstanceType("csv"),classifier,args[1]);
		constructor.experiment(args[0],InstanceDataFactory.getInstanceType("csv"),classifier,args[1],Integer.parseInt(args[2]));
		classifier.saveClassifier("/home/ric/SandBox/classifier.csv");
	}

}
