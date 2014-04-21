package malictus.konverti;
import java.io.File;
import javax.swing.*;

//contains the main class for starting up konverti
public class KonvertiMain {
	
	//for now, ffmpeg executables are static and locked to 64-bit windows
	private static final String FFMPEG_PATH = "ffmpeg/win_64/bin/ffmpeg.exe";
	private static final String FFPROBE_PATH = "ffmpeg/win_64/bin/ffprobe.exe";
	public static File FFMPEG_FILE;
	public static File FFPROBE_FILE;

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
		
	}

}
