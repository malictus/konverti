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
	private Vector<ConvertFileEntry> filesList;
	private int conversion_preset;
	private JLabel lbl_status;
    
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
        JButton btn_close = new JButton("Close");
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
        JTextArea txt_cmdline = new JTextArea();
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
        btn_close.setEnabled(true);
        //TODO make stuff beyond this a thread and add cancel buton
        //TODO add file extension on when sending files to ffmpeg based on preset (unless ffmpeg does this anyway)
        populateFilesList(incomingFilesList);
        //TODO put this in after starting thread; execution will block here
        setVisible(true);
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
	
	/**
	 * Cancel the current file conversion process so the window can be closed.
	 */
	private void cancelProcessing() {
		
	}

}
