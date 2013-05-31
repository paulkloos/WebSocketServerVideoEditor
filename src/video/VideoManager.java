package video;

import java.util.ArrayList;
import java.util.HashMap;

import server.SourceFiles;

import json_objects.FileInput;
import json_objects.Message;
import json_objects.tools.Tool;

public class VideoManager
{
	private Tool[] toollist;
	private FileInput[] filelist;
	private SourceFiles files;
	private ArrayList<String> filters;
	public VideoManager(SourceFiles value)
	{
		files = value;
		filters = new ArrayList<String>();
	}
	public void processList(Message request)
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
			for(int index = 0; index < maplist.size(); index++)
			{//TODO fix the loose ends
				SourceFile item = children.get(maplist.get(index).getHashValues());
				//TODO split between network and local here
				if(item != null)
				;//	send.add(conn, children.get(index).getAbsoluteFile(), new json_objects.File("clip", file.getRelativeFile(), children.get(index).getFileName(), item.getVideoProfile().getStart(), item.getVideoProfile().getDuration()));
				else
				;//	send.add(conn, file, maplist.get(index));
			}
		}
	}

}
