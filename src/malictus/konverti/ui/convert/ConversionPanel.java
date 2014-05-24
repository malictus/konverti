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
 * This is the dialog that appears while the actual file conversion is taking place.
 */
public class ConversionPanel extends JDialog {
	
	//UI Components
	public static final int WIDTH = 550;
	public static final int HEIGHT = 400;
	private JPanel contentPanel;
	private JLabel lbl_status;
	private JButton btn_close;
	private JTextArea txt_cmdline;
	private JButton btn_stop;
	//thread control
	private boolean cancel_signal = false;
	//the folder that new files should go into, or null if new files should go to the same directory as the originals
	private File target_folder = null;
	//which conversion preset to use when running FFmpeg; will be one of the PRESET_ static values
	private int conversion_preset;
	//presets, which should match the preset combox box on the parent window
	public static final int PRESET_WAV_CD = 1;
	public static final int PRESET_MP3_CBR_HI_320 = 2;
	public static final int PRESET_MP3_CBR_MID_192 = 3;
	public static final int PRESET_MP3_CBR_LO_128 = 4;	
	public static final int PRESET_MP3_VBR_HI_0 = 5;
	public static final int PRESET_MP3_VBR_MID_4 = 6;
	public static final int PRESET_MP3_VBR_LOW_7 = 7;
	
	/**
	 * Initialize the conversion window without a target specified
	 * @param conversion_preset which preset to use for conversion
	 */
	public ConversionPanel(int conversion_preset) {
		new ConversionPanel(conversion_preset, null);
	}
	
	/**
	 * Initialize the conversion window with a target specified
	 * @param conversion_preset which preset to use for conversion
	 * @param target_folder the target folder where converted files should go, or null if files should go into original folder
	 */
	public ConversionPanel(int conversion_preset, File target_folder) {
		super();
		this.target_folder = target_folder;
		this.conversion_preset = conversion_preset;
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Konverti " + KonvertiMain.VERSION + " -- File Conversion");
		this.setModal(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPanel = new JPanel();        
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
        //set up processing thread
        final java.util.List<FFProbeExaminer> incomingFilesListFinal = KonvertiMain.vec_files;
        Runnable q = new Runnable() {
			public void run() {
				doConvert(incomingFilesListFinal);
		    }
		};
		Thread t = new Thread(q);
		t.start();
		//show window (block until window closes)
        setVisible(true);
	}
	
	/**
	 * This method handles the actual file conversion process, in a separate thread
	 * @param incomingFilesList the files to process
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
		this.txt_cmdline.append("\nFinished Processing");
		btn_close.setEnabled(true);
		btn_stop.setEnabled(false);
		lbl_status.setText("Status: Finished converting " + filesList.size() + " files.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Run the FFmpeg command, based on the selected preset
	 * 
	 * @param inFile input file
	 * @param outFile output file
	 * @throws IOException if read/write errors occur
	 */
	private void runFFMpegCommand(File inFile, File outFile) throws IOException {
		FFmpegParams params = getConversionParams();
		FFmpegCommand theCommand = new FFmpegCommand(inFile.getAbsolutePath(), outFile.getAbsolutePath(), params);
		String command = theCommand.getCommand();
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
	 * Generate the command line options, based on the selected preset
	 * @return the FFmpegParams with appropriate parameters
	 */
	private FFmpegParams getConversionParams() {
		FFmpegParams command = new FFmpegParams();
		if (conversion_preset == PRESET_WAV_CD) {
			command.setAudioOnly(true);
			command.setAudioSampleRate(44100);
			command.setAudioChannels(2);
			command.setAudioEncodingCodec("pcm_s16le");
		} else if (conversion_preset == PRESET_MP3_CBR_HI_320) {
			command.setAudioOnly(true);
			command.setAudioEncodingCodec("libmp3lame");
			command.setAudioBitRate("320k");
		} else if (conversion_preset == PRESET_MP3_CBR_MID_192) {
			command.setAudioOnly(true);
			command.setAudioEncodingCodec("libmp3lame");
			command.setAudioBitRate("192k");
		} else if (conversion_preset == PRESET_MP3_CBR_LO_128) {
			command.setAudioOnly(true);
			command.setAudioEncodingCodec("libmp3lame");
			command.setAudioBitRate("128k");
		} else if (conversion_preset == PRESET_MP3_VBR_HI_0) {
			command.setAudioOnly(true);
			command.setAudioEncodingCodec("libmp3lame");
			command.setAudioQuality(0);
		} else if (conversion_preset == PRESET_MP3_VBR_MID_4) {
			command.setAudioOnly(true);
			command.setAudioEncodingCodec("libmp3lame");
			command.setAudioQuality(4);
		} else if (conversion_preset == PRESET_MP3_VBR_LOW_7) {
			command.setAudioOnly(true);
			command.setAudioEncodingCodec("libmp3lame");
			command.setAudioQuality(7);
		}
		return command;
	}
	
	/**
	 * Iterate through the incoming files, and attempt to find appropriate names for new files to use for conversion
	 * @param incomingFilesList the incoming files from FFProbe
	 */
	private Vector<ConvertFileEntry> populateFilesList(java.util.List<FFProbeExaminer> incomingFilesList) {
		Vector<ConvertFileEntry> vec_cfe = new Vector<ConvertFileEntry>();
		String extension = getExtension();
		int counter = 0;
		while (counter < incomingFilesList.size()) {
			try {
				File inFile = incomingFilesList.get(counter).getFile();
				ConvertFileEntry cfe = new ConvertFileEntry(inFile, extension, target_folder);
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
		if (conversion_preset == PRESET_WAV_CD) {
			return "wav";
		}
		if ((conversion_preset == PRESET_MP3_CBR_HI_320) || (conversion_preset == PRESET_MP3_CBR_MID_192) ||
				(conversion_preset == PRESET_MP3_CBR_LO_128) || (conversion_preset == PRESET_MP3_VBR_HI_0) ||
				(conversion_preset == PRESET_MP3_VBR_MID_4) || (conversion_preset == PRESET_MP3_VBR_LOW_7)) {
			return "mp3";
		}
		return "";
	}

}
