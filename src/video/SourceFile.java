package video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import json_objects.Stream;

import com.google.gson.Gson;

import video.OutputLogs.ACTION;

public class SourceFile implements Serializable
{
	private String file;
	//private ArrayList<SourceFile> converted;
	private HashMap<ArrayList<String>,SourceFile> converted;
	private VideoProfile profile;
	private String folderpath;
	private boolean update;
	private int filecount;
	private Properties settings;
	private SourceFile parent;
	public SourceFile(String file,Properties settings)
	{
		converted = new HashMap<ArrayList<String>,SourceFile>();
		profile = new VideoProfile();
		this.file = file;
		update = true;
		setFolderPath("\\");
		filecount = 0;
		this.settings = settings;
		parent = null;
		setValues();
	}
	public SourceFile(String file, boolean ischild,Properties settings,SourceFile parent)
	{
		if(ischild == true)
		{
			converted = null;
			this.parent = parent;
		}
		else
			converted = new HashMap<ArrayList<String>,SourceFile>();
		
		profile = new VideoProfile();
		this.file = file;
		update = true;
		filecount = 0;
		this.settings = settings;
		setValues();
	}
	public void setFolderPath(String value)
	{
		if(value.indexOf("\\") == 0)
			folderpath = value;
		else
			folderpath = "\\"+value;
	}
	public String getFolderPath()
	{
		return folderpath;
	}
	public String getAbsoluteFile()
	{
		return file;
	}
	public String getRelativeFile()
	{
		return getFolderPath() + "\\" + getFileName();
	}
	public String getFileName()
	{
		String[] temp = file.split("\\\\");
		return temp[temp.length-1];
	}
	public boolean isChild()
	{
		if(converted == null)
			return true;
		else
			return false;
	}
	public SourceFile getParent()
	{
		return parent;
	}
	public VideoProfile getVideoProfile()
	{
		return profile;
	}
	public HashMap<ArrayList<String>,SourceFile> getChildren()
	{
		return converted;
	}
	public boolean getChanged()
	{
		if(update)
		{
			update = false;
			return true;
		}
		else
			return false;
	}
	public SourceFile createChild(VideoProfile profile)
	{
		String temp = settings.getProperty("CONVERTED") + getFolderPath() + "\\temp"+(filecount++)+ "."+profile.getExtension();
		VideoClip clip = new VideoClip(file,settings,temp,profile);
		clip.run();
		SourceFile value = new SourceFile(clip.getOutputFiles().get(0),true,settings,this);
		String startv = profile.getStart();
		value.getVideoProfile().setStart(Profile.getHours(startv),Profile.getMinutes(startv),Profile.getSeconds(startv));
		converted.put(value.getVideoProfile().getHashValues(), value);
		//add(value);
		update = true;
		return value;
	}
	private void setValues()
	{
		Process p;
		BufferedReader br;
		OutputLogs errors = null;
		String result = "";
		Gson converter = new Gson();
		json_objects.Profile info;
		List<String> commands = new ArrayList<String>();
		ProcessBuilder builder = new ProcessBuilder();
		//build command list
		commands.add(settings.getProperty("FFPROBE"));
		commands.add("-v");
		commands.add("quiet");
		commands.add("-print_format");
		commands.add("json");
		commands.add("-show_format");
		commands.add("-show_streams");
		commands.add(file);
		builder.command(commands);
		builder.redirectErrorStream(true);
		try {
			p = builder.start();
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			errors = new OutputLogs(p.getErrorStream(),ACTION.PRINT);
			String line = null;
			while(line == null)
				line = br.readLine();
			
			while(line != null)
			{
				result+= line;
				line = br.readLine();				
			}
			p.waitFor();
			errors.Done();
			info = converter.fromJson(result, json_objects.Profile.class);
			if(info.getFormat() != null)
				processProfile(info);//TODO: destory invalid file
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update = true;
	}
	private void processProfile(json_objects.Profile data)
	{
		Stream temp = data.getStream("video");
		if(temp != null)
		{
			profile.setAspect(temp.getDisplay_aspect_ratio());
			profile.setBitRate(data.getFormat().getBit_rate());
			profile.setCodec(temp.getCodec_name());
			profile.setDimensions(temp.getHeight(), temp.getWidth());
			profile.setDuration(data.getFormat().getDuration());
			String[] exts = data.getFormat().getFormat_name();
			profile.setExtension(exts[exts.length-1]);
			profile.setStart(data.getFormat().getStart_time());
		}
	}
}
