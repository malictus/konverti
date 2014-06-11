package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;

/**
 * Picker dialog for MP4 video options. Audio options will be set later.
 * @author Jim Halliday
 */
public class PickerMP4 extends PickerDialog {
	
	private JTextField txtf_crf;
	private JComboBox<String> comb_preset;
	private JComboBox<String> comb_tuning;
	
	/**
	 * Initialize the MP4 options picker window
	 * @param struct the struct to pass through
	 */
	public PickerMP4(FFmpegStruct struct) {
		super(struct);
		this.setSize(new java.awt.Dimension(300, 160));
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose MP4 video encoding options");
		txtf_crf = new JTextField();
		txtf_crf.setMinimumSize(new Dimension(40, 16));
		txtf_crf.setPreferredSize(new Dimension(40, 16));
		txtf_crf.setText("23");
		comb_preset = new JComboBox<String>();
		comb_preset.addItem("ultrafast");
		comb_preset.addItem("superfast");
		comb_preset.addItem("veryfast");
		comb_preset.addItem("faster");
		comb_preset.addItem("fast");
		comb_preset.addItem("medium");
		comb_preset.addItem("slow");
		comb_preset.addItem("slower");
		comb_preset.addItem("veryslow");
		comb_preset.addItem("placebo");
		comb_preset.setSelectedItem("medium");
		comb_tuning = new JComboBox<String>();
		comb_tuning.addItem("<no setting>");
		comb_tuning.addItem("film");
		comb_tuning.addItem("animation");
		comb_tuning.addItem("grain");
		comb_tuning.addItem("stillimage");
		comb_tuning.addItem("psnr");
		comb_tuning.addItem("ssim");
		comb_tuning.addItem("fastdecode");
		comb_tuning.addItem("zerolatency");
		comb_tuning.setSelectedIndex(0);
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new BoxLayout(center_panel, BoxLayout.Y_AXIS));
        JPanel pnl_crf = new JPanel();
        pnl_crf.setLayout(new FlowLayout());
        JLabel lbl_crf = new JLabel("Constant Rate Factor (0 - 51) [0 is best]");
        pnl_crf.add(lbl_crf);
        pnl_crf.add(txtf_crf);
        center_panel.add(pnl_crf);
        JPanel pnl_qual = new JPanel();
        pnl_qual.setLayout(new FlowLayout());
        JLabel lbl_qual = new JLabel("Preset");
        pnl_qual.add(lbl_qual);
        pnl_qual.add(comb_preset);
        center_panel.add(pnl_qual);
        JPanel pnl_tune = new JPanel();
        pnl_tune.setLayout(new FlowLayout());
        JLabel lbl_tune = new JLabel("Tuning");
        pnl_tune.add(lbl_tune);
        pnl_tune.add(comb_tuning);
        center_panel.add(pnl_tune);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
	}
	
	/**
	 * Go to next step in the process
	 */
	private void doNext() {
		Integer i = null;
		boolean nope = false;
		try {
			i = new Integer(txtf_crf.getText());
		} catch (Exception err) {
			nope = true;
		}
		if (nope == false) {
			if ((i != null) && (i.intValue() >= 0) && (i.intValue() <= 51)) {
				struct.params.setConstantRateFactor(i.intValue());
			} else {
				nope = true;
			}
		}
		if (nope == true) {
			JOptionPane.showMessageDialog(this, "You must enter an integer between 0 and 51 in the constant rate factor text field", 
					"Incorrect Constant Rate Factor value", JOptionPane.ERROR_MESSAGE);
			return;
		}
		struct.params.setPreset((String)comb_preset.getSelectedItem());
		if (!comb_tuning.getSelectedItem().equals("<no setting>")) {
			struct.params.setTuning((String)comb_tuning.getSelectedItem());
		}
		new PickerM4A(struct);
		setVisible(false);
        dispose();
	}

}
