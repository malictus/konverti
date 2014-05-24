package malictus.konverti;

/**
 * This class represents a set of FFmpeg parameters. Basically, these are the non-file command line options that should be passed to FFmpeg.
 * This is pulled out to a separate call so it can be used over and over with various files as input and output.
 * @author Jim Halliday
 */
public class FFmpegParams {
	
	private boolean audioOnly = false;
	private int audioSampleRate = -1;
	private int audioChannels = -1;
	private String audioEncodingCodec = null;
	private String audioBitRate = null;
	private int audioQuality = -1;
	
	public FFmpegParams() {}
	
	/**
	 * This returns the parameters portion of the FFmpeg call, but not including the input file.
	 * @return the parameters string
	 */
	public String getCommand() {
		//dont show banner every time (always do this)
		String command = " -hide_banner ";
		//dont show lots of text (always do this)
		command = command + "-v warning ";
		if (audioOnly) {
			command = command + "-vn ";
		}
		if (audioSampleRate > -1) {
			command = command + "-ar " + audioSampleRate + " ";
		}
		if (audioChannels > -1) {
			command = command + "-ac " + audioChannels + " ";
		}
		if (audioEncodingCodec != null) {
			command = command + "-acodec " + audioEncodingCodec + " ";
		}
		if (audioBitRate != null) {
			command = command + "-b:a " + audioBitRate + " ";
		}
		if (audioQuality > -1) {
			command = command + "-q:a " + audioQuality + " ";
		}
		return command;
	}
	
	/**
	 * Toggle whether to send audio-only parameter to FFmpeg. Default is false.
	 * @param audioOnly true to send parameter, false to leave blank
	 */
	public void setAudioOnly(boolean audioOnly) {
		this.audioOnly = audioOnly;
	}
	
	/**
	 * Set the audio sampling rate (22050, 44100, etc.)  Default is -1, which means don't set this parameter at all.
	 * @param audioSampleRate the sampling rate to set. 
	 */
	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}
	
	/**
	 * Set the audio bitrate (320k, 192k, etc.)  This is a string rather than an int,
	 * since it is usually specifie with the 'k' at the end.
	 * Default is null, which means don't set this parameter at all.
	 * @param audioBitRate the bitrate to set. 
	 */
	public void setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
	}
	
	/**
	 * Set the audio quality. The usage and acceptable values of this parameter varies from codec to codec.
	 * The default is -1, which means don't set this parameter at all.
	 * @param audioQuality the quality to set. 
	 */
	public void setAudioQuality(int audioQuality) {
		this.audioQuality = audioQuality;
	}
	
	/**
	 * Set the audio channels for output (usually 1 or 2).  Default is -1, which means don't set this parameter at all.
	 * @param audioChannels the sampling rate to set. 
	 */
	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}
	
	/**
	 * Set the audio codec for output.  Default is null, which means don't set this parameter at all.
	 * This parameter should be set everytime, if there are audio streams present.
	 * @param audioCodec the codec to use
	 */
	public void setAudioEncodingCodec(String audioEncodingCodec) {
		this.audioEncodingCodec = audioEncodingCodec;
	}

}
