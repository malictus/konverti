package malictus.konverti.ui.components;

import java.awt.FlowLayout;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Component to pick the encoding codec to use with a WMA file
 * @author Jim Halliday
 */
public class WMACodec extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_codec;

	public WMACodec() {
		super();
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
        setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Choose WMA Codec:");
        add(lbl_qual);
        add(comb_codec);
	}
	
	public void modifyStruct(FFmpegStruct struct) throws Exception {
		if (comb_codec.getSelectedItem().equals("wmav1")) {
			struct.params.setAudioEncodingCodec("wmav1");
		} else if (comb_codec.getSelectedItem().equals("wmav2")) {
			struct.params.setAudioEncodingCodec("wmav2");
		} else {
			throw new Exception("Invalid WMA Codec specified");
		}
	}
	
}
