package malictus.konverti.ui.components;

import java.awt.FlowLayout;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;

/**
 * Component to pick number of audio channels
 * @author Jim Halliday
 */
public class AudioChannels extends JPanel implements FFmpegComponent {
	
	private JComboBox<String> comb_channel;

	public AudioChannels() {
		super();
		JLabel lbl_channels = new JLabel("Number of channels:");
        comb_channel = new JComboBox<String>();
        comb_channel.addItem("Preserve original");
        comb_channel.addItem("1");
        comb_channel.addItem("2");
        comb_channel.setEditable(true);
        setLayout(new FlowLayout());
        add(lbl_channels);
        add(comb_channel);
	}
	
	public void modifyStruct(FFmpegStruct struct) throws Exception {
		String channels = (String)comb_channel.getSelectedItem();
		if (!channels.equals("Preserve original")) {
			Integer channelsInt;
			try {
				channelsInt = Integer.decode(channels);
			} catch (Exception err) {
				throw new Exception("Invalid audio channels");
			}
			if (channelsInt.intValue() < 1) {
				throw new Exception("Invalid audio channels");
			}
			struct.params.setAudioChannels(channelsInt.intValue());
		}
	}
	
}
