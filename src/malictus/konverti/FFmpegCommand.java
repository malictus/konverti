package malictus.konverti;

import java.util.ArrayList;

/**
 * This class represents a single command-line call to FFmpeg. Basically, you can set various parameters, then call getString() to get out the
 * full command to pass to FFmpeg.
 * @author Jim Halliday
 */
public class FFmpegCommand {
	
	private String inputFile;
	private String outputFile;
	private FFmpegParams params;
	
	/**
	 * Initiate the FFmpeg command. Note that the input and output files are not verfied at this stage. 
	 * @param inputFile a string that contains the full path to the input file
	 * @param outputFile a string that contains the full path to the output file
	 * @param the params class that represents all the command-line options
	 */
	public FFmpegCommand(String inputFile, String outputFile, FFmpegParams params) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.params = params;
	}
	
	/**
	 * Generate the full text for the FFmpeg command to use, based on the current state of this object
	 * @return the full command string array for FFmpeg
	 */
	public ArrayList<String> getCommand() {
		ArrayList<String> commands = new ArrayList<String>();
		//first add actual ffmpeg call
		commands.add(KonvertiMain.FFMPEG_BIN_FOLDER + "ffmpeg");
		//input file parameter
		commands.add("-i");
		commands.add(inputFile);
		commands.addAll(params.getCommand());
		//output file - should always be last
		commands.add(outputFile);
		return commands;
	}

}
