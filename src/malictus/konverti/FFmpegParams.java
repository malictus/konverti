package malictus.konverti;

import java.util.ArrayList;

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
	private boolean useExperimental = false;
	private int cutoff = -1;
	private String videoEncodingCodec = null;
	private int constantRateFactor = -1;
	private String tuning = null;
	private String preset = null;
	
	public FFmpegParams() {}
	
	/**
	 * This returns the parameters portion of the FFmpeg call, but not including the input file.
	 * @return the parameters string array
	 */
	public ArrayList<String> getCommand() {
		ArrayList<String> commands = new ArrayList<String>();
		//dont show banner every time (always do this)
		commands.add("-hide_banner");
		//dont show lots of text (always do this)
		commands.add("-v");
		commands.add("warning");
		if (audioOnly) {
			commands.add("-vn");
		}
		if (useExperimental) {
			commands.add("-strict");
			commands.add("-2");
		}
		if (audioSampleRate > -1) {
			commands.add("-ar");
			commands.add("" + audioSampleRate);
		}
		if (audioChannels > -1) {
			commands.add("-ac");
			commands.add("" + audioChannels);
		}
		if (audioEncodingCodec != null) {
			commands.add("-acodec");
			commands.add(audioEncodingCodec);
		}
		if (audioBitRate != null) {
			commands.add("-b:a");
			commands.add(audioBitRate);
		}
		if (audioQuality > -1) {
			commands.add("-q:a");
			commands.add("" + audioQuality);
		}
		if (cutoff > -1) {
			commands.add("-cutoff");
			commands.add("" + cutoff);
		}
		if (videoEncodingCodec != null) {
			commands.add("-vcodec");
			commands.add(videoEncodingCodec);
		}
		if (constantRateFactor > -1) {
			commands.add("-crf");
			commands.add("" + constantRateFactor);
		}
		if (preset != null) {
			commands.add("-preset");
			commands.add("" + preset);
		}
		if (tuning != null) {
			commands.add("-tune");
			commands.add("" + tuning);
		}
		return commands;
	}
	
	/**
	 * Toggle whether to send audio-only parameter to FFmpeg. Default is false.
	 * @param audioOnly true to send parameter, false to leave blank
	 */
	public void setAudioOnly(boolean audioOnly) {
		this.audioOnly = audioOnly;
	}
	
	/**
	 * Toggle whether to send use experimental parameter to FFmpeg. Default is false.
	 * @param useExperimental true to send parameter, false to leave blank
	 */
	public void setUseExperimental(boolean useExperimental) {
		this.useExperimental = useExperimental;
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
	 * Set the cutoff.
	 * The default is -1, which means don't set this parameter at all.
	 * @param cutoff the new cutoff value 
	 */
	public void setCutoff(int cutoff) {
		this.cutoff = cutoff;
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
	
	/**
	 * Set the video codec for output.  Default is null, which means don't set this parameter at all.
	 * This parameter should be set everytime, if there are video streams present.
	 * @param videoCodec the codec to use
	 */
	public void setVideoEncodingCodec(String videoEncodingCodec) {
		this.videoEncodingCodec = videoEncodingCodec;
	}
	
	/**
	 * Set the CRF (constant rate factor) for output. Default is -1, which means don't set this parameter at all.
	 * @param crf the constant rate factor to set. 
	 */
	public void setConstantRateFactor(int constantRateFactor) {
		this.constantRateFactor = constantRateFactor;
	}
	
	/**
	 * Set the preset for output. Default is null, which means don't set this parameter at all.
	 * @param preset the preset to use
	 */
	public void setPreset(String preset) {
		this.preset = preset;
	}
	
	/**
	 * Set the tuning parameter for output. Default is null, which means don't set this parameter at all.
	 * @param tuning the tuning to use
	 */
	public void setTuning(String tuning) {
		this.tuning = tuning;
	}

}
