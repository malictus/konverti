package malictus.konverti.ui.convert;

import java.awt.*;
import java.util.Vector;
import java.io.*;
import javax.swing.*;
import malictus.konverti.*;
import malictus.konverti.examine.*;

/**
 * This is the window that appears when the actual file conversion is taking place.
 */
public class ConversionPanel extends JFrame {
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;
	private JPanel contentPanel;
	private Vector<ConvertFileEntry> filesList;
	private int conversion_preset;
    
	/*
	 * Initialize the conversion window
	 */
	public ConversionPanel(java.util.List<FFProbeExaminer> incomingFilesList, int conversion_preset) {
		super();
		this.conversion_preset = conversion_preset;
		setTitle("Konverti " + KonvertiMain.VERSION + " -- File Conversion in Process");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPanel = new JPanel();        
        /*********************************/
        /** set up components on screen **/
        /*********************************/
        contentPanel.setLayout(new BorderLayout());
        
        //finalize
        contentPanel.setOpaque(true); 
        setContentPane(contentPanel);
        setResizable(true);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));;
        setSize(WIDTH, HEIGHT);
        //center on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
        	frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
        	frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
        //TODO make stuff beyond this a thread
        //TODO also make this a DIALOG so users can't go back to previous window and do anything while waiting!
        //TODO add file extension on when sending files to ffmpeg based on preset (unless ffmpeg does this anyway)
        populateFilesList(incomingFilesList);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Interate through the incoming files, and attempt to find appropriate names for new files to use for conversion
	 * @param incomingFilesList the incoming files from FFProbe
	 */
	private void populateFilesList(java.util.List<FFProbeExaminer> incomingFilesList) {
		int counter = 0;
		while (counter < incomingFilesList.size()) {
			try {
				File inFile = incomingFilesList.get(counter).getFile();
				ConvertFileEntry cfe = new ConvertFileEntry(inFile);
				filesList.add(cfe);
			} catch (Exception err) {
				//something weird happened, but no need to abort, just keep going
			}
			counter++;
		}
	}

}
