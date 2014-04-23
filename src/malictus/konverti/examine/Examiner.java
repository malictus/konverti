package malictus.konverti.examine;

import java.io.*;

//an examiner is a class that can examine a file using a specific library, and extract information about it
public interface Examiner {
	
	//whether or not the file is valid and can be opened using this library
	public boolean isValid();
	
	//return a human readable string of information about a file's format
	public String getInfo();
	
	//return the file in question
	public File getFile();

}
