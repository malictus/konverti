package malictus.konverti.ui.components;

import java.awt.FlowLayout;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;

/**
 * Component to pick the audio sample rate
 * @author Jim Halliday
 */
public class AudioSampleRate extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_samplerate;

	public AudioSampleRate() {
		super();
		JLabel lbl_samplerate = new JLabel("Audio sample rate (Hz):");
        comb_samplerate = new JComboBox<String>();
        comb_samplerate.addItem("Preserve original");
        comb_samplerate.addItem("11025");
        comb_samplerate.addItem("22050");
        comb_samplerate.addItem("44100");
        comb_samplerate.addItem("48000");
        comb_samplerate.addItem("96000");
        comb_samplerate.setEditable(true);
        setLayout(new FlowLayout());
        add(lbl_samplerate);
        add(comb_samplerate);
	}
	
	public void modifyStruct(FFmpegStruct struct) throws Exception {
		String samplerate = (String)comb_samplerate.getSelectedItem();
		if (!samplerate.equals("Preserve original")) {
			Integer samplerateInt;
			try {
				samplerateInt = Integer.decode(samplerate);
			} catch (Exception err) {
				throw new Exception("Invalid sample rate value");
			}
			if (samplerateInt.intValue() < 1000) {
				throw new Exception("Invalid sample rate value");
			}
			struct.params.setAudioSampleRate(samplerateInt.intValue());
		}
	}
	
}
