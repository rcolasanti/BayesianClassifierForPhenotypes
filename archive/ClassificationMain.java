package bayesianclassifier;
import org.apache.commons.cli.*; 
public class ClassificationMain {

	/**
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean toPrint = false;
		Options options = new Options();
		Option versionOption = new Option( "v", "verbose", false, "Prints out all of the metrics during the calculations." );
		Option helpOption = new Option( "h", "help", false, "Help." );
		Option scanOption = new Option("s", true, "scan for class types [file] file name");
			
		OptionBuilder.hasArgs();
		OptionBuilder.isRequired(false);
		OptionBuilder.withDescription("Create a test classifier [File] filename  [Clastypes] (comma separated)");
		Option testOption =OptionBuilder.create("t");
		
		OptionBuilder.hasArgs(3);
		OptionBuilder.isRequired(false);
		OptionBuilder.withDescription("Creates and saves a classifier  [inFile] file name [Class types] (comma separated) [outFile] filename");
		Option createOption =OptionBuilder.create("c");
		
		OptionBuilder.hasArgs();
		OptionBuilder.isRequired(false);
		OptionBuilder.withDescription("experiment");
		Option experimentOption =OptionBuilder.create("e");
		
		options.addOption(helpOption);
		options.addOption(scanOption);
		options.addOption(versionOption);
		options.addOption(testOption);
		options.addOption(experimentOption);
		options.addOption(createOption);

		CommandLineParser parser = new BasicParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException pe) {
			usage(options);
			return;
		}

		
		if (cmd.hasOption("v")) {
			toPrint = true;
		}
		if(cmd.hasOption("s")) {
			String fileName = cmd.getOptionValue('s');
			ClassifierConstructor classifier = new ClassifierConstructor();
			classifier.scanClasses(fileName,"csv");
			System.out.println();
		} 
		
		if (cmd.hasOption("c")) {
			String[] functionArgs = cmd.getOptionValues('c');
			if(functionArgs.length==3){
				ClassifierConstructor classifier = new ClassifierConstructor();
				classifier.setToPrint(toPrint);
				if(classifier.createClassifier(functionArgs[0],InstanceDataFactory.getInstanceType("csv"),functionArgs[1],functionArgs[2])==false){
					usage(options);
					return;					
				}
			}else{
				usage(options);
				return;
			}
		}
		if (cmd.hasOption("t")) {
			String[] functionArgs = cmd.getOptionValues('t');
			if(functionArgs.length==2){
				ClassifierConstructor classifier = new ClassifierConstructor();
				classifier.setToPrint(toPrint);
				if(classifier.constructTestClassifier(functionArgs[0],InstanceDataFactory.getInstanceType("csv"),functionArgs[1])==false){
					usage(options);
					return;					
				}
			}else{
				usage(options);
				return;
			}
		}
		if (cmd.hasOption("e")) {
			String[] functionArgs = cmd.getOptionValues('e');
			System.out.println(functionArgs.length);
			if(functionArgs.length==3){
				ClassifierConstructor classifier = new ClassifierConstructor();
				classifier.setToPrint(toPrint);
				classifier.experiment(functionArgs[0],InstanceDataFactory.getInstanceType("csv"),functionArgs[1],Integer.parseInt(functionArgs[2]));
			}else{
				usage(options);
				return;
			}
		}
		if(cmd.hasOption("h")){
			usage(options);
			return;
		}
		if(cmd.hasOption("")){
				usage(options);
				return;
		}

	}

	private static void usage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("CLIDemo", options,true);
	}
}