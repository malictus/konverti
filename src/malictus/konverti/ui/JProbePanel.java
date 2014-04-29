package malictus.konverti.ui;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class JProbePanel extends JPanel {
	
	MainFrame parent;
	JLabel lbl_ffDuration;
	JLabel lbl_ffFormat;
	
	//TODO: show stream data
	
	public JProbePanel(MainFrame parent) {
		super();
		this.parent = parent;
		TitledBorder bdrJProbe = BorderFactory.createTitledBorder(null, "FFmpeg");
        setBorder(bdrJProbe);
        setPreferredSize(new Dimension(680, 100));
        BoxLayout layout_Jprobepanel = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout_Jprobepanel);
        lbl_ffDuration = new JLabel("Duration:");
        add(lbl_ffDuration);
        lbl_ffFormat = new JLabel("Format:");
        add(lbl_ffFormat);
	}
	
	protected void setDuration(String duration) {
		//TODO turn this into a utility method in Konverti utils
		float seconds = new Float(duration);
		long hours = TimeUnit.SECONDS.toHours((long)seconds);
		long minutes = TimeUnit.SECONDS.toMinutes((long)seconds) - (TimeUnit.SECONDS.toHours((long)seconds)* 60);
		seconds = seconds - (TimeUnit.SECONDS.toMinutes((long)seconds) *60);
		if (hours > 0) {
			String minuteString = "";
			if (minutes < 10) {
				minuteString = "0" + minutes;
			} else {
				minuteString = "" + minutes;
			}
			lbl_ffDuration.setText("Duration: " + hours + ":" + minuteString + ":" + seconds);
		} else {
			lbl_ffDuration.setText("Duration: " + minutes + ":" + seconds);
		}
	}
	
	protected void setFormat(String format) {
		lbl_ffFormat.setText("Format: " + format);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		lbl_ffDuration.setEnabled(enabled);
		if (!enabled) {
			lbl_ffDuration.setText("Duration:");
		}
		lbl_ffFormat.setEnabled(enabled);
		if (!enabled) {
			lbl_ffFormat.setText("Format:");
		}
	}

}
