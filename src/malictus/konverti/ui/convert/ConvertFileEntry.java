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
	
	public ConvertFileEntry(File inFile) throws IOException {
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
		while (keepgoing) {
			String convertedName = newFileName + "-converted";
			if (counter > 0) {
				convertedName = convertedName + "-" + counter;
			}
			try {
				testFile = new File(inFile.getParentFile().getCanonicalPath() + File.separator + convertedName);
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
