package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Picker dialog for AIFF options.
 * @author Jim Halliday
 */
public class PickerAIFF extends PickerDialog {
	
	private JComboBox<String> comb_codec;
	
	/**
	 * Initialize the AIFF options picker window
	 * @param struct the struct to pass through
	 */
	public PickerAIFF(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose AIFF encoding options");
		comb_codec = new JComboBox<String>();
		//entry 0 - AIFF audio (8 bit)
		if (KonvertiUtils.encoderIsPreset("pcm_s8")) {
			comb_codec.addItem("AIFF audio (8-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - AIFF audio (8-bit)");
        }
        //entry 1 - AIFF audio (16 bit) - big-endian
        if (KonvertiUtils.encoderIsPreset("pcm_s16be")) {
        	comb_codec.addItem("AIFF audio (16-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - AIFF audio (16-bit)");
        }
        //entry 2 - AIFF audio (16 bit) - little-endian
        if (KonvertiUtils.encoderIsPreset("pcm_s16le")) {
        	comb_codec.addItem("AIFF audio (16-bit, OS X)");
        } else {
        	comb_codec.addItem("(Missing encoder) - AIFF audio (16-bit, OS X)");
        }
        //entry 3 - AIFF audio (24 bit) - big-endian
        if (KonvertiUtils.encoderIsPreset("pcm_s24be")) {
        	comb_codec.addItem("AIFF audio (24-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - AIFF audio (24-bit)");
        }
        //entry 4 - AIFF audio (32 bit) - big-endian
        if (KonvertiUtils.encoderIsPreset("pcm_s32be")) {
        	comb_codec.addItem("AIFF audio (32-bit)");
        } else {
        	comb_codec.addItem("(Missing encoder) - AIFF audio (32-bit)");
        }
        comb_codec.setSelectedItem(1);
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Choose AIFF format:");
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
			struct.params.setAudioEncodingCodec("pcm_s8");
		} else if (comb_codec.getSelectedIndex() == 1) {
			struct.params.setAudioEncodingCodec("pcm_s16be");
		} else if (comb_codec.getSelectedIndex() == 2) {
			struct.params.setAudioEncodingCodec("pcm_s16le");
		} else if (comb_codec.getSelectedIndex() == 3) {
			struct.params.setAudioEncodingCodec("pcm_s24be");
		} else if (comb_codec.getSelectedIndex() == 4) {
			struct.params.setAudioEncodingCodec("pcm_s32be");
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
