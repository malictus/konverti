package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;

/**
 * Picker dialog for the standard video options that are commmon to most video formats.
 * @author Jim Halliday
 */
public class PickerVideoOptions extends PickerDialog {
	
	private JCheckBox chk_scale;
	private JTextField txt_scale_width;
	private JTextField txt_scale_height;
	private JLabel lbl_scale;
	private JLabel lbl_width;
	private JLabel lbl_height;
	
	/**
	 * Initialize the video options picker window
	 * @param struct the struct to pass through
	 */
	public PickerVideoOptions(FFmpegStruct struct) {
		super(struct);
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose video options");
		this.setSize(new java.awt.Dimension(400, 120));
		chk_scale = new JCheckBox("Rescale Video");
		chk_scale.setSelected(false);
		chk_scale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chk_scale.isSelected()) {
            		txt_scale_width.setEnabled(true);
            		txt_scale_height.setEnabled(true);
                    lbl_scale.setEnabled(true);
                    lbl_width.setEnabled(true);
                    lbl_height.setEnabled(true);
                } else {
                	txt_scale_width.setEnabled(false);
            		txt_scale_height.setEnabled(false);
                    lbl_scale.setEnabled(false);
                    lbl_width.setEnabled(false);
                    lbl_height.setEnabled(false);
                }
            }
        }); 
		txt_scale_width = new JTextField();
		txt_scale_width.setEnabled(false);
		txt_scale_width.setMinimumSize(new Dimension(40, 20));
		txt_scale_width.setPreferredSize(new Dimension(40, 20));
		txt_scale_height = new JTextField();
		txt_scale_height.setEnabled(false);
		txt_scale_height.setMinimumSize(new Dimension(40, 20));
		txt_scale_height.setPreferredSize(new Dimension(40, 20));
        lbl_scale = new JLabel("Specify only one parameter (width or height) to maintain aspect ratio.");
        lbl_scale.setEnabled(false);
        lbl_height = new JLabel("HEIGHT:");
        lbl_height.setEnabled(false);
        lbl_width = new JLabel("WIDTH:");
        lbl_width.setEnabled(false);
        
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(chk_scale);
        center_panel.add(lbl_width);
        center_panel.add(txt_scale_width);
        center_panel.add(lbl_height);
        center_panel.add(txt_scale_height);
        center_panel.add(lbl_scale);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
	}
	
	/**
	 * Proceed to audio options based on extension
	 */
	private void doNext() {
		if (this.chk_scale.isSelected()) {
			//first figure out width
			int width = 0;
			if (this.txt_scale_width.getText().trim().equals("")) {
				width = -1;
			} else {
				try {
					width = Integer.decode(this.txt_scale_width.getText());
				} catch (Exception err) {
					//ignore
					width = 0;
				}
			}
			if ((width < -1) || (width == 0)) {
				//invalid number; abort
				return;
			}
			//fix for pixel problem
			if (width > 0 && (width % 2) == 1) {
				width++;
			}
			//now figure out height
			int height = 0;
			if (this.txt_scale_height.getText().trim().equals("")) {
				height = -1;
			} else {
				try {
					height = Integer.decode(this.txt_scale_height.getText());
				} catch (Exception err) {
					//ignore
					height = 0;
				}
			}
			if ((height < -1) || (height == 0)) {
				//invalid number; abort
				return;
			}
			if (height > 0 && (height % 2) == 1) {
				height++;
			}
			//make sure at least one of them isn't -1
			if ((height == -1) && (width == -1)) {
				return;
			}
			struct.params.setResize(true);
			struct.params.setWidth(width);
			struct.params.setHeight(height);	
		}
		if (struct.extension.equals("mp4")) {
			new PickerM4A(struct);
		} else {
			//default, but shouldn't ever happen if all video settings are accounted for
			new PickerAudioOptions(struct);
		}
		setVisible(false);
        dispose();
	}

}
