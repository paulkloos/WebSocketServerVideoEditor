package json_objects;

import java.util.zip.DataFormatException;

import json_objects.tools.Tool;

public class Message
{
	public enum COMMAND{FILE_REQUEST,FILE_COMPILE,UPLOAD,FOLDER,STATUS,ACTION}
	private String action;
	private FileInput[] files;
	private Tool[] filters;
	public void setTools(Tool[] value)
	{
		filters = value;
	}
	public void setType(String value)
	{
		action = value;
	}
	public Tool[] getTools()
	{
		return filters;
	}
	public COMMAND getCommand() throws DataFormatException
	{
		if(action.equals("file_request"))
			return COMMAND.FILE_REQUEST;
		else if(action.equals("file_compile"))
			return COMMAND.FILE_COMPILE;
		else if(action.equals("upload"))
			return COMMAND.UPLOAD;
		else if(action.equals("folder"))
			return COMMAND.FOLDER;
		else if(action.equals("status"))
			return COMMAND.STATUS;
		else if(action.equals("action"))
			return COMMAND.ACTION;
		else
			throw new DataFormatException("invalid message type: expected file, status, or action");
	}
	public FileInput[] getFiles() {
		return files;
	}
	public void setFiles(FileInput[] files) {
		this.files = files;
	}/* TODO evenually remove this
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
	}*/
}
