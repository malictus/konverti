package malictus.konverti;

import java.io.*;

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
	    	System.out.println(line);
	    	val = val + line + "\n";
	    }
	    br.close();
	    isr.close();
	    is.close();
        return val;
	}

}
