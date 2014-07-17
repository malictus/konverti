package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.ui.components.AIFFCodec;

/**
 * Picker dialog for AIFF options.
 * @author Jim Halliday
 */
public class PickerAIFF extends PickerDialog {
	
	private AIFFCodec aiff_codec;
	
	/**
	 * Initialize the AIFF options picker window
	 * @param struct the struct to pass through
	 */
	public PickerAIFF(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose AIFF encoding options");
		aiff_codec = new AIFFCodec();
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(aiff_codec, BorderLayout.CENTER);
	}
	
	/**
	 * Next step after this is general audio options dialog
	 */
	private void doNext() {
		try {
			aiff_codec.modifyStruct(struct);
		} catch (Exception err) {
			//don't continue, one of the fields has an invalid value
			return;
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
