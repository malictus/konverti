package malictus.konverti.ui.main;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import malictus.konverti.KonvertiUtils;
import malictus.konverti.examine.FFProbeExaminer;

/**
 * A drag-and-drop table that runs ffProbe on files and adds them to a list.
 * @author Jim Halliday
 */
public class FileTable extends JTable {
	
	public final static String[] COLUMN_NAMES = new String[] {"File Name", "File Type", "Duration"};
	private List<FFProbeExaminer> vec_files = new Vector<FFProbeExaminer>();
	private MainPanel parentPanel;
	private int count_files;
	private int good_files;
	private boolean cancel_trigger = false;
	private boolean thread_is_running = false;
	
	/**
	 * Create a FileTable object
	 * @param model the table model to use for this table. The table model can be initialized with a simple, empty DefaultTableModel object.
	 * @param parentPanel the parent panel for this table
	 */
	public FileTable(DefaultTableModel model, MainPanel parentPanel) {
		super(model);
		this.parentPanel = parentPanel;
		setFillsViewportHeight(true);
		fixColumnWidths();
		final MainPanel PARENT_PANEL = parentPanel;
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
	        	PARENT_PANEL.updateTheUI(getSelectedRowCount(), getRowCount());
	        }
	    });
	}
	
	/**
	 * Override from JTable to disallow user edits to fields
	 */
	public boolean isCellEditable(int row, int column) {                
        return false;               
	};
	
	/**
	 * Get method for the list of files
	 * @return the list of files that are currently in this table
	 */
	protected List<FFProbeExaminer> getFFProbeFiles() {
		return vec_files;
	}
	
	/**
	 * Remove all files from the file list and update UI
	 */
	protected void removeAllFiles() {
		this.vec_files.clear();
		redoTable();
		parentPanel.setStatus("removed all files from file list");
	}
	
	/**
	 * Remove selected files from the file list and update UI
	 */
	protected void removeSelectedFiles() {
		int[] selected = this.getSelectedRows();
		if (selected.length < 1) {
			return;
		}
		for( int i = selected.length - 1; i >= 0; i-- ) {   
			this.vec_files.remove(selected[i]);
		}
		redoTable();
		parentPanel.setStatus("removed " + selected.length + " files from file list");
	}
	
	/**
	 * Cancel button has been pressed while files are processing. Stop process and clean up UI.
	 */
	protected void handleCancel() {
		if (thread_is_running) {
			cancel_trigger = true;
		}
	}
	
	/**
	 * Make the duration column small and fixed width
	 */
	private void fixColumnWidths() {
		setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        TableColumn durationColumn = getColumn(FileTable.COLUMN_NAMES[2]);
        durationColumn.setMinWidth(80);
        durationColumn.setMaxWidth(80);
	}
	
	/**
	 * Called when files are dropped onto this table or the parent scroll pane
	 * @param files the array of files that have been dropped onto the table
	 */
	protected void handleFileDrop(File[] files) {
		if (!this.isEnabled()) {
			return;
		}
		//tell parent we're starting this
		parentPanel.turnOffInterface();
		//start the thread to do the actual file examination
		final File[] theFiles = files;
		Runnable q = new Runnable() {
			public void run() {
				startFileExamine(theFiles);
		    }
		};
		Thread t = new Thread(q);
		t.start();
	}
	
	/**
	 * Called from within the file examine thread. This examines and processes the files with FFprobe.
	 * @param theFiles
	 */
	private void startFileExamine(File[] theFiles) {
		thread_is_running = true;
		parentPanel.btn_cancel.setEnabled(true);
		parentPanel.setStatus("counting files");
		int numFiles = KonvertiUtils.countFiles(theFiles);
		parentPanel.setStatus("total number of files to process: " + numFiles);
		count_files = 0;
		good_files = 0;
		addFilesToList(theFiles, numFiles);
		if (good_files > 0) {
			Collections.sort(vec_files);
			redoTable();
		}
		cancel_trigger = false;
		if (good_files == 0) {
			parentPanel.setStatus("finished processing. No new valid files added to list.");
		} else if (good_files == 1) {
			parentPanel.setStatus("finished processing. " + good_files + " valid file added to list.");
		} else {
			parentPanel.setStatus("finished processing. " + good_files + " valid files added to list.");
		}
		parentPanel.btn_cancel.setEnabled(false);
		thread_is_running = false;
		parentPanel.turnOnInterface();
	}
	
	/**
	 * Recursive method to add files to the list of files if they quality
	 * @param files the files to add to the list
	 * @param numFiles the total number of files to be processed, so that the UI can show progress
	 */
	private void addFilesToList(File[] files, final int numFiles) {
		for( int i = 0; i < files.length; i++ ) {   
			if (cancel_trigger) {
				return;
			}
			File theFile = files[i];
			if (theFile.isDirectory()) {
				//recurse
				addFilesToList(theFile.listFiles(), numFiles);
				continue;
			}
			count_files++;
			parentPanel.setStatus("processing file " + count_files + " of " + numFiles + " -- " + good_files + " valid files added to list so far");
			if (alreadyHave(theFile)) {
				continue;
			}
			if (!theFile.canRead() || !theFile.isFile()) {
				continue;
			}
			if (KonvertiUtils.filenameIsInBlackList(theFile.getName())) {
				continue;
			}
			//run ffprobe on file and add to list if it is valid
			try {
				FFProbeExaminer ffprobe = new FFProbeExaminer(theFile);
				if (ffprobe.isValid()) {
					vec_files.add(ffprobe);
					good_files++;
				} 
			} catch (Exception err) {
				continue;
			}
        } 
	}
	
	/**
	 * Check the current vector of files to process to see if a file is already in there
	 * @param aFile the new file to check
	 * @return true if this file is already in the vector of files
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
	
	/**
	 * Called after editing the files to be processed list, this updates the UI accordingly.
	 */
	private void redoTable() {
		//sort files
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
    	this.fixColumnWidths();
    	parentPanel.updateTheUI(getSelectedRowCount(), getRowCount());
    }

}
