package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegParams;
import malictus.konverti.KonvertiUtils;
import malictus.konverti.ui.main.MainPanel;

/**
 * A simple dialog that allows the user to pick a target format (file extension) and start the process of a custom convert.
 * @author Jim Halliday
 */
public class PickerFormat extends PickerDialog {
	
	private JComboBox<String> comb_format;
	
	/**
	 * Initialize the format picker window
	 * @param parent the parent panel
	 * @params params the FFmpegParams object to use
	 */
	public PickerFormat(MainPanel parent, FFmpegParams params) {
		super(parent, params, null);
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose an output format");
        JLabel lbl_format = new JLabel("Please choose the target file format for export.");
        comb_format = new JComboBox<String>();
        //entry 0 - WAV audio
        if (KonvertiUtils.encoderIsPreset("pcm_s16le")) {
        	comb_format.addItem("WAV audio");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio");
        }
        //entry 1 - mp3 audio
        if (KonvertiUtils.encoderIsPreset("libmp3lame")) {
        	comb_format.addItem("MP3 audio");
        } else {
        	comb_format.addItem("(Missing encoder) - MP3 audio");
        }
        comb_format.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateTheUI();
            }
        });
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(lbl_format);
        center_panel.add(comb_format);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
        updateTheUI();
	}
	
	/**
	 * Go to next step in process
	 */
	private void doNext() {
		if (comb_format.getSelectedIndex() == 0) {
			//WAV
			params.setAudioEncodingCodec("pcm_s16le");
			//TODO finish
			//new AudioOptionsPicker(parent, params, "wav");
		} else if (comb_format.getSelectedIndex() == 1) {
			//MP3
			params.setAudioEncodingCodec("libmp3lame");
			//TODO finish
			//new MP3Picker(parent, params, "mp3");
		}
		setVisible(false);
        dispose();
	}
	
	/**
	 * Update the UI based on whether the format is available or not
	 */
	private void updateTheUI() {
		if (((String)comb_format.getSelectedItem()).startsWith("(Missing encoder")) {
			btn_next.setEnabled(false);
		} else {
			btn_next.setEnabled(true);
		}
	}

}
