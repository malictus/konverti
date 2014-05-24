package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import malictus.konverti.FFmpegParams;
import malictus.konverti.KonvertiUtils;
import malictus.konverti.ui.main.MainPanel;

/**
 * A simple dialog that allows the user to pick a target format (file extension) and start the process of a custom convert.
 * @author Jim Halliday
 */
public class PickerFormat extends PickerDialog {
	
	/**
	 * Initialize the format picker window
	 * @param parent the parent panel
	 * @params params the FFmpegParams object to use
	 */
	public PickerFormat(MainPanel parent, FFmpegParams params) {
		super(parent, params);
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose an output format");
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
        //TODO - add logic for when to disable next button
        //TODO - finish this panel and attach params when OK is pressed and move on
	}

}
