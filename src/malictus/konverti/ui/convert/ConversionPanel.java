package malictus.konverti.ui.convert;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.io.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

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
	private JButton btn_stop;
	private boolean cancel_signal = false;
	//presets from the preset combox box on the parent window
	public static int PRESET_CD = 1;
    
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
        btn_stop = new JButton("Stop");
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
        DefaultCaret caret = (DefaultCaret)txt_cmdline.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
		btn_stop.setEnabled(true);
		Vector<ConvertFileEntry> filesList = populateFilesList(incomingFilesList);
		int counter = 0;
		while (counter < filesList.size()) {
			lbl_status.setText("Status: Converting File " + (counter+1) + " of " + (filesList.size()));
			File inFile = filesList.get(counter).getInFile();
			this.txt_cmdline.append("\nProcessing " + inFile.getAbsolutePath() + "\n");
			try {
				runFFMpegCommand(inFile, filesList.get(counter).getOutFile());
			} catch (Exception err) {
				err.printStackTrace();
			}
			if (cancel_signal) {
				return;
			}
			counter++;
		}
		//finished --- OK to close now
		btn_close.setEnabled(true);
		btn_stop.setEnabled(false);
		lbl_status.setText("Status: Finished converting " + filesList.size() + " files.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Run FFmpeg command, based on the selected preset
	 * 
	 * @param inFile input file
	 * @param outFile output file
	 * @throws IOException if read/write errors occur
	 */
	private void runFFMpegCommand(File inFile, File outFile) throws IOException {
		String command = "\"" + KonvertiMain.FFMPEG_BIN_FOLDER + File.separator + "ffmpeg\" ";
		//input file parameter
		command = command + "-i \"" + inFile.getAbsolutePath() + "\" ";
		//dont show banner every time
		command = command + "-hide_banner ";
		//dont show lots of text
		command = command + "-v warning ";
		command = command + addConversionParams();
		//output file
		command = command + "\"" + outFile.getAbsolutePath() + "\"";
		this.txt_cmdline.append("Command: " + command + "\n");
		ProcessBuilder builder = new ProcessBuilder(command);
	    final Process process = builder.start();
	    InputStream is = process.getErrorStream();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
	    	txt_cmdline.append(line + "\n");
	    	if (cancel_signal) {
	    		return;
	    	}
	    }
	    br.close();
	    isr.close();
	    is.close();
	}
	
	/**
	 * Figure out what to append to the command line, based on the selected preset
	 * @return the command parameters that represent this preset
	 */
	private String addConversionParams() {
		String command = "";
		if (conversion_preset == PRESET_CD) {
			//audio only
			command = command + " -vn ";
			//44100 sample rate
			command = command + " -ar 44100 ";
			//2 channels
			command = command + " -ac 2 ";
			//16 bit, signed PCM codec
			command = command + " -acodec pcm_s16le ";
		}
		return command;
	}
	
	/**
	 * Interate through the incoming files, and attempt to find appropriate names for new files to use for conversion
	 * @param incomingFilesList the incoming files from FFProbe
	 */
	private Vector<ConvertFileEntry> populateFilesList(java.util.List<FFProbeExaminer> incomingFilesList) {
		Vector<ConvertFileEntry> vec_cfe = new Vector<ConvertFileEntry>();
		String extension = getExtension();
		int counter = 0;
		while (counter < incomingFilesList.size()) {
			try {
				File inFile = incomingFilesList.get(counter).getFile();
				ConvertFileEntry cfe = new ConvertFileEntry(inFile, extension);
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
		btn_close.setEnabled(true);
		btn_stop.setEnabled(false);
		lbl_status.setText("Status: Conversion canceled.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cancel_signal = true;
	}
	
	/**
	 * Figure out what extension should go on the end of the file based on the new file type
	 * @return
	 */
	private String getExtension() {
		if (conversion_preset == PRESET_CD) {
			return "wav";
		}
		return "";
	}

}
