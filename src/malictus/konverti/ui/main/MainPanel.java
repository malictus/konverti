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
    private JButton btn_play;
    private JTextArea textArea;
	
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
        contentPanel.add(pnl_north, BorderLayout.NORTH);
        //east panel - additional buttons
        JPanel pnl_east = new JPanel();
        pnl_east.setLayout(new BorderLayout());
        JPanel pnl_play_button = new JPanel();
        pnl_play_button.setLayout(new FlowLayout());
        btn_play = new JButton("Play");
        btn_play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWithDefault();
            }
        }); 
        btn_play.setEnabled(false);
        pnl_play_button.add(btn_play);
        pnl_east.add(pnl_play_button, BorderLayout.NORTH);
        textArea = new JTextArea(15, 25);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea); 
        JPanel pnl_text = new JPanel();
        pnl_text.setLayout(new FlowLayout());
        pnl_text.add(scrollPane);
        pnl_east.add(pnl_text, BorderLayout.EAST);
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
        btn_cancel = new JButton("Cancel");
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
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
        setVisible(true);
	}
	
	protected void turnOffInterface() {
		tbl_file.setEnabled(false);
		this.btn_removeAll.setEnabled(false);
		this.btn_removeSelected.setEnabled(false);
		this.btn_play.setEnabled(false);
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
			this.btn_play.setEnabled(true);
		} else {
			this.btn_play.setEnabled(false);
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
