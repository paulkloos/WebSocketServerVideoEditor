package json_objects;

public class Format
{
	private String filename;
	private int nb_streams;
	private String format_name;
	private String format_long_name;
	private String start_time;
	private String duration;
	private String size;
	private String bit_rate;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getNb_streams() {
		return nb_streams;
	}
	public void setNb_streams(int nb_streams) {
		this.nb_streams = nb_streams;
	}
	public String[] getFormat_name() {
		return format_name.split(",");
	}
	public void setFormat_name(String format_name) {
		this.format_name = format_name;
	}
	public String getFormat_long_name() {
		return format_long_name;
	}
	public void setFormat_long_name(String format_long_name) {
		this.format_long_name = format_long_name;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public double getStart_time()
	{
		return Double.parseDouble(start_time);
	}
	public double getDuration()
	{
		return Double.parseDouble(duration);
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getBit_rate() {
		return Integer.parseInt(bit_rate);
	}
	public void setBit_rate(String bit_rate) {
		this.bit_rate = bit_rate;
	}

}
