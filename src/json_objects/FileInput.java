package json_objects;

public class FileInput
{
	public enum TYPE{IN,OUT};
	private String file;
	private VideoInput video;
	private AudioInput audio;
	private String split;
	private String type;
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public VideoInput getVideo() {
		return video;
	}
	public void setVideo(VideoInput video) {
		this.video = video;
	}
	public AudioInput getAudio() {
		return audio;
	}
	public void setAudio(AudioInput audio) {
		this.audio = audio;
	}
	public String getSplit() {
		return split;
	}
	public void setSplit(String split) {
		this.split = split;
	}
	public TYPE getType()
	{
		if(type.equalsIgnoreCase("out"))
			return TYPE.OUT;
		else
			return TYPE.IN;
	}
	public void setType(String type) {
		this.type = type;
	}
}
