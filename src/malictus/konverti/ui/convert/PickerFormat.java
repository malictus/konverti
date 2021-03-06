package malictus.konverti.ui.convert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import malictus.konverti.FFmpegStruct;
import malictus.konverti.KonvertiUtils;

/**
 * A simple dialog that allows the user to pick a target format (file extension) and start the process of a custom convert.
 * @author Jim Halliday
 */
public class PickerFormat extends PickerDialog {
	
	private JComboBox<String> comb_format;
	
	/**
	 * Initialize the format picker window
	 * @param struct the struct object to pass through
	 */
	public PickerFormat(FFmpegStruct struct) {
		super(struct);
		/*********************************/
        /** set up components on screen **/
        /*********************************/
		setTitle("Choose an output format");
        JLabel lbl_format = new JLabel("Please choose the target file format for export.");
        comb_format = new JComboBox<String>();
        //entry 0 - WAV audio
        comb_format.addItem("WAV audio");
        //entry 1 - AIFF audio
        comb_format.addItem("AIFF audio");
        //entry 2 - mp3 audio
        if (KonvertiUtils.encoderIsPreset("libmp3lame")) {
        	comb_format.addItem("MP3 audio");
        } else {
        	comb_format.addItem("(Missing encoder) - MP3 audio");
        }
        //entry 3 - ogg vorbis audio
        if ( (KonvertiUtils.encoderIsPreset("libvorbis")) || (KonvertiUtils.encoderIsPreset("vorbis"))
        		|| (KonvertiUtils.encoderIsPreset("flac"))) {
        	comb_format.addItem("OGG Vorbis audio");
        } else {
        	comb_format.addItem("(Missing encoder) - OGG Vorbis audio");
        }
        //entry 4 - flac
        if (KonvertiUtils.encoderIsPreset("flac")) {
        	comb_format.addItem("FLAC audio");
        } else {
        	comb_format.addItem("(Missing encoder) - FLAC audio");
        }
        //entry 5 - m4a
        comb_format.addItem("M4A (MPEG) audio");
        //entry 6 - wma
        if ((KonvertiUtils.encoderIsPreset("wmav1")) || (KonvertiUtils.encoderIsPreset("wmav2"))) {
        	comb_format.addItem("Windows Media Audio (WMA)");
        } else {
        	comb_format.addItem("(Missing encoder) - Windows Media Audio (WMA)");
        }
        //entry 7 - mp4
        if (KonvertiUtils.encoderIsPreset("libx264")) {
        	comb_format.addItem("MP4 Video");
        } else {
        	comb_format.addItem("(Missing encoder) - MP4 Video");
        }
        comb_format.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                updateTheUI();
            }
        });
        JPanel center_panel = new JPanel();
        center_panel.setLayout(new FlowLayout());
        center_panel.add(lbl_format);
        center_panel.add(comb_format);
        btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        }); 
        contentPanel.add(center_panel, BorderLayout.CENTER);
        updateTheUI();
	}
	
	/**
	 * Go to next step in process
	 */
	private void doNext() {		
		if (comb_format.getSelectedIndex() == 0) {
			//WAV
			//codec will be set later
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerWAV(struct);
		} else if (comb_format.getSelectedIndex() == 1) {
			//AIFF
			//codec will be set later
			struct.params.setAudioOnly(true);
			struct.extension = "aiff";
			new PickerAIFF(struct);
		} else if (comb_format.getSelectedIndex() == 2) {
			//MP3
			struct.params.setAudioEncodingCodec("libmp3lame");
			struct.params.setAudioOnly(true);
			struct.extension = "mp3";
			new PickerMP3(struct);
		} else if (comb_format.getSelectedIndex() == 3) {
			//OGG Vorbis
			struct.params.setAudioOnly(true);
			struct.extension = "ogg";
			new PickerOGGVorbis(struct);
		} else if (comb_format.getSelectedIndex() == 4) {
			//FLAC
			struct.params.setAudioOnly(true);
			struct.extension = "flac";
			struct.params.setAudioEncodingCodec("flac");
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 5) {
			//M4A
			struct.params.setAudioOnly(true);
			struct.extension = "m4a";
			new PickerM4A(struct);
		} else if (comb_format.getSelectedIndex() == 6) {
			//WMA
			struct.params.setAudioOnly(true);
			struct.extension = "wma";
			new PickerWMA(struct);
		} else if (comb_format.getSelectedIndex() == 7) {
			//MP4
			struct.extension = "mp4";
			struct.params.setVideoEncodingCodec("libx264");
			new PickerMP4(struct);
		}
		setVisible(false);
        dispose();
	}
	
	/**
	 * Update the UI based on whether the format is available or not
	 */
	private void updateTheUI() {
		if (((String)comb_format.getSelectedItem()).startsWith("(Missing encoder")) {
			btn_next.setEnabled(false);
		} else {
			btn_next.setEnabled(true);
		}
	}

}
