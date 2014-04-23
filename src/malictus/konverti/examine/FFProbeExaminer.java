package malictus.konverti.examine;

import java.io.*;

import malictus.konverti.ConsoleException;
import malictus.konverti.KonvertiMain;
import malictus.konverti.KonvertiUtils;

public class FFProbeExaminer implements Examiner {

	private File theFile;
	private boolean isValid = false;
	private String info = "";
	
	public FFProbeExaminer(File theFile) throws IOException, ConsoleException {
		this.theFile = theFile;
		//first, run a command to see if the file is recognized by FFPROBE
		String command = "\"" + KonvertiMain.FFPROBE_FILE.getCanonicalPath() + "\" -v quiet -show_error " + theFile.getPath();
		String validCheck = KonvertiUtils.runExternalCommand(command);
		if (validCheck.trim().length() > 1) {
			isValid = false;
			return;
		} else {
			isValid = true;
		}
		//next, run a command to generate all format info for this file
		command = "\"" + KonvertiMain.FFPROBE_FILE.getCanonicalPath() + "\" -v quiet -show_format " + theFile.getPath();
		info = KonvertiUtils.runExternalCommand(command);
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getInfo() {
		return info;
	}
	
	public File getFile() {
		return theFile;
	}
	
}
