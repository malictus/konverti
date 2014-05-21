package malictus.konverti;

import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.Vector;
import java.util.prefs.*;
import malictus.konverti.ui.main.*;

/**
 * Contains the main method for Konverti and startup procedures, including testing for FFMPEG libraries.
 * @author Jim Halliday
 */
public class KonvertiMain {
	
	//TODO overall tasks
	
	/* 
	 * Add ability to choose which ffmpeg to use (finish) when button pushed, including 
	 * 	1. checking in same directory as exe first
	 *  2. checking command line next, and 
	 *  3. having user pick one 
	 *  
	 * Better handling of finding FFMPEG at startup
	 * 	1. check system pref
	 *  2. check same directory as exe
	 *  3. check command line
	 *  4. ask user whether they have it. If not, link to locations incl zeranoe.
	 *   	If they do have it, have them find it.
	 * 
	 * CODE CLEANUP
	 * 
     * FACTOR OUT PARAM STUFF
     * 
     * Flesh out custom button for wav/aiff/mp3, incl refactor as I can
	 * 
	 * Create executable for windows
	 * Update license, readme, and documentation in source
	 * Upload various versions to sourceforge
	 * Website up
	 * 
	 * Add more stuff
	 */
	
	//file folder location for ffmpeg and ffprobe, not including trailing slash
	//if this is blank, they can be called directly from command line 
	public static String FFMPEG_BIN_FOLDER = "";
	//current version
	public static float VERSION = 0.01f;
	//name of preferences node to use for all prefs
	private static final String PREFS_NAME = "Konverti_Preferences";
	//holds saved folder location for FFmpeg binary files
	private static final String PREFS_FFMPEG_FOLDER_LOC = "Konverti_ffMPEG_Folder";
	//default value for no pref value present
	private static final String PREFS_NOVALUE = "~Nope~";
	//list of all available FFmpeg decoders
	public static Vector<Encoder> encoders;
	//preferences 
	private static Preferences prefs;

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
		if (!initFFmpeg()) {
			//couldn't find libraries ---- exit
			JOptionPane.showMessageDialog(null, "Fatal error. FFmpeg executables are missing or invalid", "FFmpeg missing", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		//get the full list of supported encoders from ffmpeg
		encoders = KonvertiUtils.getEncoders();
		//bring up the window
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	/**
	 * Just initiate the GUI
	 */
	private static void createAndShowGUI() {
		new MainPanel();
    }
	
	/**
	 * attempt to find the FFmpeg libraries 
	 * @return false if libraries can't be found, true otherwise
	 */
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
