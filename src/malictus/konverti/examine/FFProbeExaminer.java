package malictus.konverti.examine;

import java.io.*;
import java.util.Hashtable;
import malictus.konverti.ConsoleException;
import malictus.konverti.KonvertiUtils;

/**
 * This class represents a single file that has been examine by FFprobe, and it encapsulates the data from that command.
 * @author Jim Halliday
 */
public class FFProbeExaminer implements Comparable<FFProbeExaminer> {

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
	//the stream data objects for this file
	private Stream[] streams;
	
	/**
	 * Initiate on FFProbeExaminer object
	 * @param theFile the file to examine with FFprobe
	 * @throws IOException if file error occurs
	 * @throws ConsoleException if console error occurs
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
		Hashtable<String, String> format_vals = KonvertiUtils.getHashtableFor(format_info);
		//pull fields out of metadata
		duration = format_vals.get("duration");
		format = format_vals.get("format_long_name");
		//now generate stream information
		command = "-v quiet -show_streams";
		stream_info = KonvertiUtils.runFFProbeCommand(command, theFile);
		String[] raw_streams = stream_info.split("\\[\\/STREAM\\]");
		if (raw_streams.length > 1) {
			//last one will be empty
			streams = new Stream[raw_streams.length-1];
			int count = 0;
			while (count < (raw_streams.length - 1)) {
				Stream currentStream = new Stream();
				streams[count] = currentStream;
				Hashtable<String, String> stream_vals = KonvertiUtils.getHashtableFor(raw_streams[count]);
				if (stream_vals.get("codec_type") != null) {
					streams[count].codec_type = stream_vals.get("codec_type");
				}
				if (stream_vals.get("codec_long_name") != null) {
					streams[count].codec_long_name = stream_vals.get("codec_long_name");
				}
				if (stream_vals.get("channels") != null) {
					streams[count].channels = stream_vals.get("channels");
				}
				if (stream_vals.get("width") != null) {
					streams[count].width = stream_vals.get("width");
				}
				if (stream_vals.get("height") != null) {
					streams[count].height = stream_vals.get("height");
				}
				if (stream_vals.get("sample_rate") != null) {
					streams[count].sample_rate = stream_vals.get("sample_rate");
				}
				count++;
			}
		}
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
	
	public File getFile() {
		return theFile;
	}
	
	public Stream[] getStreams() {
		return streams;
	}
	
	/**
	 * An override of the compareTo method to sort files based on file name in the files list 
	 */
	@Override 
	public int compareTo(FFProbeExaminer other_probe) {
		return this.getFile().getName().compareTo(other_probe.getFile().getName());
	}

}
