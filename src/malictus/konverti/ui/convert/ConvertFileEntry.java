package malictus.konverti.ui.convert;

import java.io.*;

/**
 * This class represents a file pair --- a file to be converted, and the corresponding file that represents the converted version of the file.
 * Checks are put into place to ensure the new file doesn't already exist. This class does not add a suffix to outgoing to file names: that
 * will be handled later in the pipeline. This class also does not actually create the new file; it just sets it up so that FFmpeg can create it.
 */
public class ConvertFileEntry {
	
	private File inFile;
	private File outFile;
	
	/**
	 * Create a ConvertFileEntry object.
	 * 
	 * @param inFile the input file to be processed
	 * @param newSuffix a new suffix to be applied to the output file; use empty string if no new suffix is desired
	 * @param target_folder the target folder to write to, or null to write to the same folder as the original file
	 * @throws IOException if file read error occurs
	 */
	public ConvertFileEntry(File inFile, String newSuffix, File target_folder) throws IOException {
		this.inFile = inFile;
		if (!inFile.isFile() || !inFile.canRead()) {
			throw new IOException("Input file can't be read");
		}
		String newFileName = inFile.getName();
		if(newFileName.contains(".")) {
			try {
				//strip extension
				newFileName = newFileName.substring(0, newFileName.lastIndexOf('.'));
			} catch (Exception err) {
				newFileName = inFile.getName();
			}
		}
		int counter = 0;
		boolean keepgoing = true;
		File testFile = null;
		//first try adding suffix without other changes
		String newName = newFileName + "." + newSuffix;
		try {
			if (target_folder == null) {
				testFile = new File(inFile.getParentFile().getCanonicalPath() + File.separator + newName);
			} else {
				testFile = new File(target_folder.getCanonicalPath() + File.separator + newName);
			}
			if (!testFile.exists()) {
				outFile = testFile;
				keepgoing = false;
			}
		} catch (Exception err) {
			err.printStackTrace();
			keepgoing = true;
		}
		//keep going until we find a name that's not taken
		while (keepgoing) {
			String convertedName = newFileName + "-converted";
			if (counter > 0) {
				convertedName = convertedName + "-" + counter;
			}
			//add suffix back
			if ((newSuffix != null) && (newSuffix.length() > 0)) {
				convertedName = convertedName + "." + newSuffix;
			}
			try {
				if (target_folder == null) {
					testFile = new File(inFile.getParentFile().getCanonicalPath() + File.separator + convertedName);
				} else {
					testFile = new File(target_folder.getCanonicalPath() + File.separator + convertedName);
				}
				if (!testFile.exists()) {
					outFile = testFile;
					keepgoing = false;
				}
			} catch (Exception err) {
				err.printStackTrace();
				keepgoing = true;
			}
			counter++;
			if (counter > 300) {
				//sanity check
				keepgoing = false;
				throw new IOException("Can't create new file");
			}
		}		
	}
	
	public File getInFile() {
		return inFile;
	}
	
	public File getOutFile() {
		return outFile;
	}

}
