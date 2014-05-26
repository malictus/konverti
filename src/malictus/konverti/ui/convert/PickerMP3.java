package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;

/**
 * Picker dialog for MP3 options.
 * @author Jim Halliday
 */
public class PickerMP3 extends PickerDialog {
	
	private JComboBox<String> comb_bitrate;
	private JComboBox<String> comb_quality;
	private JRadioButton chk_bitrate;
	private JRadioButton chk_quality;
	
	/**
	 * Initialize the audio options picker window
	 * @param struct the struct to pass through
	 */
	public PickerMP3(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose MP3 encoding options");
        chk_bitrate = new JRadioButton("Constant Bitrate");
        chk_quality = new JRadioButton("Variable Bitrate");
        chk_bitrate.setSelected(true);
        ButtonGroup grp = new ButtonGroup();
        grp.add(chk_bitrate);
        grp.add(chk_quality);
        comb_bitrate = new JComboBox<String>();
        comb_bitrate.addItem("8k");
        comb_bitrate.addItem("16k");
        comb_bitrate.addItem("24k");
        comb_bitrate.addItem("32k");
        comb_bitrate.addItem("40k");
        comb_bitrate.addItem("48k");
        comb_bitrate.addItem("64k");
        comb_bitrate.addItem("80k");
        comb_bitrate.addItem("96k");
        comb_bitrate.addItem("112k");
        comb_bitrate.addItem("128k");
        comb_bitrate.addItem("160k");
        comb_bitrate.addItem("192k");
        comb_bitrate.addItem("224k");
        comb_bitrate.addItem("256k");
        comb_bitrate.addItem("320k");
        comb_bitrate.setSelectedItem("192k");
        comb_quality = new JComboBox<String>();
        comb_quality.addItem("0 (best)");
        comb_quality.addItem("1");
        comb_quality.addItem("2");
        comb_quality.addItem("3");
        comb_quality.addItem("4");
        comb_quality.addItem("5");
        comb_quality.addItem("6");
        comb_quality.addItem("7");
        comb_quality.addItem("8");
        comb_quality.addItem("9 (worst)");
        comb_quality.setSelectedItem("5");
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(chk_bitrate);
        center_panel.add(comb_bitrate);
        center_panel.add(chk_quality);
        center_panel.add(comb_quality);
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
			comb_bitrate.setEnabled(true);
			comb_quality.setEnabled(false);
		} else {
			comb_bitrate.setEnabled(false);
			comb_quality.setEnabled(true);
		}
	}
	
	/**
	 * Next step after this is general audio options dialog
	 */
	private void doNext() {
		if (chk_bitrate.isSelected()) {
			struct.params.setAudioBitRate((String)comb_bitrate.getSelectedItem());
		} else {
			struct.params.setAudioQuality(comb_quality.getSelectedIndex());
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
