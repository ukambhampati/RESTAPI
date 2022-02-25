package functions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class ReadPropertyFile {

	
	FileInputStream inputFile;	
	
	public Properties initPropertyFile(String path,Properties prop) throws IOException {
		
		inputFile = new FileInputStream(path);
	
		prop.load(inputFile);
		return prop;
				
	}
	
	

}
