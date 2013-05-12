package json_objects;

public class File
{
	private String type;
	private String parent;
	private String name;
	private String start;
	private String duration;
	public File(String type,String parent,String name, String start, String duration)
	{
		setType(type);
		setName(name);
		setStart(start);
		setDuration(duration);
		setParent(parent);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}	
}
