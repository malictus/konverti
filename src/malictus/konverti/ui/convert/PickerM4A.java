package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.ui.components.AACCodec;
import malictus.konverti.ui.components.AudioBitrate;

/**
 * Picker dialog for M4A options.
 * @author Jim Halliday
 */
public class PickerM4A extends PickerDialog {
	
	private AACCodec codec;
	private AudioBitrate bitrate;
	
	/**
	 * Initialize the M4A options picker window
	 * @param struct the struct to pass through
	 */
	public PickerM4A(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(300, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose MPEG audio encoding options");
		bitrate = new AudioBitrate();
        codec = new AACCodec();
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(codec);
        center_panel.add(bitrate);
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
		bitrate.modifyStruct(struct);
		try {
			codec.modifyStruct(struct);
		} catch (Exception err) {
			//don't continue, one of the fields has an invalid value
			return;
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
