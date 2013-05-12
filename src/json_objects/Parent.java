package json_objects;

public class Parent
{
	private String type;
	private String path;
	private String duration;
	private String clipsize;
	public Parent(String path,String duration,String clipsize)
	{
		setType("file");
		setPath(path);
		setDuration(duration);
		setClipsize(clipsize);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getClipsize() {
		return clipsize;
	}
	public void setClipsize(String clipsize) {
		this.clipsize = clipsize;
	}
}
