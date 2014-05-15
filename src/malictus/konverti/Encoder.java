package malictus.konverti;

/**]
 * This class represents a single FFmpeg encoder.
 * 
 * @author Jim Halliday
 */
public class Encoder {
	
	public static final int TYPE_AUDIO = 1;
	public static final int TYPE_SUBTITLE = 2;
	public static final int TYPE_VIDEO = 3;
	private int type;
	private String name;
	private String long_name;
	
	public Encoder(int type, String name, String long_name) {
		this.type = type;
		this.name = name;
		this.long_name = name;
	}
	
	public int getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLongName() {
		return long_name;
	}

}
