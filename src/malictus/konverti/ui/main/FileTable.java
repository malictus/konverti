package malictus.konverti.ui.main;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import malictus.konverti.KonvertiUtils;
import malictus.konverti.examine.FFProbeExaminer;

/*
 * A drag-and-drop table that runs ffProbe on files and adds them to a list.
 */
public class FileTable extends JTable {
	
	public final static String[] COLUMN_NAMES = new String[] {"File Name", "File Type", "Duration"};
	private Vector<FFProbeExaminer> vec_files = new Vector<FFProbeExaminer>();
	
	public FileTable(DefaultTableModel model) {
		super(model);
		setFillsViewportHeight(true);
	    new FileDrop(this, new FileDrop.Listener() {   
	    	public void filesDropped(File[] files ) {   
	    		addFilesToList(files);
            } 
        });
	}
	
	private void addFilesToList(File[] files) {
		for( int i = 0; i < files.length; i++ ) {   
			File theFile = files[i];
			if (theFile.isDirectory()) {
				//recurse
				addFilesToList(theFile.listFiles());
				continue;
			}
			if (alreadyHave(theFile)) {
				continue;
			}
			if (!theFile.canRead() || !theFile.isFile()) {
				continue;
			}
			//run ffprobe on file and add to list if it is valid
			try {
				FFProbeExaminer ffprobe = new FFProbeExaminer(theFile);
				if (ffprobe.isValid()) {
					vec_files.add(ffprobe);
				} 
			} catch (Exception err) {
				continue;
			}
        } 
		redoTable();
	}
	
	/*
	 * Check current vector to see if a file is already in there
	 */
	private boolean alreadyHave(File aFile) {
		int count = 0;
		while (count < vec_files.size()) {
			if (vec_files.get(count).getFile().equals(aFile)) {
				return true;
			}
			count++;
		}
		return false;
	}
	
	/*
	 * File vector has changed, so redraw the table
	 */
	private void redoTable() {
		String[][] data = new String[vec_files.size()][FileTable.COLUMN_NAMES.length];
    	int count = 0;
    	while (count < vec_files.size()) {
    		FFProbeExaminer x = vec_files.get(count);
    		data[count][0] = x.getFile().getName();
    		data[count][1] = x.getFormat();
    		data[count][2] = KonvertiUtils.showDuration(x.getDuration());
    		count++;
    	}
    	DefaultTableModel dtm = (DefaultTableModel)this.getModel();
    	dtm.setDataVector(data, COLUMN_NAMES);
    }

}
