package malictus.konverti;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

import java.util.List;
import java.util.Vector;
import java.util.prefs.*;

import malictus.konverti.examine.FFProbeExaminer;
import malictus.konverti.ui.main.*;

/**
 * Contains the main method for Konverti and startup procedures, including testing for FFMPEG libraries.
 * @author Jim Halliday
 */
public class KonvertiMain {
	
	//TODO overall tasks
	
	/* 
	 * ADD A NEW PREFERENCE FOR CURRENT LOCATION THAT FILES GO (AND WHETHER TO SELECT IT OR NOT)
	 * 
	 * Create executable for windows
	 * Update readme, and documentation in source
	 * Upload various versions to sourceforge
	 * Website up
	 */
	
	/**
	 * How to add new formats
	 * 
	 * 1. Add appropriate values in PickerFormat combo box and doNext() method.
	 * 2. Add appropriate windows to fill in values.
	 */
	
	/**
	 * How to create new presets
	 * 
	 * 1. Add a new PRESET_... constant to ConversionPanel
	 * 2. Add appropriate conversion parameters to ConversionPanel.addConversionParams()
	 * 3. Add appropriate extension to ConversionPanel.getExtension()
	 * 4. Add logic to MainPanel.populateComboBox()
	 */
	
	//file folder location (plus trailing file separator) for ffmpeg and ffprobe, not including trailing slash
	//if this String is blank, they can be called directly from command line 
	public static String FFMPEG_BIN_FOLDER = "";
	//current version
	public static float VERSION = 0.03f;
	//name of preferences node to use for all prefs
	private static final String PREFS_NAME = "Konverti_Preferences";
	//holds saved folder location for FFmpeg binary files
	private static final String PREFS_FFMPEG_FOLDER_LOC = "Konverti_ffMPEG_Folder";
	//default value for no pref value present
	private static final String PREFS_NOVALUE = "~Nope~";
	//list of all available FFmpeg encoders
	public static Vector<Encoder> encoders;
	//preferences 
	private static Preferences prefs;
	//the vector of files that are to be processed
	public static List<FFProbeExaminer> vec_files = new Vector<FFProbeExaminer>();

