package json_objects;

import java.util.ArrayList;

import com.google.gson.Gson;

import video.Profile;
import video.SourceFile;
import video.VideoProfile;

public class Clip extends Tool
{
	private int height;
	private int width;
	private String format;
	private String split;
	private String start;
	private Gson inobject;
	private ArrayList<SourceFile> files;
	private ArrayList<VideoProfile> profiles;
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
	private void newClip(SourceFile file,VideoProfile profile)
	{
		files.add(file);
		profiles.add(profile);
	}
	public void run()
	{
		Parent p;
		String json;
		if(getSplit() == null)
		{
			p = new Parent(obj.getRelativeFile(),obj.getVideoProfile().getDuration(),getSplit());
			json = inobject.toJson(p);
			//sendText(conn,json);
			SourceFile convertedfile = null;
			ArrayList<SourceFile> children = obj.getChildren();
			for(int index = 0; index < children.size(); index++)
			{
				String[] childpart = children.get(index).getFileName().split("\\.");
				if(children.get(index).getVideoProfile().getHeight() == getHeight() 
						&& children.get(index).getVideoProfile().getWidth() == getWidth() 
						&& childpart[childpart.length-1].equals(getFormat()))
				{
					convertedfile = children.get(index);
				}								
			}
			if(convertedfile == null)
			{
				VideoProfile tempprofile = new VideoProfile();
				String start = getStart();
				tempprofile.setStart(Profile.getHours(start), Profile.getMinutes(start), Profile.getSeconds(start));
				tempprofile.setDimensions(getHeight(), getWidth());
				tempprofile.setExtension(getFormat());
				newClip(obj,tempprofile);
			}
			else
				newClip(convertedfile,null);
		}
	}
}
