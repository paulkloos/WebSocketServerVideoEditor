package video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import server.HTTPConnection;
import server.SourceFiles;

import json_objects.FileInput;
import json_objects.Message;
import json_objects.FileInput.TYPE;
import json_objects.Message.COMMAND;
import json_objects.Parent;
import json_objects.tools.Tool;

public class VideoManager
{
	private Tool[] toollist;
	private FileInput[] filelist;
	private SourceFiles files;
	private ArrayList<String> filters;
	private SendQueue send;
	private Gson inobject;
	public VideoManager(SourceFiles value, SendQueue queue)
	{
		files = value;
		filters = new ArrayList<String>();
		send = queue;
		inobject = new Gson();
	}
	public void processList(Message request, WebSocket conn)
	{
		toollist = request.getTools();
		for(int x = 0; x < toollist.length; x++)
		{
			toollist[x].run();
			filters.add(toollist[x].filter());
		}
		filelist = request.getFiles();
		for(int x = 0; x < filelist.length; x++)
		{//TODO add audio profile comparison
			VideoProfile videoconversion = null;
			AudioProfile audioconversion = null;
			HashMap<ArrayList<String>,VideoProfile> map = new HashMap<ArrayList<String>,VideoProfile>();
			String[] parts2 = filelist[x].getFile().split("\\\\");
			String path = "";
			for(int z = 0; z < parts2.length-1; z++)
				path.concat("\\"+parts2[z]);
			
			SourceFile file = files.getFile(path, parts2[parts2.length-1]);
			if(filelist[x].getVideo() != null)
				videoconversion = filelist[x].getVideo().apply(file.getVideoProfile());
			
			if(filelist[x].getAudio() != null)
				audioconversion = filelist[x].getAudio().apply(file.getVideoProfile().getAudio());
			
			if(filelist[x].getSplit() != null)
			{
				double itteration = Profile.timeToSecond(filelist[x].getSplit());
				double duration = Profile.timeToSecond(filelist[x].getVideo().getDuration());
				
				for(double y = Profile.timeToSecond(videoconversion.getDuration()); y < duration; y += itteration)
				{
					VideoProfile tempprofile = new VideoProfile();
					tempprofile.setStart(y);
					if(x+itteration < duration)
						tempprofile.setDuration(itteration);
					else
						tempprofile.setDuration(duration - y);
										
					map.put(tempprofile.getHashValues(), tempprofile);
				}
			}
			else
			{
				map.put(videoconversion.getHashValues(), videoconversion);
			}
			HashMap<ArrayList<String>,SourceFile> children = file.getChildren();
			ArrayList<VideoProfile> maplist = new ArrayList<VideoProfile>(map.values());
			Parent p;
			String json;
			for(int index = 0; index < maplist.size(); index++)
			{
				SourceFile item = children.get(maplist.get(index).getHashValues());
				try {
					if(item != null)
					{
						if(request.getCommand() == COMMAND.FILE_COMPILE)
						{
							String temp = files.getSettings().getProperty("ROOT") + this.getOutputFile();
							VideoClip clip = new VideoClip(filelist[x].getFile(),files.getSettings(),temp,item.getVideoProfile());
							clip.setFilter(this.getFilters());
							clip.run();
						}
						else if(request.getCommand() == COMMAND.FILE_REQUEST)
						{//sends parent file header then queues up file
							p = new Parent(file.getRelativeFile(),file.getVideoProfile().getDuration(),file.getVideoProfile().getDuration());
							json = inobject.toJson(p);
							HTTPConnection.sendText(conn,json);
							send.add(conn, children.get(index).getAbsoluteFile(), new json_objects.File("clip", file.getRelativeFile(), children.get(index).getFileName(), item.getVideoProfile().getStart(), item.getVideoProfile().getDuration()));
						}
						
					}
					else
					{
						
						if(request.getCommand() == COMMAND.FILE_COMPILE)
						{
							String temp = files.getSettings().getProperty("ROOT") + this.getOutputFile();
							VideoClip clip = new VideoClip(filelist[x].getFile(),files.getSettings(),temp,maplist.get(index));
							clip.setFilter(this.getFilters());
							clip.run();
						}
						else if(request.getCommand() == COMMAND.FILE_REQUEST)
						{//sends parent file header then queues up file
							p = new Parent(file.getRelativeFile(),file.getVideoProfile().getDuration(),file.getVideoProfile().getDuration());
							json = inobject.toJson(p);
							HTTPConnection.sendText(conn,json);
							send.add(conn, file, maplist.get(index));
						}
					}
				} catch (DataFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private String getOutputFile()
	{
		String file = null;
		for(int x = 0; x < filelist.length && file == null; x++)
			if(filelist[x].getType() == TYPE.OUT)
				file = filelist[x].getFile();
		
		return file;
	}
	private String getFilters()
	{
		String temp = null;
		for(int x = 0; x < filters.size(); x++)
		{
			if(x == 0)
				temp = filters.get(x);
			else
				temp += "; "+filters.get(x);
		}
		return temp;
	}
}
