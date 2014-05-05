package malictus.konverti.ui.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import malictus.konverti.*;

public class MainPanel extends JFrame {
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 600;
	private JPanel contentPanel;
	private FileTable tbl_file = null;
	private JLabel lbl_status;
	private JButton btn_removeAll;
    private JButton btn_removeSelected;
	
	/*
	 * TODO: 
	 * 			create cancel button that cancels but still adds what has already been created
	 * 			when only one selected, show additionl info in dialog box, and ffplay button and default app open button
	 *  
	 * 		2. Include credits somehow
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
        //file drag/drop table (center)
	    String[][] data = new String[][]{};
	    DefaultTableModel model = new DefaultTableModel(data, FileTable.COLUMN_NAMES);
        tbl_file = new FileTable(model, this);
        JScrollPane scroll = new JScrollPane(tbl_file);
        contentPanel.add(scroll, BorderLayout.CENTER);  
        //north panel
        JPanel pnl_north = new JPanel();
        JLabel lbl_drag = new JLabel("Drag and drop files and folders to be processed below.");
        pnl_north.setLayout(new BorderLayout());
        pnl_north.add(lbl_drag, BorderLayout.WEST);
        JPanel pnl_upper_buttons = new JPanel();
        pnl_upper_buttons.setLayout(new FlowLayout());
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
        pnl_upper_buttons.add(btn_removeAll);
        pnl_upper_buttons.add(btn_removeSelected);
        pnl_north.add(pnl_upper_buttons, BorderLayout.EAST);
        contentPanel.add(pnl_north, BorderLayout.NORTH);
        //south panel - status and progress components
        JPanel pnl_south = new JPanel();
        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.LEFT);
        pnl_south.setLayout(flow);
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
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
        setVisible(true);
	}
	
	protected void turnOffInterface() {
		tbl_file.setEnabled(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	protected void turnOnInterface() {
		tbl_file.setEnabled(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	protected void setStatus(String status) {
		this.lbl_status.setText("Status: " + status);
	}
	
	/**
	 * Triggered when changes are made to the file table
	 */
	protected void updateTheUI(int selectedRows, int totalRows) {
		if (totalRows > 0) {
			this.btn_removeAll.setEnabled(true);
		} else {
			this.btn_removeAll.setEnabled(false);
		}
		if (selectedRows > 0) {
			this.btn_removeSelected.setEnabled(true);
		} else {
			this.btn_removeSelected.setEnabled(false);
		}
	}

}
