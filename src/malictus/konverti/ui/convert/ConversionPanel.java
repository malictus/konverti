package malictus.konverti.ui.convert;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.io.*;
import javax.swing.*;
import malictus.konverti.*;
import malictus.konverti.examine.*;

/**
 * This is the window that appears when the actual file conversion is taking place.
 */
public class ConversionPanel extends JDialog {
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 400;
	private JPanel contentPanel;
	private int conversion_preset;
	private JLabel lbl_status;
	private JButton btn_close;
	private JTextArea txt_cmdline;
    
	/*
	 * Initialize the conversion window
	 */
	public ConversionPanel(java.util.List<FFProbeExaminer> incomingFilesList, int conversion_preset) {
		super();
		this.conversion_preset = conversion_preset;
		setTitle("Konverti " + KonvertiMain.VERSION + " -- File Conversion");
		this.setModal(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPanel = new JPanel();        
        /*********************************/
        /** set up components on screen **/
        /*********************************/
        contentPanel.setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        btn_close = new JButton("Close");
        btn_close.setEnabled(false);
        btn_close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        }); 
        JButton btn_stop = new JButton("Stop");
        btn_stop.setEnabled(false);
        btn_stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelProcessing();
            }
        }); 
        southPanel.add(btn_stop);
        southPanel.add(btn_close);
        contentPanel.add(southPanel, BorderLayout.SOUTH);
        txt_cmdline = new JTextArea();
        txt_cmdline.setEditable(false);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
        txt_cmdline.setFont(font);
        JScrollPane scrollPane = new JScrollPane(txt_cmdline); 
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        lbl_status = new JLabel("Status: Starting...");
        northPanel.add(lbl_status);
        contentPanel.add(northPanel, BorderLayout.NORTH);
        
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
        final java.util.List<FFProbeExaminer> incomingFilesListFinal = incomingFilesList;
        Runnable q = new Runnable() {
			public void run() {
				doConvert(incomingFilesListFinal);
		    }
		};
		Thread t = new Thread(q);
		t.start();
        setVisible(true);
	}
	
	/**
	 * Handle the actual file conversion process here, in a separate thread
	 */
	private void doConvert(java.util.List<FFProbeExaminer> incomingFilesList) {
		Vector<ConvertFileEntry> filesList = populateFilesList(incomingFilesList);
		int counter = 0;
		while (counter < filesList.size()) {
			lbl_status.setText("Status: Converting File " + (counter+1) + " of " + (filesList.size()));
			File inFile = filesList.get(counter).getInFile();
			this.txt_cmdline.append("Processing " + inFile.getAbsolutePath() + "\n");
			//TODO actual processing
			counter++;
		}
		//finished --- OK to close now
		btn_close.setEnabled(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Interate through the incoming files, and attempt to find appropriate names for new files to use for conversion
	 * @param incomingFilesList the incoming files from FFProbe
	 */
	private Vector<ConvertFileEntry> populateFilesList(java.util.List<FFProbeExaminer> incomingFilesList) {
		Vector<ConvertFileEntry> vec_cfe = new Vector<ConvertFileEntry>();
		int counter = 0;
		while (counter < incomingFilesList.size()) {
			try {
				File inFile = incomingFilesList.get(counter).getFile();
				ConvertFileEntry cfe = new ConvertFileEntry(inFile);
				vec_cfe.add(cfe);
			} catch (Exception err) {
				//something weird happened, but no need to abort, just keep going
			}
			counter++;
		}
		return vec_cfe;
	}
	
	/**
	 * Cancel the current file conversion process so the window can be closed.
	 */
	private void cancelProcessing() {
		
	}

}
