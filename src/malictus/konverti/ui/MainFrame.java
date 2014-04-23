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
	private JTextField txtfProcess; 
	private JButton btnConvert;
	
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
        FlowLayout fl = new FlowLayout();
        fl.setHgap(5);
        fl.setVgap(5);
        fl.setAlignment(FlowLayout.LEFT);
        contentPane.setLayout(fl);
        /* FILE TO PROCESS LINE  */
        JPanel pnlProcess = new JPanel();
        FlowLayout f1 = new FlowLayout();
        f1.setAlignment(FlowLayout.LEFT);
        pnlProcess.setLayout(f1);
        JLabel lblProcess = new JLabel("File To Process:");
        txtfProcess = new JTextField("");
        txtfProcess.setPreferredSize(new Dimension(425, 25));
        JButton btnProcess = new JButton("Browse");
        pnlProcess.add(lblProcess);
        pnlProcess.add(txtfProcess);
        pnlProcess.add(btnProcess);
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int response = fc_process.showOpenDialog(null);
				if (response != JFileChooser.CANCEL_OPTION) {
					File x = fc_process.getSelectedFile();
					txtfProcess.setText(x.getPath());
				}	
			}
        });
        btnConvert = new JButton("<html><b>Convert</b></html>");
        btnConvert.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				processFile();
			}
        });
        pnlProcess.add(btnConvert);
        contentPane.add(pnlProcess);
        
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
		if (this.txtfProcess.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Error reading file to process.", "Error reading file", JOptionPane.WARNING_MESSAGE);
			return;
		}
		File fileToProcess;
		try {
			fileToProcess = new File(this.txtfProcess.getText());
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
		try {
			FFProbeExaminer ffprobe = new FFProbeExaminer(theFile);
			if (ffprobe.isValid()) {
				System.out.println("VALID");
				System.out.println(ffprobe.getInfo());
			} else {
				System.out.println("NOT VALID");
			}
		} catch (Exception err) {
			err.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error reading file using FFProbe.", "Error reading file", JOptionPane.ERROR_MESSAGE);
		}
		turnOnInterface();
	}
	
	private void turnOffInterface() {
		txtfProcess.setEnabled(false);
		btnConvert.setEnabled(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void turnOnInterface() {
		txtfProcess.setEnabled(true);
		btnConvert.setEnabled(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
