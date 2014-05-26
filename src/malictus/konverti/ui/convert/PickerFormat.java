package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * A simple dialog that allows the user to pick a target format (file extension) and start the process of a custom convert.
 * @author Jim Halliday
 */
public class PickerFormat extends PickerDialog {
	
	private JComboBox<String> comb_format;
	
	/**
	 * Initialize the format picker window
	 * @param struct the struct object to pass through
	 */
	public PickerFormat(FFmpegStruct struct) {
		super(struct);
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
			struct.params.setAudioEncodingCodec("pcm_s16le");
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 1) {
			//MP3
			struct.params.setAudioEncodingCodec("libmp3lame");
			struct.extension = "mp3";
			//TODO finish
			new PickerMP3(struct);
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
