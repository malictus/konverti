package malictus.konverti.ui.main;

import java.awt.datatransfer.DataFlavor;
import java.io.*;

/**
 * Adapted from http://iharder.sourceforge.net/current/java/filedrop/
 * which is in the public domain. Thanks!
 */

public class FileDrop {
	
    private transient javax.swing.border.Border normalBorder;
    private transient java.awt.dnd.DropTargetListener dropListener;
    
    /** Discover if the running JVM is modern enough to have drag and drop. */
    private static Boolean supportsDnD;
    
    // Default border color
    private static java.awt.Color defaultBorderColor = new java.awt.Color( 0f, 0f, 1f, 0.25f );
    
    public FileDrop(final java.awt.Component c, final Listener listener ) {   
    	this( c, javax.swing.BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor ), false, listener );
    }   // end constructor
    
    public FileDrop(final java.awt.Component c, final javax.swing.border.Border dragBorder, final boolean recursive, final Listener listener) {   
        
        if( supportsDnD() )
        {   // Make a drop listener
            dropListener = new java.awt.dnd.DropTargetListener()
            {   public void dragEnter( java.awt.dnd.DropTargetDragEvent evt )
                {      

                    // Is this an acceptable drag event?
                    if( isDragOk(  evt ) )
                    {
                        // If it's a Swing component, set its border
                        if( c instanceof javax.swing.JComponent )
                        {   javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            normalBorder = jc.getBorder();
                            jc.setBorder( dragBorder );
                        }   // end if: JComponent   

                        // Acknowledge that it's okay to enter
                        //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY );
                    }   // end if: drag ok
                    else 
                    {   // Reject the drag event
                        evt.rejectDrag();
                    }   // end else: drag not ok
                }   // end dragEnter

                public void dragOver( java.awt.dnd.DropTargetDragEvent evt ) 
                {   // This is called continually as long as the mouse is
                    // over the drag target.
                }   // end dragOver

                @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
				public void drop( java.awt.dnd.DropTargetDropEvent evt )
                {   
                    try
                    {   // Get whatever was dropped
                        java.awt.datatransfer.Transferable tr = evt.getTransferable();

                        // Is it a file list?
                        if (tr.isDataFlavorSupported (java.awt.datatransfer.DataFlavor.javaFileListFlavor))
                        {
                            // Say we'll take it.
                            //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                            evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY );

                            // Get a useful list
                            java.util.List fileList = (java.util.List) 
                                tr.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                            java.util.Iterator iterator = fileList.iterator();

                            // Convert list to array
                            java.io.File[] filesTemp = new java.io.File[ fileList.size() ];
                            fileList.toArray( filesTemp );
                            final java.io.File[] files = filesTemp;

                            // Alert listener to drop.
                            if( listener != null )
                                listener.filesDropped( files );

                            // Mark that drop is completed.
                            evt.getDropTargetContext().dropComplete(true);
                        }   // end if: file list
                        else // this section will check for a reader flavor.
                        {
                            // Thanks, Nathan!
                            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (int zz = 0; zz < flavors.length; zz++) {
                                if (flavors[zz].isRepresentationClassReader()) {
                                    // Say we'll take it.
                                    //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                                    evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);

                                    Reader reader = flavors[zz].getReaderForText(tr);

                                    BufferedReader br = new BufferedReader(reader);
                                    
                                    if(listener != null)
                                        listener.filesDropped(createFileArray(br));
                                    
                                    // Mark that drop is completed.
                                    evt.getDropTargetContext().dropComplete(true);
                                    handled = true;
                                    break;
                                }
                            }
                            if(!handled){
                                evt.rejectDrop();
                            }
                        }   // end else: not a file list
                    }   // end try
                    catch ( java.io.IOException io) 
                    {  
                        evt.rejectDrop();
                    }   // end catch IOException
                    catch (java.awt.datatransfer.UnsupportedFlavorException ufe) 
                    {  
                        evt.rejectDrop();
                    }   // end catch: UnsupportedFlavorException
                    finally
                    {
                        // If it's a Swing component, reset its border
                        if( c instanceof javax.swing.JComponent )
                        {   javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            jc.setBorder( normalBorder );
                        }   // end if: JComponent
                    }   // end finally
                }   // end drop

                public void dragExit( java.awt.dnd.DropTargetEvent evt ) 
                {  
                    // If it's a Swing component, reset its border
                    if( c instanceof javax.swing.JComponent )
                    {   javax.swing.JComponent jc = (javax.swing.JComponent) c;
                        jc.setBorder( normalBorder );
                    }   // end if: JComponent
                }   // end dragExit

                public void dropActionChanged( java.awt.dnd.DropTargetDragEvent evt ) 
                {  
                    // Is this an acceptable drag event?
                    if( isDragOk( evt ) )
                    {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY );
                        
                    }   // end if: drag ok
                    else 
                    {   evt.rejectDrag();
                    }   // end else: drag not ok
                }   // end dropActionChanged
            }; // end DropTargetListener

            // Make the component (and possibly children) drop targets
            makeDropTarget( c, recursive );
        }   // end if: supports dnd
    }   // end constructor

    
    @SuppressWarnings("rawtypes")
	private static boolean supportsDnD()
    {   // Static Boolean
        if( supportsDnD == null )
        {   
            boolean support = false;
            try
            {   @SuppressWarnings("unused")
			Class arbitraryDndClass = Class.forName( "java.awt.dnd.DnDConstants" );
                support = true;
            }   // end try
            catch( Exception e )
            {   support = false;
            }   // end catch
            supportsDnD = new Boolean( support );
        }   // end if: first time through
        return supportsDnD.booleanValue();
    }   // end supportsDnD
    
    
     // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
     private static String ZERO_CHAR_STRING = "" + (char)0;
     @SuppressWarnings({ "unchecked", "rawtypes" })
	private static File[] createFileArray(BufferedReader bReader)
     {
        try { 
            java.util.List list = new java.util.ArrayList();
            java.lang.String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    // kde seems to append a 0 char to the end of the reader
                    if(ZERO_CHAR_STRING.equals(line)) continue; 
                    
                    java.io.File file = new java.io.File(new java.net.URI(line));
                    list.add(file);
                } catch (Exception ex) {
                   
                }
            }

            return (java.io.File[]) list.toArray(new File[list.size()]);
        } catch (IOException ex) {
          
        }
        return new File[0];
     }
     // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
     
    
    private void makeDropTarget( final java.awt.Component c, boolean recursive )
    {
        // Make drop target
        final java.awt.dnd.DropTarget dt = new java.awt.dnd.DropTarget();
        try
        {   dt.addDropTargetListener( dropListener );
        }   // end try
        catch( java.util.TooManyListenersException e )
        {   e.printStackTrace();
        }   // end catch
        
        // Listen for hierarchy changes and remove the drop target when the parent gets cleared out.
        c.addHierarchyListener( new java.awt.event.HierarchyListener()
        {   public void hierarchyChanged( java.awt.event.HierarchyEvent evt )
            {  
                java.awt.Component parent = c.getParent();
                if( parent == null )
                {   c.setDropTarget( null );
                }   // end if: null parent
                else
                {   new java.awt.dnd.DropTarget(c, dropListener);
                }   // end else: parent not null
            }   // end hierarchyChanged
        }); // end hierarchy listener
        if( c.getParent() != null )
            new java.awt.dnd.DropTarget(c, dropListener);
        
        if( recursive && (c instanceof java.awt.Container ) )
        {   
            // Get the container
            java.awt.Container cont = (java.awt.Container) c;
            
            // Get it's components
            java.awt.Component[] comps = cont.getComponents();
            
            // Set it's components as listeners also
            for( int i = 0; i < comps.length; i++ )
                makeDropTarget(comps[i], recursive );
        }   // end if: recursively set components as listener
    }   // end dropListener
    
    /** Determine if the dragged data is a file list. */
    private boolean isDragOk( final java.awt.dnd.DropTargetDragEvent evt )
    {   boolean ok = false;
        
        // Get data flavors being dragged
        java.awt.datatransfer.DataFlavor[] flavors = evt.getCurrentDataFlavors();
        
        // See if any of the flavors are a file list
        int i = 0;
        while( !ok && i < flavors.length )
        {   
            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            // Is the flavor a file list?
            final DataFlavor curFlavor = flavors[i];
            if( curFlavor.equals( java.awt.datatransfer.DataFlavor.javaFileListFlavor ) ||
                curFlavor.isRepresentationClassReader()){
                ok = true;
            }
            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            i++;
        }   // end while: through flavors
        
        return ok;
    }  

    public static boolean remove( java.awt.Component c)
    {   return remove(c, true );
    }   // end remove
    
    public static boolean remove( java.awt.Component c, boolean recursive )
    {   // Make sure we support dnd.
        if( supportsDnD() )
        { 
            c.setDropTarget( null );
            if( recursive && ( c instanceof java.awt.Container ) )
            {   java.awt.Component[] comps = ((java.awt.Container)c).getComponents();
                for( int i = 0; i < comps.length; i++ )
                    remove( comps[i], recursive );
                return true;
            }   // end if: recursive
            else return false;
        }   // end if: supports DnD
        else return false;
    }   // end remove
    
