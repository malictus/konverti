package malictus.konverti.ui.main;

import javax.swing.*;
import javax.swing.table.*;

public class FileTable extends JScrollPane {
	
	private MainPanel parent;
	
	public FileTable(MainPanel parent) {
		this.parent = parent;
	    JTable tbl_file = new JTable(new FileTableModel());
	    this.getViewport().setView(tbl_file);
	    tbl_file.setFillsViewportHeight(true);
	    new FileDrop(tbl_file, new FileDrop.Listener() {   
	    	public void filesDropped( java.io.File[] files ) {   
	    		for( int i = 0; i < files.length; i++ ) {   
	    			try {   
	    				System.out.println(files[i].getCanonicalPath() + "\n" );
                    }  
                    catch( java.io.IOException e ) {}
                } 
            } 
        });
	}
	
	public MainPanel getParent() {
		return parent;
	}
	
	private class FileTableModel extends AbstractTableModel {
        private String[] columnNames = {"File Name", "File Type"};
        private Object[][] data = {};
 
        public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return data.length;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
    }
	
	

}
