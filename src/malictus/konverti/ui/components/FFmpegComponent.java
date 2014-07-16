package malictus.konverti.ui.components;

import malictus.konverti.FFmpegStruct;

/**
 * Components which implement this can automatically affect an FFmpegStruct.
 * @author Jim Halliday
 */
public interface FFmpegComponent {
	
	/**
	 * Modify a struct passed to this component based on whatever state the component is in
	 * @param struct the FFmpegStruct passed in to be modified
	 */
	public void modifyStruct(FFmpegStruct struct);

}
