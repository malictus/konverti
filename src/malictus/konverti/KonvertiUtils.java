package malictus.konverti;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

//utility methods for Konverti
public class KonvertiUtils {
	
	/*
	 * Run FFProbe with a file parameter (only command line options, such as version)
	 */
	public static String runFFProbeCommand(String command) throws IOException, ConsoleException {
		return KonvertiUtils.runFFProbeCommand(command, null);
	}
	
	/*
	 * Run FFProbe, passing in a series of command-line options as a string, and a file to examine
	 * File should be set to null if no file is to be specified
	 */
	public static String runFFProbeCommand(String command, File fileToProcess) throws IOException, ConsoleException {
		command = "\"" + KonvertiMain.FFMPEG_BIN_FOLDER + File.separator + "ffprobe\" " + command;
		if (fileToProcess != null) {
			command = command + " \"" + fileToProcess.getAbsolutePath() + "\"";
		}
		String val = "";
		ProcessBuilder builder = new ProcessBuilder(command);
	    final Process process = builder.start();
	    InputStream is = process.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
	    	val = val + line + "\n";
	    }
	    br.close();
	    isr.close();
	    is.close();
        return val;
	}
	
	/*
	 * Parse a string, and look for any lines that are in the form 'xxx=yyy' and parse these into key value pairs.
	 * This mimics the way ffprobe outputs information.
	 */
	public static Hashtable<String, String> getHashtableFor(String stringToParse) {
		String[] lines = stringToParse.split("\\n");
		int counter = 0;
		Hashtable<String, String> hash = new Hashtable<String, String>();
		while (counter < lines.length) {
			String line = lines[counter];
			if (line.contains("=")) {
				String[] split = line.split("=");
				if (split.length > 1) {
					if ((!split[0].trim().equals("")) && (!split[1].trim().equals(""))) {
						hash.put(split[0], split[1]);
					}
				}
			}
			counter++;
		}
		return hash;
	}
	
	/*
	 * Given a number that represents a duration in seconds, return a string representation of that number, formatted correctly
	 */
	public static String showDuration(String duration) {
		float seconds = -1f;
		try {
			seconds = new Float(duration);
		} catch (Exception err) {
			return "";
		}
		if (seconds < 0) {
			return "";
		}
		long hours = TimeUnit.SECONDS.toHours((long)seconds);
		long minutes = TimeUnit.SECONDS.toMinutes((long)seconds) - (TimeUnit.SECONDS.toHours((long)seconds)* 60);
		seconds = seconds - (TimeUnit.SECONDS.toMinutes((long)seconds) *60);
		int secondsint = (int)seconds;
		String secondsString = "" + secondsint;
		if (secondsint < 10) {
			secondsString = "0" + secondsint;
		}
		if (hours > 0) {
			String minuteString = "";
			if (minutes < 10) {
				minuteString = "0" + minutes;
			} else {
				minuteString = "" + minutes;
			}
			return hours + ":" + minuteString + ":" + secondsString;
		} else {
			return minutes + ":" + secondsString;
		}
	}

}
