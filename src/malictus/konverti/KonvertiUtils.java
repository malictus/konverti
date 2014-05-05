package malictus.konverti;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Utility method for Konverti.
 * @author Jim Halliday
 */
public class KonvertiUtils {
	
	/**
	 * Run FFProbe command without a file parameter (only command line options, such as version, can be used in this way)
	 * @param command command-line options to pass to ffprobe
	 * @return the text output from running the command
	 * @throws IOException if file error occurs
	 * @throws ConsoleException if problem occurs with the command line call
	 */
	public static String runFFProbeCommand(String command) throws IOException, ConsoleException {
		return KonvertiUtils.runFFProbeCommand(command, null);
	}
	
	/**
	 * Run FFProbe command without a file parameter (only command line options, such as version, can be used in this way)
	 * @param command command-line options to pass to ffprobe
	 * @param fileToProcess the file to be processed by FFprobe; or null for command to be run without a file parameter
	 * @return the text output from running the command
	 * @throws IOException if file error occurs
	 * @throws ConsoleException if problem occurs with the command line call
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
	
	/**
	 * Parse a string, and look for any lines that are in the form 'xxx=yyy' and parse these into key value pairs.
	 * This mimics the way ffprobe outputs information.
	 * @param stringToParse the string to parse for values
	 * @return a hashtable of string pairs
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
	
	/**
	 * Convert a string that represents a duration in seconds in a nicely formatted hh:mm:ss format
	 * @param duration the string that represents the number of seconds
	 * @return the formatted time string
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
	
	/**
	 * Count the number of files in a array of files, recursively.
	 * @param theFiles the initial folder contents
	 * @return the number of files total in this folder and all subfolders
	 */
	public static int countFiles(File[] theFiles) {
		int val = 0;
		for( int i = 0; i < theFiles.length; i++ ) {
			File theFile = theFiles[i];
			if (theFile.isDirectory()) {
				val = val + KonvertiUtils.countFiles(theFile.listFiles());
			} else if (theFile.isFile()){
				val = val + 1;
			}
		}
		return val;
	}
	
	/**
	 * Since FFprobe returns false positives for many files that can't actually be converted by Konverti, here we filter out
	 * files by file extension so that the file list doesn't show the bogus files.
	 * @param theFileName the name of the file to check
	 * @return true if the file should be excluded from the list of files to process
	 */
	public static boolean filenameIsInBlackList(String theFileName) {
		theFileName = theFileName.trim().toLowerCase();
		if (theFileName.endsWith("exe") || theFileName.endsWith("dll") || theFileName.endsWith("txt") || theFileName.endsWith("png")
				|| theFileName.endsWith("jpg") || theFileName.endsWith("gif") || theFileName.endsWith("ico")) {
			return true;
		}
		return false;
	}

}
