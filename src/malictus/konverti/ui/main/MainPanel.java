package malictus.konverti.ui.main;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import malictus.konverti.*;

public class MainPanel extends JFrame {
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 600;
	private JPanel contentPanel;
	private FileTable tbl_file = null;
	
	/*
	 * TODO: 
	 * 			make it so drag triggers a pop-up progress dialog, with cancel/stop (use commented out code below)
	 * 			always show a dialog after complete, with number of files added / not added
	 * 			make column widths vary appropriately
	 *          make sorting by clicking column work
	 *  
	 * 		2. Include credits somehow
	 * 		3. Include window and drag/drop
	 * 		4. Make drop/drag work with folders too, with popup for recursion if needed
	 * 		5. file info list shows metadat and can be sorted
	 * 		6. individually selected items can be opened with ffplay or default app, and shows more info
	 * 		7. convert button works automatically and uses presets and basic settings
	 * 		8. advanced button brings up more advanced dialog
	 * 		9. process shows in separate window and can be canceled
	 */
	
	/*
	 * Initialize the main window
	 */
	public MainPanel() {
		super();
		setTitle("Konverti " + KonvertiMain.VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel();        
        /*********************************/
        /** set up components on screen **/
        /*********************************/
        contentPanel.setLayout(new BorderLayout());
	    String[][] data = new String[][]{};
	    DefaultTableModel model = new DefaultTableModel(data, FileTable.COLUMN_NAMES);
        tbl_file = new FileTable(model);
        JScrollPane scroll = new JScrollPane(tbl_file);
        contentPanel.add(scroll);     
        
        //finalize
        contentPanel.setOpaque(true); 
        setContentPane(contentPanel);
        setResizable(false);
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
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
        setVisible(true);
	}
	
	/*
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
	
	*/

}
