package malictus.konverti.ui.main;

import java.awt.*;
import javax.swing.*;
import malictus.konverti.*;

public class ConversionPanel extends JFrame {
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;
	private JPanel contentPanel;
    
	/*
	 * Initialize the conversion window
	 */
	public ConversionPanel() {
		super();
		setTitle("Konverti " + KonvertiMain.VERSION + " -- File Conversion in Process");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        contentPanel = new JPanel();        
        /*********************************/
        /** set up components on screen **/
        /*********************************/
        contentPanel.setLayout(new BorderLayout());
        
        //finalize
        contentPanel.setOpaque(true); 
        setContentPane(contentPanel);
        setResizable(true);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));;
        setSize(WIDTH, HEIGHT);
        //center on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
        	frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
        	frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
	}

}