/* ********  I N N E R   I N T E R F A C E   L I S T E N E R  ******** */    
    
    public static interface Listener {
       
        public abstract void filesDropped( java.io.File[] files );
        
    }   // end inner-interface Listener
    
    
/* ********  I N N E R   C L A S S  ******** */    
    
    
    /**
     * This is the event that is passed to the
     * {@link FileDropListener#filesDropped filesDropped(...)} method in
     * your {@link FileDropListener} when files are dropped onto
     * a registered drop target.
     *
     * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
     * 
     * @author  Robert Harder
     * @author  rob@iharder.net
     * @version 1.2
     */
    public static class Event extends java.util.EventObject {

        private java.io.File[] files;

    
        public Event( java.io.File[] files, Object source ) {
            super( source );
            this.files = files;
        }   // end constructor

       
        public java.io.File[] getFiles() {
            return files;
        }   // end getFiles
    
    }   // end inner class Event
    
    
    
/* ********  I N N E R   C L A S S  ******** */
   
    public static class TransferableObject implements java.awt.datatransfer.Transferable
    {
       
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";

        public final static java.awt.datatransfer.DataFlavor DATA_FLAVOR = 
            new java.awt.datatransfer.DataFlavor( FileDrop.TransferableObject.class, MIME_TYPE );


        private Fetcher fetcher;
        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor; 


        public TransferableObject( Object data )
        {   this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor( data.getClass(), MIME_TYPE );
        }   // end constructor

      
        public TransferableObject( Fetcher fetcher )
        {   this.fetcher = fetcher;
        }   // end constructor

        public TransferableObject( @SuppressWarnings("rawtypes") Class dataClass, Fetcher fetcher )
        {   this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor( dataClass, MIME_TYPE );
        }   // end constructor

       
        public java.awt.datatransfer.DataFlavor getCustomDataFlavor()
        {   return customFlavor;
        }   // end getCustomDataFlavor


    /* ********  T R A N S F E R A B L E   M E T H O D S  ******** */    

        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() 
        {   
            if( customFlavor != null )
                return new java.awt.datatransfer.DataFlavor[]
                {   customFlavor,
                    DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
            else
                return new java.awt.datatransfer.DataFlavor[]
                {   DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
        }   // end getTransferDataFlavors


        public Object getTransferData( java.awt.datatransfer.DataFlavor flavor )
        throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException 
        {   
            // Native object
            if( flavor.equals( DATA_FLAVOR ) )
                return fetcher == null ? data : fetcher.getObject();

            // String
            if( flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ) )
                return fetcher == null ? data.toString() : fetcher.getObject().toString();

            // We can't do anything else
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }   // end getTransferData


        public boolean isDataFlavorSupported( java.awt.datatransfer.DataFlavor flavor ) 
        {
            // Native object
            if( flavor.equals( DATA_FLAVOR ) )
                return true;

            // String
            if( flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ) )
                return true;

            // We can't do anything else
            return false;
        }   // end isDataFlavorSupported


    /* ********  I N N E R   I N T E R F A C E   F E T C H E R  ******** */    

     
        public static interface Fetcher
        {
          
            public abstract Object getObject();
        }   // end inner interface Fetcher


    }   // end class TransferableObject

    
}   // end class FileDrop
