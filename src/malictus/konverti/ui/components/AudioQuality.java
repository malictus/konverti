package malictus.konverti.ui.components;

import java.awt.FlowLayout;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;

/**
 * Component to pick the audio quality
 * @author Jim Halliday
 */
public class AudioQuality extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_quality;
	private JLabel lbl_qual;

	public AudioQuality() {
		super();
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
        setLayout(new FlowLayout());
        lbl_qual = new JLabel("Choose audio quality");
        add(lbl_qual);
        add(comb_quality);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.comb_quality.setEnabled(enabled);
		this.lbl_qual.setEnabled(enabled);
	}
	
	public void modifyStruct(FFmpegStruct struct) {
		struct.params.setAudioQuality(comb_quality.getSelectedIndex());
	}
	
}
