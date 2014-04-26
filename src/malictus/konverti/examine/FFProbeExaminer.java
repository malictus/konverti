package malictus.konverti.examine;

import java.io.*;

import malictus.konverti.ConsoleException;
import malictus.konverti.KonvertiMain;
import malictus.konverti.KonvertiUtils;

public class FFProbeExaminer implements Examiner {

	private File theFile;
	private boolean isValid = false;
	private String info = "";
	private String format = "";
	private String duration = "";
	
	public FFProbeExaminer(File theFile) throws IOException, ConsoleException {
		this.theFile = theFile;
		//first, run a command to see if the file is recognized by FFPROBE
		String command = "\"" + KonvertiMain.FFPROBE_FILE.getCanonicalPath() + "\" -v quiet -show_error \"" + theFile.getPath() + "\"";
		String validCheck = KonvertiUtils.runExternalCommand(command);
		if (validCheck.trim().length() > 1) {
			isValid = false;
			return;
		} else {
			isValid = true;
		}
		//next, run a command to generate all format info for this file
		command = "\"" + KonvertiMain.FFPROBE_FILE.getCanonicalPath() + "\" -v quiet -show_format \"" + theFile.getPath() + "\"";
		info = KonvertiUtils.runExternalCommand(command);
		//pull fields out of metadata
		int startDuration = info.indexOf("duration=") + 9;
		if (startDuration < 0) {
			return;
		}
		int endDuration = info.indexOf("\n", startDuration);
		if (endDuration < 0) {
			return;
		}
		duration = info.substring(startDuration, endDuration);
		int startFormat = info.indexOf("format_long_name=") + 17;
		if (startFormat < 0) {
			return;
		}
		int endFormat = info.indexOf("\n", startFormat);
		if (endFormat < 0) {
			return;
		}
		format = info.substring(startFormat, endFormat);
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getInfo() {
		return info;
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public File getFile() {
		return theFile;
	}
	
}
