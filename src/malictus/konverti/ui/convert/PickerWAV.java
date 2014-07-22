package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.ui.components.WAVCodec;

/**
 * Picker dialog for WAV options.
 * @author Jim Halliday
 */
public class PickerWAV extends PickerDialog {
	
	private WAVCodec codec;
	
	/**
	 * Initialize the WAV options picker window
	 * @param struct the struct to pass through
	 */
	public PickerWAV(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(300, 140));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose WAV audio zencoding options");
		codec = new WAVCodec();
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(codec);
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
