package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Picker dialog for WMA options.
 * @author Jim Halliday
 */
public class PickerWMA extends PickerDialog {
	
	private JComboBox<String> comb_codec;
	private JComboBox<String> comb_bitrate;
	
	/**
	 * Initialize the WMA options picker window
	 * @param struct the struct to pass through
	 */
	public PickerWMA(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose Windows Media Audio encoding options");
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
        comb_codec = new JComboBox<String>();
        if (KonvertiUtils.encoderIsPreset("wmav1")) {
        	comb_codec.addItem("wmav1");
        } else {
        	comb_codec.addItem("(Missing encoder) - wmav1");
        }
        if (KonvertiUtils.encoderIsPreset("wmav2")) {
        	comb_codec.addItem("wmav2");
        } else {
        	comb_codec.addItem("(Missing encoder) - wmav2");
        }   
        comb_codec.setSelectedIndex(1);
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        JLabel lbl_codec = new JLabel("Choose codec");
        center_panel.add(lbl_codec);
        center_panel.add(comb_codec);
        JLabel lbl_qual = new JLabel("Choose encoding quality");
        center_panel.add(lbl_qual);
        center_panel.add(comb_bitrate);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
	}
	
	/**
	 * Next step after this is general audio options dialog
	 */
	private void doNext() {
		struct.params.setAudioBitRate((String)comb_bitrate.getSelectedItem());
		if (comb_codec.getSelectedItem().equals("wmav1")) {
			struct.params.setAudioEncodingCodec("wmav1");
		} else if (comb_codec.getSelectedItem().equals("wmav2")) {
			struct.params.setAudioEncodingCodec("wmav2");
		} else {
			//user chose invalid codec
			return;
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
