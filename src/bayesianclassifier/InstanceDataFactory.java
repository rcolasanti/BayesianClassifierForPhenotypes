package bayesianclassifier;

public class InstanceDataFactory {
	public static InstanceData getInstanceType(String type){
		if(type=="csv"){
			return new CsvInstance();
		}
		return null;
	}
}
