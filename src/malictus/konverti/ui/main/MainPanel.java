package malictus.konverti.ui.main;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import malictus.konverti.*;
import malictus.konverti.examine.FFProbeExaminer;
import malictus.konverti.examine.Stream;
import malictus.konverti.ui.convert.*;

/**
 * This class represents the main window for the program.
 * 
 * @author Jim Halliday
 */
public class MainPanel extends JFrame {
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 600;
	private JPanel contentPanel;
	private FileTable tbl_file = null;
	private JLabel lbl_status;
	private JButton btn_removeAll;
    private JButton btn_removeSelected;
    protected JButton btn_cancel;
    private JButton btn_play;
    private JTextArea txt_fileinfo;
    private JScrollPane scroll_file;
    private final static String ALT_TEXT_EMPTY = "             Drag and drop files and folders here";
    private final static String ALT_TEXT_PROCESSING = "             Processing files --- please wait";
    private JLabel lbl_alt_tbl_text = new JLabel(ALT_TEXT_EMPTY);
    private JComboBox<String> comb_preset;
    private JButton btn_convert;
    private JButton btn_custom; 
    private JButton btn_Browse_Conv;
    private JCheckBox chk_loc;
    private JTextField txt_Browse_Conv;
    private File convert_folder;
    private JFileChooser jfc_customdir;
    private JButton btn_ffChoose;
    private JTextField txt_ffChoose;
    
