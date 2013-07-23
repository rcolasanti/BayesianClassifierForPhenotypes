package bayesianclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CsvInstance implements InstanceData {
	private BufferedReader reader;
	private String instanceID;
	private String instanceClass;
	private String[] instanceAtributes;

	/*	
	*/
	public CsvInstance() {
		this.reader = null;
		this.instanceID = null;
		this.instanceClass = null;
		this.instanceAtributes = null;
		;
	}

	@Override
	public boolean openFile(String fileName) {
		try {
			File inFile = new File(fileName);
			FileReader fileReader = new FileReader(inFile);
			reader = new BufferedReader(fileReader);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean closeFile() {
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public String[] getInstanceAtributes() {
		return this.instanceAtributes;
	}

	@Override
	public String getInstanceClass() {
		return this.instanceClass;
	}

	@Override
	public String getInstanceID() {
		return this.instanceID;
	}

	@Override
	public boolean readInstance() {
		String line;
		try {
			if ((line = reader.readLine()) != null) {
				String delims = "\t";
				String[] tokens = line.split(delims);
				if (tokens.length > 3) {
					this.instanceID = tokens[0];
					this.instanceClass = tokens[2];
					this.instanceAtributes = Arrays.copyOfRange(tokens, 3,tokens.length);
					return true;
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
