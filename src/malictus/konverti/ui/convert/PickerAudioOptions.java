package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.ui.components.*;

/**
 * Picker dialog for the standard audio options that are commmon to most audio formats (sample rate and channels).
 * @author Jim Halliday
 */
public class PickerAudioOptions extends PickerDialog {
	
	private AudioSampleRate audio_sample_rate;
	private AudioChannels audio_channel;
	
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
		audio_sample_rate = new AudioSampleRate();
        audio_channel = new AudioChannels();
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(audio_sample_rate);
        center_panel.add(audio_channel);
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
		audio_sample_rate.modifyStruct(struct);
		audio_channel.modifyStruct(struct);
		new ConversionPanel(struct);
		setVisible(false);
        dispose();
	}

}
