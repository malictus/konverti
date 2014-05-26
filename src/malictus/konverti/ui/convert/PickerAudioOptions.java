package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;

/**
 * Picker dialog for the standard audio options that are commmon to most audio formats (sample rate and channels).
 * @author Jim Halliday
 */
public class PickerAudioOptions extends PickerDialog {
	
	private JComboBox<String> comb_samplerate;
	private JComboBox<String> comb_channels;
	
	/**
	 * Initialize the audio options picker window
	 * @param struct the struct to pass through
	 */
	public PickerAudioOptions(FFmpegStruct struct) {
		super(struct);
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose audio options");
        JLabel lbl_samplerate = new JLabel("Audio sample rate (Hz):");
        comb_samplerate = new JComboBox<String>();
        comb_samplerate.addItem("Preserve original");
        comb_samplerate.addItem("11025");
        comb_samplerate.addItem("22050");
        comb_samplerate.addItem("44100");
        comb_samplerate.addItem("48000");
        comb_samplerate.addItem("96000");
        comb_samplerate.setEditable(true);
        JLabel lbl_channels = new JLabel("Number of channels:");
        comb_channels = new JComboBox<String>();
        comb_channels.addItem("Preserve original");
        comb_channels.addItem("1");
        comb_channels.addItem("2");
        comb_channels.setEditable(true);
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(lbl_samplerate);
        center_panel.add(comb_samplerate);
        center_panel.add(lbl_channels);
        center_panel.add(comb_channels);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
	}
	
	/**
	 * This should always be the last step, so take user to the end
	 */
	private void doNext() {
		String samplerate = (String)comb_samplerate.getSelectedItem();
		if (!samplerate.equals("Preserve original")) {
			try {
				Integer samplerateInt = Integer.decode(samplerate);
				if (samplerateInt.intValue() < 1000) {
					//invalid, can't move on
					return;
				}
				struct.params.setAudioSampleRate(samplerateInt.intValue());
			} catch (Exception err) {
				//not a number, can't move on
				return;
			}
		}
		String channels = (String)comb_channels.getSelectedItem();
		if (!channels.equals("Preserve original")) {
			try {
				Integer channelsInt = Integer.decode(channels);
				if (channelsInt.intValue() < 1) {
					//invalid, can't move on
					return;
				}
				struct.params.setAudioChannels(channelsInt.intValue());
			} catch (Exception err) {
				//not a number, can't move on
				return;
			}
		}
		//tests passed, FINISH
		new ConversionPanel(struct);
		setVisible(false);
        dispose();
	}

}
