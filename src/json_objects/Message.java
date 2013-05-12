package json_objects;

import java.util.zip.DataFormatException;

import video.VideoProfile;

public class Message
{
	public enum COMMAND{FILE,FOLDER,STATUS,ACTION}
	private String type;
	private String path;
	private clip clipfile;
	private VideoProfile videofile;
	private String upload;
	public void setType(String value)
	{
		type = value;
	}
	public void setPath(String value)
	{
		path = value;
	}
	public void setClip(clip value)
	{
		clipfile = value;
	}
	public void setVideoProfile(VideoProfile value)
	{
		videofile = value;
	}
	public void setUpload(String value)
	{
		upload = value;
	}
	public COMMAND getCommand() throws DataFormatException
	{
		if(type.equals("file"))
			return COMMAND.FILE;
		else if(type.equals("folder"))
			return COMMAND.FOLDER;
		else if(type.equals("status"))
			return COMMAND.STATUS;
		else if(type.equals("action"))
			return COMMAND.ACTION;
		else
			throw new DataFormatException("invalid message type: expected file, status, or action");
	}
	public String getPath()
	{
		return path;
	}
	public String getDescription() throws DataFormatException
	{
		if(getCommand() == COMMAND.FILE)
			return "get: "+getPath();
		else if(getCommand() == COMMAND.FOLDER)
			return "get: "+getPath();
		else if(getCommand() == COMMAND.STATUS)
			return "get: status";
		else if(getCommand() == COMMAND.ACTION)
			return "DO";
		else
			return "Did nothing, bad command: "+type;
	}
	public clip getClip()
	{
		return clipfile;
	}
	public VideoProfile getVideoProfile()
	{
		return videofile;
	}
	public String getUpload()
	{
		return upload;
	}
	public static class clip
	{
		private int height;
		private int width;
		private String format;
		private String split;
		private String start;
		public void setheight(int value)
		{
			height = value;
		}
		public void setWidth(int value)
		{
			width = value;
		}
		public void setFormat(String value)
		{
			format = value;
		}
		public int getHeight()
		{
			return height;
		}
		public int getWidth()
		{
			return width;
		}
		public String getFormat()
		{
			return format;
		}
		public String getSplit() {
			return split;
		}
		public void setSplit(String split) {
			this.split = split;
		}
		public String getStart() {
			if(start == null)
				return "00:00:00";
			else
				return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
	}
}
