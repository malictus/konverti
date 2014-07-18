package malictus.konverti.ui.components;

import java.awt.FlowLayout;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;

/**
 * Component to pick the audio bitrate
 * @author Jim Halliday
 */
public class AudioBitrate extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_bitrate;

	public AudioBitrate() {
		super();
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
        comb_bitrate.setEditable(false);
        comb_bitrate.setSelectedItem("192k");
        setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Choose audio bitrate");
        add(lbl_qual);
        add(comb_bitrate);
	}
	
	public void modifyStruct(FFmpegStruct struct) {
		struct.params.setAudioBitRate((String)comb_bitrate.getSelectedItem());
	}
	
}
