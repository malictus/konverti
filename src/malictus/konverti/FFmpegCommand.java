package malictus.konverti;

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
	 * @return the full command string for FFmpeg
	 */
	public String getCommand() {
		String command = "\"" + KonvertiMain.FFMPEG_BIN_FOLDER + "ffmpeg\" ";
		//input file parameter
		command = command + "-i \"" + inputFile + "\" ";
		command = command + params.getCommand();
		//output file - should always be last
		command = command + "\"" + outputFile + "\"";
		return command;
	}

}
