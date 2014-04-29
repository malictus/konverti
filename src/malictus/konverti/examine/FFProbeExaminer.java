package malictus.konverti.examine;

import java.io.*;
import malictus.konverti.ConsoleException;
import malictus.konverti.KonvertiUtils;

public class FFProbeExaminer {

	private File theFile;
	private boolean isValid = false;
	//this is the entire format chunk from ffprobe
	private String format_info = "";
	//this is the entire raw stream chunk from ffprobe
	private String stream_info = "";
	//the overall file format (from the format chunk)
	private String format = "";
	//the overall duration in seconds (from the format chunk)
	private String duration = "";
	//TODO: add stream data type, and add array of them here
	
	/**
	 * This class is responsible for querying FFProbe to get information about a file
	 */
	public FFProbeExaminer(File theFile) throws IOException, ConsoleException {
		this.theFile = theFile;
		//first, check to see if the file is recognized by FFPROBE
		String command = "-v quiet -show_error";
		String validCheck = KonvertiUtils.runFFProbeCommand(command, theFile);
		if (validCheck.trim().length() > 1) {
			isValid = false;
			return;
		} else {
			isValid = true;
		}
		//next, run a command to generate basic format information for this file
		command = "-v quiet -show_format";
		format_info = KonvertiUtils.runFFProbeCommand(command, theFile);
		//TODO: generate stream info and populate arrays
		//TODO: a better way of pulling info out of these strings with a single method
		//pull fields out of metadata
		int startDuration = format_info.indexOf("duration=") + 9;
		if (startDuration < 0) {
			return;
		}
		int endDuration = format_info.indexOf("\n", startDuration);
		if (endDuration < 0) {
			return;
		}
		duration = format_info.substring(startDuration, endDuration);
		int startFormat = format_info.indexOf("format_long_name=") + 17;
		if (startFormat < 0) {
			return;
		}
		int endFormat = format_info.indexOf("\n", startFormat);
		if (endFormat < 0) {
			return;
		}
		format = format_info.substring(startFormat, endFormat);
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getFormatInfo() {
		return format_info;
	}
	
	public String getStreamInfo() {
		return stream_info;
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getDuration() {
		return duration;
	}
	
	//TODO: add methods for getting stream data out
	
	public File getFile() {
		return theFile;
	}
	
}
