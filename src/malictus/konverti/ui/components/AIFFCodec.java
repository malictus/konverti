package malictus.konverti.ui.components;

import java.awt.FlowLayout;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Component to pick the encoding codec to use with an AIFF file
 * @author Jim Halliday
 */
public class AIFFCodec extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_codec;

	public AIFFCodec() {
		super();
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
        setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Choose AIFF format:");
        add(lbl_qual);
        add(comb_codec);
	}
	
	public void modifyStruct(FFmpegStruct struct) throws Exception {
		if (((String)comb_codec.getSelectedItem()).startsWith("(Missing encoder")) {
			throw new Exception("Missing encoder");
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
	}
	
}
