package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;

/**
 * Picker dialog for OGG Vorbis options.
 * @author Jim Halliday
 */
public class PickerOGGVorbis extends PickerDialog {
	
	private JComboBox<String> comb_quality;
	
	/**
	 * Initialize the OGG options picker window
	 * @param struct the struct to pass through
	 */
	public PickerOGGVorbis(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(200, 200));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose OGG encoding options");
        comb_quality = new JComboBox<String>();
        comb_quality.addItem("0 (worst)");
        comb_quality.addItem("1");
        comb_quality.addItem("2");
        comb_quality.addItem("3");
        comb_quality.addItem("4");
        comb_quality.addItem("5");
        comb_quality.addItem("6");
        comb_quality.addItem("7");
        comb_quality.addItem("8");
        comb_quality.addItem("9");
        comb_quality.addItem("10 (best)");
        comb_quality.setSelectedItem("5");
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Choose encoding quality");
        center_panel.add(lbl_qual);
        center_panel.add(comb_quality);
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
		//this works out nicely since quality is a zero-based number
		struct.params.setAudioQuality(comb_quality.getSelectedIndex());
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
