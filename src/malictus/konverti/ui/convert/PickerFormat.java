package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.*;

import malictus.konverti.KonvertiUtils;
import malictus.konverti.ui.main.MainPanel;

/**
 * A simple dialog that allows the user to pick a target format (file extension) and start the process of a custom convert.
 * @author Jim Halliday
 */
public class PickerFormat extends JDialog {
	
	private MainPanel parent;
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	private JPanel contentPanel;
	
	/**
	 * Initialize the format picker window
	 * @param parent
	 */
	public PickerFormat(MainPanel parent) {
		super();
		this.parent = parent;
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose an output format");
		this.setModal(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        JLabel lbl_format = new JLabel("Please choose the target file format for export.");
        JComboBox<String> comb_format = new JComboBox<String>();
        if (KonvertiUtils.encoderIsPreset("pcm_s16le")) {
        	comb_format.addItem("WAV audio");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio");
        }
        if (KonvertiUtils.encoderIsPreset("libmp3lame")) {
        	comb_format.addItem("MP3 audio");
        } else {
        	comb_format.addItem("(Missing encoder) - MP3 audio");
        }
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(lbl_format);
        center_panel.add(comb_format);
        contentPanel.add(center_panel, BorderLayout.CENTER);
        //TODO - add next and cancel buttons, disable 'next' button if incorrect combo option is pressed, adjust size
        
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

}
