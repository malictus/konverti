package malictus.konverti;

import java.io.File;

/**
 * This object encapulates everything about a FFmpeg command that needs to persist from window to window as the command is being generated.
 * @author Jim Halliday
 */
public class FFmpegStruct {
	
	//the ffmpeg command line options to be passed in
	public FFmpegParams params;
	//the target file extension
	public String extension;
	//the target folder to send converted files to; if null, files will be copied to the same folder at the original file (but renamed)
	public File convert_folder;
	
	/**
	 * Initialize a FFmpegStruct object
	 * @param params the command-line options for this FFmpeg command
	 * @param extension the target file extension
	 * @param convert_folder the folder for converted files to go, or null if converted file should go to same folder as original file
	 */
	public FFmpegStruct(FFmpegParams params, String extension, File convert_folder) {
		this.params = params;
		this.extension = extension;
		this.convert_folder = convert_folder;
	}

}
