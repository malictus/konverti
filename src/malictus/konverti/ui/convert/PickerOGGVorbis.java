package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * Picker dialog for OGG Vorbis options.
 * @author Jim Halliday
 */
public class PickerOGGVorbis extends PickerDialog {
	
	private JComboBox<String> comb_quality;
	private JComboBox<String> comb_codec;
	
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
        comb_codec = new JComboBox<String>();
        if (KonvertiUtils.encoderIsPreset("libvorbis")) {
        	comb_codec.addItem("libvorbis");
        } else {
        	comb_codec.addItem("(Missing encoder) - libvorbis");
        }
        if (KonvertiUtils.encoderIsPreset("vorbis")) {
        	comb_codec.addItem("vorbis (experimental)");
        } else {
        	comb_codec.addItem("(Missing encoder) - vorbis");
        }   
        if (KonvertiUtils.encoderIsPreset("flac")) {
        	comb_codec.addItem("flac");
        } else {
        	comb_codec.addItem("(Missing encoder) - flac");
        }   
        comb_codec.setSelectedIndex(0);
        comb_codec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if (comb_codec.getSelectedIndex() < 2) {
                	comb_quality.setEnabled(true);
                } else {
                	comb_quality.setEnabled(false);
                }
            }
        });
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        JLabel lbl_codec = new JLabel("Choose codec");
        center_panel.add(lbl_codec);
        center_panel.add(comb_codec);
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
		if (comb_codec.getSelectedItem().equals("libvorbis")) {
			struct.params.setAudioEncodingCodec("libvorbis");
		} else if (comb_codec.getSelectedItem().equals("vorbis (experimental)")) {
			struct.params.setAudioEncodingCodec("vorbis");
			struct.params.setUseExperimental(true);
		} else if (comb_codec.getSelectedItem().equals("flac")) {
			struct.params.setAudioEncodingCodec("flac");
		} else {
			//user chose invalid codec
			return;
		}
		new PickerAudioOptions(struct);
		setVisible(false);
        dispose();
	}

}
