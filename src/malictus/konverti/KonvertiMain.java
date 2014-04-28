package malictus.konverti;

import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.prefs.*;
import malictus.konverti.ui.*;

//contains the main class for initializing and starting up konverti
public class KonvertiMain {
	
	/*
	 * TODO: First, fix console util to have three method instead of one, using existing file prefs
	 * later, create new opening window
	 * 		1. Include a way to change ffmpeg loc
	 * 		2. Include credits somehow
	 * 		3. Include window and drag/drop
	 * 		4. Make drop/drag work with folders too, with popup for recursion if needed
	 * 		5. file info list shows metadat and can be sorted
	 * 		6. individually selected items can be opened with ffplay or default app, and shows more info
	 * 		7. convert button works automatically and uses presets and basic settings
	 * 		8. advanced button brings up more advanced dialog
	 */
	
	//file location for ffmpeg, ffprobe, and ffplay, not including trailing slahs
	//if this is blank, they can be called directly from command line 
	public static String FFMPEG_BIN_FOLDER = "";
	//current version
	public static float VERSION = 0.01f;
	//preferences 
	private static Preferences prefs;
	//name of preferences node to use for all prefs
	private static final String PREFS_NAME = "Konverti_Preferences";
	//holds saved folder location for FFmpeg binary files
	private static final String PREFS_FFMPEG_FOLDER_LOC = "Konverti_ffMPEG_Folder";
	//default value for no pref value present
	private static final String PREFS_NOVALUE = "~Nope~";

	public static void main(String[] args) {
		//set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Error setting look and feel.");
		}
		if (!initFFmpeg()) {
			//couldn't find libraries ---- exit
			JOptionPane.showMessageDialog(null, "Fatal error. FFmpeg executables are missing or invalid", "FFmpeg missing", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		new MainFrame();
	}
	
	//attempt to find the FFmpeg libraries --- return false if they can't be found
	private static boolean initFFmpeg() {
		//first, see if all binaries will just work on command-line automatically
		if (testFFmpeg()) {
			return true;
		}
		//see if an existing preference links to ffmpeg file location
		prefs = Preferences.userRoot().node(PREFS_NAME);
		if (!prefs.get(PREFS_FFMPEG_FOLDER_LOC, PREFS_NOVALUE).equals(PREFS_NOVALUE)) {
			//preference file exists
			String prefLoc = prefs.get(PREFS_FFMPEG_FOLDER_LOC, PREFS_NOVALUE);
			File prefFile = new File(prefLoc);
			if (prefFile.exists()) {
				if (testFFmpeg(prefLoc)) {
					//preference file exists, and works!
					FFMPEG_BIN_FOLDER = prefLoc;
					return true;
				}
			}
		}
		//query user to find FFmpeg folder
		JOptionPane.showMessageDialog(null, "Whoa there! First, we need to locate the FFmpeg folder\n"
				+ "on your machine. Please select the folder that contains the FFmpeg executables.\n They are "
				+ "probably in a folder named 'bin' inside the FFmpeg directory.\n"
				+ "You should only have to do this once.\n Go to http://ffmpeg.org if you need to download these files."
				, "Find FFmpeg", JOptionPane.WARNING_MESSAGE);
		JFileChooser fc_findlibs = new JFileChooser();
		fc_findlibs.setMultiSelectionEnabled(false);
		fc_findlibs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fc_findlibs.showOpenDialog(null);
		if (response == JFileChooser.CANCEL_OPTION) {
			return false;
		}
		File x = fc_findlibs.getSelectedFile();
		if (!x.exists() || !x.isDirectory()) {
			return false;
		}
		if (testFFmpeg(x.getAbsolutePath())) {
			FFMPEG_BIN_FOLDER = x.getAbsolutePath();
			//set preference for next time
			prefs.put(PREFS_FFMPEG_FOLDER_LOC, x.getAbsolutePath());
			return true;
		}
		//giving up!
		return false;
	}
	
	private static boolean testFFmpeg() {
		return testFFmpeg("");
	}
	
	private static boolean testFFmpeg(String filePrefix) {
		try {
			Runtime rt = Runtime.getRuntime();
			if (!filePrefix.equals("")) {
				filePrefix = filePrefix + File.separator;
			}
			//we don't even need to check these ourselves -- they will throw exceptions if they don't work
			rt.exec(filePrefix + "ffprobe -version");
			rt.exec(filePrefix + "ffplay -version");
			rt.exec(filePrefix + "ffmpeg -version"); 
		} catch (IOException err) {
			return false;
		}
		return true;
	}
	
}
