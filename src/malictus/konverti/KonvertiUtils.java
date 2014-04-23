package malictus.konverti;

import java.io.*;

//utility methods for Konverti
public class KonvertiUtils {
	
	//run a command-line command and return the output from the command as a string
	public static String runExternalCommand(String command) throws IOException, ConsoleException {
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	    String s = null;	    
	    String val = "";
	    String errval = "";
	    //first see if any errors
	    while ((s = stdError.readLine()) != null) {
            errval = errval + s + "\n";
        }
	    if (errval.length() > 0) {
	    	throw new ConsoleException(errval);
	    }
	    //now read output
	    while ((s = stdInput.readLine()) != null) {
	    	val = val + s + "\n";
	    }
	    stdInput.close();
	    stdError.close();
        return val;
	}

}
