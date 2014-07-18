package malictus.konverti.ui.components;

import java.awt.FlowLayout;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Component to pick the specific codec for using an AAC-type audio codec
 * @author Jim Halliday
 */
public class AACCodec extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_codec;

	public AACCodec() {
		super();
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
        setLayout(new FlowLayout());
        JLabel lbl_codec = new JLabel("Choose AAC codec");
        add(lbl_codec);
        add(comb_codec);
	}
	
	public void modifyStruct(FFmpegStruct struct) throws Exception {
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
			throw new Exception("Invalid AAC encoder specified");
		}
	}
	
}
