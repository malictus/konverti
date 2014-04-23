package malictus.konverti;

import java.io.File;
import javax.swing.*;
import malictus.konverti.ui.*;

//contains the main class for starting up konverti
public class KonvertiMain {
	
	//for now, ffmpeg executables are static and locked to 64-bit windows
	private static final String FFMPEG_PATH = "ffmpeg/win_64/bin/ffmpeg.exe";
	private static final String FFPROBE_PATH = "ffmpeg/win_64/bin/ffprobe.exe";
	public static File FFMPEG_FILE;
	public static File FFPROBE_FILE;
	//sox executable - windows only for now
	public static final String SOX_PATH = "sox/win/sox.exe";
	public static File SOX_FILE;
	//imagemagick - 64-bit windows for now
	private static final String IM_CONVERT_PATH = "ffmpeg/win_64/bin/ffmpeg.exe";
	private static final String IM_IDENTIFY_PATH = "ffmpeg/win_64/bin/ffprobe.exe";
	public static File IM_CONVERT_FILE;
	public static File IM_IDENTIFY_FILE;
	//current version
	public static float VERSION = 0.01f;

	public static void main(String[] args) {
		//set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Error setting look and feel.");
		}
		//initialize ffmpeg
		FFMPEG_FILE = new File(FFMPEG_PATH);
		FFPROBE_FILE = new File(FFPROBE_PATH);
		if (!FFMPEG_FILE.exists() || !FFMPEG_FILE.canRead() || !FFMPEG_FILE.canExecute()) {
			JOptionPane.showMessageDialog(null, "Fatal error. FFmpeg executable is missing", "FFmpeg missing", JOptionPane.ERROR_MESSAGE);
			System.out.println(FFMPEG_FILE.getPath());
			System.exit(1);
		}
		if (!FFPROBE_FILE.exists() || !FFPROBE_FILE.canRead() || !FFPROBE_FILE.canExecute()) {
			JOptionPane.showMessageDialog(null, "Fatal error. FFprobe executable is missing", "FFprobe missing", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		//init sox
		SOX_FILE = new File(SOX_PATH);
		if (!SOX_FILE.exists() || !SOX_FILE.canRead() || !SOX_FILE.canExecute()) {
			JOptionPane.showMessageDialog(null, "Fatal error. Sox executable is missing", "Sox missing", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		//init imagemagick
		IM_CONVERT_FILE = new File(IM_CONVERT_PATH);
		if (!IM_CONVERT_FILE.exists() || !IM_CONVERT_FILE.canRead() || !IM_CONVERT_FILE.canExecute()) {
			JOptionPane.showMessageDialog(null, "Fatal error. ImageMagick convert executable is missing", "IM convert missing", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		IM_IDENTIFY_FILE = new File(IM_IDENTIFY_PATH);
		if (!IM_IDENTIFY_FILE.exists() || !IM_IDENTIFY_FILE.canRead() || !IM_IDENTIFY_FILE.canExecute()) {
			JOptionPane.showMessageDialog(null, "Fatal error. ImageMagick identify executable is missing", "IM identity missing", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		new MainFrame();
	}

}
