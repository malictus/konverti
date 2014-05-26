package malictus.konverti.ui.convert;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;

/**
 * A simple dialog that allows the user to pick something. This is the abstract class that all picker dialogs extend.
 * @author Jim Halliday
 */
public abstract class PickerDialog extends JDialog {
	
	//this should be build and persisted from dialog to dialog
	protected FFmpegStruct struct;
	//default values for width and height
	private static final int WIDTH = 300;
	private static final int HEIGHT = 200;
	protected JPanel contentPanel;
	protected JButton btn_cancel = new JButton("Cancel");
    protected JButton btn_next = new JButton("Next");
	
	/**
	 * Initialize the format picker dialog
	 * @param struct the struct object to pass through the window
	 */
	public PickerDialog(FFmpegStruct struct) {
		super();
		this.struct = struct;
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Konverti Picker Window");		//default value
		this.setModal(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        JPanel pnl_bottom = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.RIGHT);
        pnl_bottom.setLayout(fl);
        pnl_bottom.add(btn_cancel);
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        }); 
        pnl_bottom.add(btn_next);
        contentPanel.add(pnl_bottom, BorderLayout.SOUTH);
        //finalize
        contentPanel.setOpaque(true); 
        setContentPane(contentPanel);
        setResizable(true);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
	}
	
	/**
	 * Handle cleanup when cancel button is pressed
	 */
	protected void doCancel() {
		setVisible(false);
        dispose();
	}

	public FFmpegStruct getStruct() {
		return struct;
	}

}
