package malictus.konverti.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import malictus.konverti.*;
import malictus.konverti.examine.*;

public class MainFrame extends JFrame {
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 600;
	
	private JPanel contentPane = null;
	private JFileChooser fc_process = new JFileChooser();
	private JTextField txtf_Process; 
	private JButton btn_Convert;
	private JLabel lbl_Status;
	private JProbePanel pnl_Jprobe;
	
	/**
	 * Initialize the main window
	 */
	public MainFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Konverti " + KonvertiMain.VERSION);
        contentPane = new JPanel();
        this.setContentPane(contentPane);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setSize(WIDTH, HEIGHT);
        
        /*********************************/
        /** set up components on screen **/
        /*********************************/
        FlowLayout layout_ContentPane = new FlowLayout();
        layout_ContentPane.setHgap(5);
        layout_ContentPane.setVgap(5);
        layout_ContentPane.setAlignment(FlowLayout.LEFT);
        contentPane.setLayout(layout_ContentPane);
        /* FILE TO PROCESS LINE  */
        JPanel pnl_Process = new JPanel();
        FlowLayout layout_Process = new FlowLayout();
        layout_Process.setAlignment(FlowLayout.LEFT);
        pnl_Process.setLayout(layout_Process);
        JLabel lbl_Process = new JLabel("File To Process:");
        txtf_Process = new JTextField("");
        txtf_Process.setPreferredSize(new Dimension(425, 25));
        txtf_Process.setEditable(false);
        JButton btn_Process = new JButton("Browse");
        pnl_Process.add(lbl_Process);
        pnl_Process.add(txtf_Process);
        pnl_Process.add(btn_Process);
        btn_Process.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int response = fc_process.showOpenDialog(null);
				if (response != JFileChooser.CANCEL_OPTION) {
					File x = fc_process.getSelectedFile();
					txtf_Process.setText(x.getPath());
					disablePanels();
				}	
			}
        });
        btn_Convert = new JButton("<html><b>Convert</b></html>");
        btn_Convert.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				processFile();
			}
        });
        pnl_Process.add(btn_Convert);
        contentPane.add(pnl_Process);
        /** JProbe Panel **/
        pnl_Jprobe = new JProbePanel(this);        
        contentPane.add(pnl_Jprobe);
        pnl_Jprobe.setEnabled(false);
        /** Status Panel **/
        JPanel pnl_Status = new JPanel();
        lbl_Status = new JLabel("Idle");
        pnl_Status.add(lbl_Status);
        this.resetStatusPanel();
        contentPane.add(pnl_Status);
        
        //center on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
        	frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
        	frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
	}
	
	private void processFile() {
		if (this.txtf_Process.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Error reading file to process.", "Error reading file", JOptionPane.WARNING_MESSAGE);
			return;
		}
		File fileToProcess;
		try {
			fileToProcess = new File(this.txtf_Process.getText());
			if (!fileToProcess.exists() || !fileToProcess.canRead() || !fileToProcess.isFile()) {
				JOptionPane.showMessageDialog(this, "Error reading file to process.", "Error reading file", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (Exception err) {
			err.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error reading file to process.", "Error reading file", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//start the thread to do the actual file examination
		final File theFile = fileToProcess;
		Runnable q = new Runnable() {
			public void run() {
				readFileThread(theFile);
		    }
		};
		Thread t = new Thread(q);
		t.start();
	}
	
	private void readFileThread(File theFile) {
		turnOffInterface();
		lbl_Status.setText("Examing file using FFprobe");
		try {
			FFProbeExaminer ffprobe = new FFProbeExaminer(theFile);
			if (ffprobe.isValid()) {
				this.pnl_Jprobe.setEnabled(true);
				this.pnl_Jprobe.setDuration(ffprobe.getDuration());
				this.pnl_Jprobe.setFormat(ffprobe.getFormat());
			} else {
				this.pnl_Jprobe.setEnabled(false);
			}
		} catch (Exception err) {
			err.printStackTrace();
			resetStatusPanel();
			JOptionPane.showMessageDialog(this, "Error reading file using FFProbe.", "Error reading file", JOptionPane.ERROR_MESSAGE);
		}
		turnOnInterface();
		resetStatusPanel();
	}
	
	private void turnOffInterface() {
		txtf_Process.setEnabled(false);
		btn_Convert.setEnabled(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void turnOnInterface() {
		txtf_Process.setEnabled(true);
		btn_Convert.setEnabled(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void resetStatusPanel() {
		lbl_Status.setText("Idle");
	}
	
	private void disablePanels() {
		pnl_Jprobe.setEnabled(false);
	}

}
