package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Picker dialog for WAV options.
 * @author Jim Halliday
 */
public class PickerWAV extends PickerDialog {
	
	private JComboBox<String> comb_codec;
	
	/**
	 * Initialize the WAV options picker window
	 * @param struct the struct to pass through
	 */
	public PickerWAV(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose WAV encoding options");
		comb_codec = new JComboBox<String>();
		//entry 0 - WAV audio (8 bit)
		if (KonvertiUtils.encoderIsPreset("pcm_u8")) {
			comb_codec.addItem("WAV audio (8-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - WAV audio (8-bit)");
        }
        //entry 1 - WAV audio (16 bit)
        if (KonvertiUtils.encoderIsPreset("pcm_s16le")) {
        	comb_codec.addItem("WAV audio (16-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - WAV audio (16-bit)");
        }
        //entry 2 - WAV audio (24 bit)
        if (KonvertiUtils.encoderIsPreset("pcm_s24le")) {
        	comb_codec.addItem("WAV audio (24-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - WAV audio (24-bit)");
        }
        //entry 3 - WAV audio (32 bit)
        if (KonvertiUtils.encoderIsPreset("pcm_s32le")) {
        	comb_codec.addItem("WAV audio (32-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - WAV audio (32-bit)");
        }
        //entry 4 - WAV audio (32 bit floating)
        if (KonvertiUtils.encoderIsPreset("pcm_f32le")) {
        	comb_codec.addItem("WAV audio (32-bit floating)");
        } else {
        	comb_codec.addItem("(Missing encoder) - WAV audio (32-bit floating)");
        }
        //entry 5 - WAV audio (64 bit floating)
        if (KonvertiUtils.encoderIsPreset("pcm_f64le")) {
        	comb_codec.addItem("WAV audio (64-bit floating)");
        } else {
        	comb_codec.addItem("(Missing encoder) - WAV audio (64-bit floating)");
        }
        comb_codec.setSelectedItem(1);
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Choose WAV format:");
        center_panel.add(lbl_qual);
        center_panel.add(comb_codec);
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
		if (((String)comb_codec.getSelectedItem()).startsWith("(Missing encoder")) {
			return;
		}
		if (comb_codec.getSelectedIndex() == 0) {
			//WAV 8 bit
			struct.params.setAudioEncodingCodec("pcm_u8");
		} else if (comb_codec.getSelectedIndex() == 1) {
			//WAV 16 bit
			struct.params.setAudioEncodingCodec("pcm_s16le");
		} else if (comb_codec.getSelectedIndex() == 2) {
			//WAV 24 bit
			struct.params.setAudioEncodingCodec("pcm_s24le");
		} else if (comb_codec.getSelectedIndex() == 3) {
			//WAV 32 bit
			struct.params.setAudioEncodingCodec("pcm_s32le");
		} else if (comb_codec.getSelectedIndex() == 4) {
			//WAV 32 bit float
			struct.params.setAudioEncodingCodec("pcm_f32le");
		} else if (comb_codec.getSelectedIndex() == 5) {
			//WAV 64 bit float
			struct.params.setAudioEncodingCodec("pcm_f64le");
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