	/**
	 * Main method for program
	 * @param args not used
	 */
	public static void main(String[] args) {
		//set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Error setting look and feel.");
		}
		//local FFmpeg libraries
		if (!initFFmpeg()) {
			//couldn't find libraries ---- exit
			System.exit(1);
		}
		//get the full list of supported encoders from ffmpeg
		encoders = KonvertiUtils.getEncoders();
		//bring up the main window
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                KonvertiMain.createAndShowGUI();
            }
        });
	}
	
	/**
	 * Attempt to reset the FFmpeg directory, called at any time once the program has already started
	 * @return true if this results in actually changing the existing value
	 */
	public static boolean setFFmpegdir() {
		if (testFFmpeg()) {
			//raw command-line works, so ask user if they want to use that
			int reply = JOptionPane.showConfirmDialog(null, "Your computer is configured properly to run FFmpeg "
					+ "from the command line.\nWould you like to use that version of FFmpeg for Konverti?", "Use command"
					+ " line version?", JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	        	FFMPEG_BIN_FOLDER = "";
				//set preference for next time
				prefs.put(PREFS_FFMPEG_FOLDER_LOC, "");
				return true;
	        }	
		}
		return KonvertiMain.letUserPick();
	}
	
	/**
	 * Let the user pick a folder that contains FFmpeg. 
	 * This will update the preferences file as well. Will also display error dialog if the wrong folder is selected.
	 * @return if a folder was successfully chosen
	 */
	private static boolean letUserPick() {
		//let user pick new location
        JFileChooser fc_findlibs = new JFileChooser();
		fc_findlibs.setMultiSelectionEnabled(false);
		fc_findlibs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fc_findlibs.showOpenDialog(null);
		if (response == JFileChooser.CANCEL_OPTION) {
			return false;
		}
		File x = fc_findlibs.getSelectedFile();
		if (x.exists() && x.isDirectory()) {
			if (testFFmpeg(x.getAbsolutePath())) {
				FFMPEG_BIN_FOLDER = x.getAbsolutePath() + File.separator;
				//set preference for next time
				prefs.put(PREFS_FFMPEG_FOLDER_LOC, x.getAbsolutePath());
				return true;
			}
		}
		//user may have tried to select FFmpeg folder directly, try accessing 'bin' folder inside it
		File xx = new File(x.getAbsolutePath() + File.separator + "bin");
		if (xx.exists() && xx.isDirectory()) {
			if (testFFmpeg(xx.getAbsolutePath())) {
				FFMPEG_BIN_FOLDER = xx.getAbsolutePath() + File.separator;
				//set preference for next time
				prefs.put(PREFS_FFMPEG_FOLDER_LOC, xx.getAbsolutePath());
				return true;
			}
		}
		//giving up!
		JOptionPane.showMessageDialog(null, "FFmpeg libraries could not be located or are invalid", "Can't fine FFmpeg", JOptionPane.ERROR_MESSAGE);
		return false;
	}
	
	/**
	 * Just initiate the GUI
	 */
	private static void createAndShowGUI() {
		new MainPanel();
    }
	
	/**
	 * Attempt to find the FFmpeg libraries, called when the program is first starting
	 * @return false if libraries can't be found, true otherwise
	 */
	private static boolean initFFmpeg() {
		//see if an existing preference links to valid ffmpeg file location
		prefs = Preferences.userRoot().node(PREFS_NAME);
		if (!prefs.get(PREFS_FFMPEG_FOLDER_LOC, PREFS_NOVALUE).equals(PREFS_NOVALUE)) {
			//preference file exists
			String prefLoc = prefs.get(PREFS_FFMPEG_FOLDER_LOC, PREFS_NOVALUE);
			if (prefLoc.equals("")) {
				if (testFFmpeg()) {
					return true;
				}
			} else {
				File prefFile = new File(prefLoc);
				if (prefFile.exists()) {
					if (testFFmpeg(prefLoc)) {
						//preference file exists, and works!
						FFMPEG_BIN_FOLDER = prefLoc + File.separator;
						return true;
					}
				}
			}
		}
		//no valid pref file exists; so try some default locations 
		//first, try the raw command line version
		if (testFFmpeg()) {
			prefs.put(PREFS_FFMPEG_FOLDER_LOC, "");
			return true;
		}
		//can't find it automatically; user will have to find it for us
		JOptionPane.showMessageDialog(null, "Please select the folder that contains the FFmpeg executables.\n"
				+ "Go to http://ffmpeg.org if you need to download these files."
				+ "\nFor Windows users try http://ffmpeg.zeranoe.com/builds/ for precompiled builds.", "Find FFmpeg", JOptionPane.WARNING_MESSAGE);
		return KonvertiMain.letUserPick();
	}
	
	/**
	 * Test FFmpeg libraries against the command-line with no folder parameter
	 * @return true if the ffmpeg and ffprobe executable commands all pass
	 */
	private static boolean testFFmpeg() {
		return testFFmpeg("");
	}
	
	/**
	 * Test for presence of FFmpeg in a given folder location.
	 * @param filePrefix File location of ffmpeg libraries. If empty string is passed, commands will be run directly on the command line.
	 * @return true if the ffmpeg and ffprobe executable commands pass
	 */
	private static boolean testFFmpeg(String filePrefix) {
		try {
			Runtime rt = Runtime.getRuntime();
			if (!filePrefix.equals("")) {
				filePrefix = filePrefix + File.separator;
			}
			//we don't even need to check these ourselves -- they will throw exceptions if they don't work
			rt.exec(filePrefix + "ffprobe -version");
			rt.exec(filePrefix + "ffmpeg -version"); 
		} catch (IOException err) {
			return false;
		}
		return true;
	}
	
}
