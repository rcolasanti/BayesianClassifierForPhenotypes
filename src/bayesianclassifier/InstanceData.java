package bayesianclassifier;

public interface InstanceData {
	public boolean openFile(String fileName);
	public boolean closeFile();
	public boolean readInstance();
	public String[] getInstanceAtributes();
	public String getInstanceClass();
	public String getInstanceID();
}
