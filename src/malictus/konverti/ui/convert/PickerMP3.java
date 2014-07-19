package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;
import malictus.konverti.ui.components.AudioBitrate;
import malictus.konverti.ui.components.AudioQuality;

/**
 * Picker dialog for MP3 options.
 * @author Jim Halliday
 */
public class PickerMP3 extends PickerDialog {
	
	private AudioBitrate bitrate;
	private AudioQuality quality;
	private JRadioButton chk_bitrate;
	private JRadioButton chk_quality;
	
	/**
	 * Initialize the MP3 options picker window
	 * @param struct the struct to pass through
	 */
	public PickerMP3(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(245, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose MP3 audio encoding options");
        chk_bitrate = new JRadioButton("CBR");
        chk_quality = new JRadioButton("VBR");
        chk_bitrate.setSelected(true);
        ButtonGroup grp = new ButtonGroup();
        grp.add(chk_bitrate);
        grp.add(chk_quality);
        bitrate = new AudioBitrate();
        quality = new AudioQuality();
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(chk_bitrate);
        center_panel.add(bitrate);
        center_panel.add(chk_quality);
        center_panel.add(quality);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
        chk_bitrate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enableAppropriate();
            }
        });
        chk_quality.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enableAppropriate();
            }
        });
        enableAppropriate();
	}
	
	/**
	 * Turn on and off the correct options
	 */
	private void enableAppropriate() {
		if (chk_bitrate.isSelected()) {
			bitrate.setEnabled(true);
			quality.setEnabled(false);
		} else {
			bitrate.setEnabled(false);
			quality.setEnabled(true);
		}
	}
	
	/**
	 * Next step after this is general audio options dialog
	 */
	private void doNext() {
		if (chk_bitrate.isSelected()) {
			bitrate.modifyStruct(struct);
		} else {
			quality.modifyStruct(struct);
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