	/*
	 * Initialize the main window
	 */
	public MainPanel() {
		super();
		jfc_customdir = new JFileChooser();
		jfc_customdir.setMultiSelectionEnabled(false);
		jfc_customdir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		setTitle("Konverti " + KonvertiMain.VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel();        
        /*********************************/
        /** set up components on screen **/
        /*********************************/
        contentPanel.setLayout(new BorderLayout());
        //file drag/drop table (center)
	    String[][] data = new String[][]{};
	    DefaultTableModel model = new DefaultTableModel(data, FileTable.COLUMN_NAMES);
        tbl_file = new FileTable(model, this);
        scroll_file = new JScrollPane(tbl_file);
        Font big_font = new Font(Font.DIALOG, Font.PLAIN, 14);
        lbl_alt_tbl_text.setFont(big_font);
        scroll_file.setViewportView(lbl_alt_tbl_text);
        new FileDrop(scroll_file, new FileDrop.Listener() {   
	    	public void filesDropped(File[] files ) {   
	    		tbl_file.handleFileDrop(files);
            } 
        });
        contentPanel.add(scroll_file, BorderLayout.CENTER);  
        //north panel
        JPanel pnl_north = new JPanel();
        JLabel lbl_drag = new JLabel("<html><font color = 'blue' ><u>Visit Konverti Website</u></font></center>");
        lbl_drag.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl_drag.addMouseListener(new MouseAdapter() {  
            public void mouseReleased(MouseEvent e) {  
                if (!e.isPopupTrigger()) {  
                	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse((new URL("http://www.konverti.org")).toURI());
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                }  
            }  
        });  
        pnl_north.setLayout(new FlowLayout());
        pnl_north.add(lbl_drag);
        contentPanel.add(pnl_north, BorderLayout.NORTH);
        //east panel - most functionality goes here
        JPanel pnl_east = new JPanel();
        pnl_east.setLayout(new BorderLayout());
        //play button and info pane
        JPanel pnl_text_and_play = new JPanel();
        pnl_text_and_play.setLayout(new BorderLayout());
        JPanel pnl_play = new JPanel();
        pnl_play.setLayout(new FlowLayout());
        btn_play = new JButton("Play");
        btn_play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWithDefault();
            }
        }); 
        btn_play.setEnabled(false);
        pnl_play.add(btn_play);
        pnl_text_and_play.add(pnl_play, BorderLayout.NORTH);        
        txt_fileinfo = new JTextArea(13, 24);
        txt_fileinfo.setEditable(false);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
        txt_fileinfo.setFont(font);
        JScrollPane scrollPane = new JScrollPane(txt_fileinfo); 
        pnl_text_and_play.add(scrollPane, BorderLayout.CENTER);
        pnl_east.add(pnl_text_and_play, BorderLayout.NORTH);
        JPanel pnl_convert = new JPanel();
        pnl_convert.setLayout(new BoxLayout(pnl_convert, BoxLayout.Y_AXIS));
        JPanel combobox_panel = new JPanel();
        combobox_panel.setLayout(new FlowLayout());
        combobox_panel.setMaximumSize(new Dimension(600, 40));
        comb_preset = new JComboBox<String>();
        comb_preset.setAlignmentX(Component.CENTER_ALIGNMENT);
        populateComboBox();
        comb_preset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateTheUI(tbl_file.getSelectedRowCount(), tbl_file.getRowCount());
            }
        });
        combobox_panel.add(comb_preset);
        btn_convert = new JButton("Convert Now!");
        btn_convert.setMargin(new Insets(2,2,2,2));
        btn_convert.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_convert.setEnabled(false);
        btn_convert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertFiles();
            }
        }); 
        combobox_panel.add(btn_convert);
        pnl_convert.add(combobox_panel);
        btn_custom = new JButton("Custom Convert...");
        btn_custom.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_custom.setEnabled(false);
        btn_custom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                customConvertFiles();
            }
        }); 
        pnl_convert.add(Box.createRigidArea(new Dimension(0, 16)));
        pnl_convert.add(btn_custom);
        JLabel lbl_ffChoose = new JLabel("FFmpeg location");
        lbl_ffChoose.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnl_convert.add(Box.createRigidArea(new Dimension(0, 70)));
        pnl_convert.add(lbl_ffChoose);
        JPanel pnl_ffChoose = new JPanel();
        pnl_ffChoose.setLayout(new FlowLayout());
        txt_ffChoose = new JTextField();
        txt_ffChoose.setEditable(false);
        txt_ffChoose.setPreferredSize(new Dimension(190, 22));
        reset_ffChoose_Textbox();
        btn_ffChoose = new JButton("Choose...");
        btn_ffChoose.setMargin(new Insets(2,2,2,2));
        btn_ffChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (KonvertiMain.setFFmpegdir()) {
            		reset_ffChoose_Textbox();
            	}
            }
        }); 
        pnl_ffChoose.add(txt_ffChoose);
        pnl_ffChoose.add(btn_ffChoose);
        pnl_convert.add(pnl_ffChoose);
        chk_loc = new JCheckBox("Use this folder for all converted files:");
        chk_loc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chk_loc.isSelected()) {
                	btn_Browse_Conv.setEnabled(true);
                	txt_Browse_Conv.setEnabled(true);
                } else {
                	btn_Browse_Conv.setEnabled(false);
                	txt_Browse_Conv.setEnabled(false);
                }
            }
        }); 
        chk_loc.setSelected(false);
        chk_loc.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnl_convert.add(Box.createRigidArea(new Dimension(0, 4)));
        pnl_convert.add(chk_loc);
        JPanel pnl_Browse_Conv = new JPanel();
        pnl_Browse_Conv.setLayout(new FlowLayout());
        txt_Browse_Conv = new JTextField();
        txt_Browse_Conv.setEditable(false);
        txt_Browse_Conv.setEnabled(false);
        txt_Browse_Conv.setPreferredSize(new Dimension(190, 22));
        this.convert_folder = new File(System.getProperty("user.home"));
        txt_Browse_Conv.setText(convert_folder.getAbsolutePath());
        btn_Browse_Conv = new JButton("Choose...");
        btn_Browse_Conv.setMargin(new Insets(2,2,2,2));
        btn_Browse_Conv.setEnabled(false);
        btn_Browse_Conv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCustomDir();
            }
        }); 
        pnl_Browse_Conv.add(txt_Browse_Conv);
        pnl_Browse_Conv.add(btn_Browse_Conv);
        pnl_convert.add(pnl_Browse_Conv);
        pnl_east.add(pnl_convert, BorderLayout.CENTER);
        contentPanel.add(pnl_east, BorderLayout.EAST);
        //south panel - status and cancel
        JPanel pnl_south = new JPanel();
        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.LEFT);
        pnl_south.setLayout(flow);
        btn_removeAll = new JButton("Remove All");
        btn_removeAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tbl_file.removeAllFiles();
            }
        }); 
        btn_removeAll.setEnabled(false);
        btn_removeSelected = new JButton("Remove Selected");
        btn_removeSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tbl_file.removeSelectedFiles();
            }
        });  
        btn_removeSelected.setEnabled(false);
        pnl_south.add(btn_removeAll);
        pnl_south.add(btn_removeSelected);
        btn_cancel = new JButton("Stop");
        btn_cancel.setEnabled(false);
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tbl_file.handleCancel();
            }
        });  
        pnl_south.add(btn_cancel);
        lbl_status = new JLabel("Status: Idle");
        pnl_south.add(lbl_status);
        contentPanel.add(pnl_south, BorderLayout.SOUTH);
        
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
	}
	
	/**
	 * Thread is running, so disable components that would disrupt that.
	 */
	protected void turnOffInterface() {
		tbl_file.setEnabled(false);
		this.btn_removeAll.setEnabled(false);
		this.btn_removeSelected.setEnabled(false);
		this.btn_play.setEnabled(false);
		this.btn_custom.setEnabled(false);
		this.btn_ffChoose.setEnabled(false);
		lbl_alt_tbl_text.setText(ALT_TEXT_PROCESSING);
		this.btn_convert.setEnabled(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * When a thread finishes, call this to turns things back on that can't be on while thread is running
	 */
	protected void turnOnInterface() {
		tbl_file.setEnabled(true);
		lbl_alt_tbl_text.setText(ALT_TEXT_EMPTY);
		btn_ffChoose.setEnabled(true);
		updateTheUI(tbl_file.getSelectedRowCount(), tbl_file.getRowCount());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Set the window status bar text
	 * @param status the new text
	 */
	protected void setStatus(String status) {
		this.lbl_status.setText("Status: " + status);
	}
	
	/**
	 * Triggered when changes are made to the file table
	 */
	protected void updateTheUI(int selectedRows, int totalRows) {
		if (totalRows > 0) {
			this.btn_removeAll.setEnabled(true);
			if (this.comb_preset.getSelectedIndex() > 0) {
				if (((String)comb_preset.getSelectedItem()).startsWith("(Missing encoder")) {
					this.btn_convert.setEnabled(false);
				} else {
					this.btn_convert.setEnabled(true);
				}
			} else {
				this.btn_convert.setEnabled(false);
			}
			this.btn_custom.setEnabled(true);
			scroll_file.setViewportView(this.tbl_file);
		} else {
			this.btn_removeAll.setEnabled(false);
			this.btn_convert.setEnabled(false);
			this.btn_custom.setEnabled(true);
			scroll_file.setViewportView(lbl_alt_tbl_text);
		}
		if (selectedRows > 0) {
			this.btn_removeSelected.setEnabled(true);
		} else {
			this.btn_removeSelected.setEnabled(false);
		}
		if (selectedRows == 1) {
			this.btn_play.setEnabled(true);
			txt_fileinfo.setText(showFileInfoFor(this.tbl_file.getSelectedRow()));
		} else {
			this.btn_play.setEnabled(false);
			txt_fileinfo.setText("");
		}
	}
	
	/**
	 * Set a custom directory for files to go to
	 */
	private void setCustomDir() {
		int response = jfc_customdir.showOpenDialog(null);
		if (response == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File x = jfc_customdir.getSelectedFile();
		if (!x.exists() || !x.isDirectory()) {
			return;
		}
		this.convert_folder = x;
		this.txt_Browse_Conv.setText(x.getAbsolutePath());
	}
	
	/**
	 * Reset the textbox that shows FFmpeg location
	 */
	private void reset_ffChoose_Textbox() {
		String x = KonvertiMain.FFMPEG_BIN_FOLDER;
    	if (x.equals("")) {
    		x = "(Raw Command Line)";
    	}
    	txt_ffChoose.setText(x);
	}
	
	/**
	 * Populate the combo box, including disabling any presets that won't work due to missing encoders
	 */
	private void populateComboBox() {
		comb_preset.addItem("Select a preset:");
		if (KonvertiUtils.encoderIsPreset("pcm_s16le")) {
			comb_preset.addItem("WAV File for CD Burning");
		} else {
			comb_preset.addItem("(Missing encoder) - WAV File for CD Burning");
		}
		if (KonvertiUtils.encoderIsPreset("libmp3lame")) {
			comb_preset.addItem("MP3 CBR - 320K");
			comb_preset.addItem("MP3 CBR - 192K");
			comb_preset.addItem("MP3 CBR - 128K");
			comb_preset.addItem("MP3 VBR - HI");
			comb_preset.addItem("MP3 VBR - MID");
			comb_preset.addItem("MP3 VBR - LOW");
		} else {
			comb_preset.addItem("(Missing encoder) - MP3 CBR - 320K");
			comb_preset.addItem("(Missing encoder) - MP3 CBR - 192K");
			comb_preset.addItem("(Missing encoder) - MP3 CBR - 128K");
			comb_preset.addItem("(Missing encoder) - MP3 VBR - HI");
			comb_preset.addItem("(Missing encoder) - MP3 VBR - MID");
			comb_preset.addItem("(Missing encoder) - MP3 VBR - LOW");
		}
		if (KonvertiUtils.encoderIsPreset("libvorbis")) {
			comb_preset.addItem("OGG Vorbis - LOW");
			comb_preset.addItem("OGG Vorbis - HI");
		} else {
			comb_preset.addItem("(Missing encoder) - OGG Vorbis - LOW");
			comb_preset.addItem("(Missing encoder) - OGG Vorbis - HI");
		}
			
	}
	
	/**
	 * Do the actual file conversion
	 */
	private void convertFiles() {
		if (this.chk_loc.isSelected()) {
			new ConversionPanel(this.comb_preset.getSelectedIndex(), this.convert_folder);
		} else {
			new ConversionPanel(this.comb_preset.getSelectedIndex(), null);
		}
	}
	
	/**
	 * Show custom convert dialogs
	 */
	private void customConvertFiles() {
		FFmpegParams params = new FFmpegParams();
		
		if (this.chk_loc.isSelected()) {
			FFmpegStruct struct = new FFmpegStruct(params, null, this.convert_folder);
			new PickerFormat(struct);
		} else {
			FFmpegStruct struct = new FFmpegStruct(params, null, null);
			new PickerFormat(struct);
		}
		
	}
	
	/**
	 * Generate a string that represents the selected file, for display in a text box
	 * @param selectedRow the selected row from the file table
	 * @return the string of file information
	 */
	private String showFileInfoFor(int selectedRow) {
		FFProbeExaminer f = KonvertiMain.vec_files.get(selectedRow);
		String val = "";
		try {
			val = val + "Path: " + f.getFile().getCanonicalPath() + "\n";
			val = val + "Number of streams: " + f.getStreams().length + "\n";
			int counter = 0;
			while (counter < f.getStreams().length) {
				Stream stream = f.getStreams()[counter];
				val = val + "\nStream " + (counter+1) + ": \n";
				if (!stream.codec_type.equals("")) {
					val = val + "Codec: " + stream.codec_long_name + " (" + stream.codec_type + ")\n";
				}
				if (!stream.channels.equals("")) {
					val = val + "Channels: " + stream.channels + "\n";
				}
				if (!stream.sample_rate.equals("")) {
					val = val + "Sample Rate: " + stream.sample_rate + "\n";
				}
				if (!stream.height.equals("")) {
					val = val + "Height: " + stream.height + "\n";
				}
				if (!stream.width.equals("")) {
					val = val + "Width: " + stream.width + "\n";
				}
				counter++;
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return val;
	}
	
	/**
	 * Open the selected file with the default program for this platform
	 */
	private void openWithDefault() {
		if (tbl_file.getSelectedRow() >= 0) {
			try {
				Desktop.getDesktop().open(KonvertiMain.vec_files.get(tbl_file.getSelectedRow()).getFile());
			} catch (IOException err) {
				err.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error opening default application", "Error opening file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
