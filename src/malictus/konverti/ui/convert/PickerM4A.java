package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Picker dialog for M4A options.
 * @author Jim Halliday
 */
public class PickerM4A extends PickerDialog {
	
	private JComboBox<String> comb_codec;
	private JComboBox<String> comb_bitrate;
	
	/**
	 * Initialize the M4A options picker window
	 * @param struct the struct to pass through
	 */
	public PickerM4A(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose MPEG audio encoding options");
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
        if (KonvertiUtils.encoderIsPreset("aac")) {
        	comb_codec.addItem("aac (experimental)");
        } else {
        	comb_codec.addItem("(Missing encoder) - aac (experimental)");
        }
        if (KonvertiUtils.encoderIsPreset("libvo_aacenc")) {
        	comb_codec.addItem("libvo_aacenc");
        } else {
        	comb_codec.addItem("(Missing encoder) - libvo_aacenc");
        }   
        if (KonvertiUtils.encoderIsPreset("libfdk_aac")) {
        	comb_codec.addItem("libfdk_aac");
        } else {
        	comb_codec.addItem("(Missing encoder) - libfdk_aac");
        }   
        if (KonvertiUtils.encoderIsPreset("libfaac")) {
        	comb_codec.addItem("libfaac");
        } else {
        	comb_codec.addItem("(Missing encoder) - libfaac");
        }   
        comb_codec.setSelectedIndex(0);
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
		if (comb_codec.getSelectedItem().equals("aac (experimental)")) {
			struct.params.setAudioEncodingCodec("aac");
			struct.params.setUseExperimental(true);
		} else if (comb_codec.getSelectedItem().equals("libvo_aacenc")) {
			struct.params.setAudioEncodingCodec("libvo_aacenc");
		} else if (comb_codec.getSelectedItem().equals("libfdk_aac")) {
			//add, as suggested by this page: http://trac.ffmpeg.org/wiki/Encode/AAC
			struct.params.setCutoff(18000);
			struct.params.setAudioEncodingCodec("libfdk_aac");
		} else if (comb_codec.getSelectedItem().equals("libfaac")) {
			struct.params.setAudioEncodingCodec("libfaac");
		} else {
			//user chose invalid codec
			return;
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
