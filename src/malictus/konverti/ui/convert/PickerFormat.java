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
        //entry 0 - WAV audio (8 bit) (always unsigned, even though other wav files are signed)
        if (KonvertiUtils.encoderIsPreset("pcm_u8")) {
        	comb_format.addItem("WAV audio (8-bit)");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio (8-bit)");
        }
        //entry 1 - WAV audio (16 bit)
        if (KonvertiUtils.encoderIsPreset("pcm_s16le")) {
        	comb_format.addItem("WAV audio (16-bit)");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio (16-bit)");
        }
        //entry 2 - WAV audio (24 bit)
        if (KonvertiUtils.encoderIsPreset("pcm_s24le")) {
        	comb_format.addItem("WAV audio (24-bit)");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio (24-bit)");
        }
        //entry 3 - WAV audio (32 bit)
        if (KonvertiUtils.encoderIsPreset("pcm_s32le")) {
        	comb_format.addItem("WAV audio (32-bit)");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio (32-bit)");
        }
        //entry 4 - WAV audio (32 bit floating)
        if (KonvertiUtils.encoderIsPreset("pcm_f32le")) {
        	comb_format.addItem("WAV audio (32-bit floating)");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio (32-bit floating)");
        }
        //entry 5 - WAV audio (64 bit floating)
        if (KonvertiUtils.encoderIsPreset("pcm_f64le")) {
        	comb_format.addItem("WAV audio (64-bit floating)");
        } else {
        	comb_format.addItem("(Missing encoder) - WAV audio (64-bit floating)");
        }
        //entry 6 - mp3 audio
        if (KonvertiUtils.encoderIsPreset("libmp3lame")) {
        	comb_format.addItem("MP3 audio");
        } else {
        	comb_format.addItem("(Missing encoder) - MP3 audio");
        }
        //entry 7 - ogg vorbis audio
        if (KonvertiUtils.encoderIsPreset("libvorbis")) {
        	comb_format.addItem("OGG Vorbis audio");
        } else {
        	comb_format.addItem("(Missing encoder) - OGG Vorbis audio");
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
			//WAV 8 bit
			struct.params.setAudioEncodingCodec("pcm_u8");
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 1) {
			//WAV 16 bit
			struct.params.setAudioEncodingCodec("pcm_s16le");
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 2) {
			//WAV 24 bit
			struct.params.setAudioEncodingCodec("pcm_s24le");
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 3) {
			//WAV 32 bit
			struct.params.setAudioEncodingCodec("pcm_s32le");
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 4) {
			//WAV 32 bit float
			struct.params.setAudioEncodingCodec("pcm_f32le");
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 5) {
			//WAV 64 bit float
			struct.params.setAudioEncodingCodec("pcm_f64le");
			struct.params.setAudioOnly(true);
			struct.extension = "wav";
			new PickerAudioOptions(struct);
		} else if (comb_format.getSelectedIndex() == 6) {
			//MP3
			struct.params.setAudioEncodingCodec("libmp3lame");
			struct.params.setAudioOnly(true);
			struct.extension = "mp3";
			new PickerMP3(struct);
		} else if (comb_format.getSelectedIndex() == 7) {
			//OGG Vorbis
			struct.params.setAudioEncodingCodec("libvorbis");
			struct.params.setAudioOnly(true);
			struct.extension = "ogg";
			new PickerOGGVorbis(struct);
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
