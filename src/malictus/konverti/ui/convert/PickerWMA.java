package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.ui.components.AudioBitrate;
import malictus.konverti.ui.components.WMACodec;

/**
 * Picker dialog for WMA options.
 * @author Jim Halliday
 */
public class PickerWMA extends PickerDialog {
	
	private WMACodec codec;
	private AudioBitrate bitrate;
	
	/**
	 * Initialize the WMA options picker window
	 * @param struct the struct to pass through
	 */
	public PickerWMA(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose Windows Media Audio encoding options");
		bitrate = new AudioBitrate();
        codec = new WMACodec();
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
