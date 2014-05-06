package malictus.konverti.ui.main;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

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
    protected JButton btn_cancel;
    private JButton btn_ffplay;
    private JButton btn_default;
	
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
        //south panel - other stuff
        JPanel pnl_south_top = new JPanel();
        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.LEFT);
        pnl_south_top.setLayout(flow);
        btn_cancel = new JButton("Cancel");
        btn_cancel.setEnabled(false);
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tbl_file.handleCancel();
            }
        });  
        pnl_south_top.add(btn_cancel);
        lbl_status = new JLabel("Status: Idle");
        pnl_south_top.add(lbl_status);
        JPanel pnl_south_middle = new JPanel();
        pnl_south_middle.setLayout(new FlowLayout());
        btn_ffplay = new JButton("Open selected with FFPlay");
        btn_ffplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWithFFplay();
            }
        }); 
        btn_ffplay.setEnabled(false);
        btn_default = new JButton("Open selected with default program");
        btn_default.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWithDefault();
            }
        }); 
        btn_default.setEnabled(false);
        pnl_south_middle.add(btn_ffplay);
        pnl_south_middle.add(btn_default);
        
        JPanel pnl_south = new JPanel();
        pnl_south.setLayout(new BorderLayout());
        pnl_south.add(pnl_south_top, BorderLayout.NORTH);
        pnl_south.add(pnl_south_middle, BorderLayout.CENTER);
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
		this.btn_removeAll.setEnabled(false);
		this.btn_removeSelected.setEnabled(false);
		this.btn_ffplay.setEnabled(false);
		this.btn_default.setEnabled(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	protected void turnOnInterface() {
		tbl_file.setEnabled(true);
		updateTheUI(tbl_file.getSelectedRowCount(), tbl_file.getRowCount());
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
		if (selectedRows == 1) {
			this.btn_default.setEnabled(true);
			this.btn_ffplay.setEnabled(true);
		} else {
			this.btn_default.setEnabled(false);
			this.btn_ffplay.setEnabled(false);
		}
	}
	
	/**
	 * Open the selected file with FFPlay
	 */
	private void openWithFFplay() {
		if (tbl_file.getSelectedRow() >= 0) {
			try {
				KonvertiUtils.runFFPlayCommand(tbl_file.getFFProbeFiles().get(tbl_file.getSelectedRow()).getFile());
			} catch (Exception err) {
				err.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error opening default application", "Error opening file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Open the selected file with the default program for this platform
	 */
	private void openWithDefault() {
		if (tbl_file.getSelectedRow() >= 0) {
			try {
				Desktop.getDesktop().open(tbl_file.getFFProbeFiles().get(tbl_file.getSelectedRow()).getFile());
			} catch (IOException err) {
				err.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error opening default application", "Error opening file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
