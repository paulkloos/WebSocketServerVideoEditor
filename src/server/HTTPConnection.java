package server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;

import json_objects.Message;
import json_objects.Message.COMMAND;
import json_objects.Parent;

import org.java_websocket.*;
import org.java_websocket.handshake.ClientHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import video.Profile;
import video.SendQueue;
import video.SourceFile;
import video.VideoProfile;

public class HTTPConnection extends WebSocketServer
{
	private SourceFiles files;
	private Gson inobject;
	private Queue<Message> upload;
	private SendQueue send;
	private ExecutorService thread;
	public HTTPConnection(int port,SourceFiles ref) throws UnknownHostException
	{
		super(new InetSocketAddress(port));
		inobject = new Gson();
		files = ref;
		upload = new LinkedList<Message>();
		send = new SendQueue();
	}
	
	public HTTPConnection(InetSocketAddress address,SourceFiles ref)
	{
		super(address);
		inobject = new Gson();
		files = ref;
		upload = new LinkedList<Message>();
		send = new SendQueue();
	}

	public void onClose(WebSocket conn, int code, String reason, boolean remote)
	{
		System.out.println("Connection closed");		
	}

	public void onError(WebSocket conn, Exception ex)
	{
		ex.printStackTrace();		
	}

	public void onMessage(final WebSocket conn, String inmessage)
	{
		Message command;
		command = inobject.fromJson(inmessage, Message.class);
		
		try {
			if(command.getCommand() == COMMAND.FOLDER &&files.isDirectory(command.getPath()))
			{
				List<String> list = Arrays.asList(files.getFolders(command.getPath()));
				JsonElement element = new Gson().toJsonTree(list);
				sendText(conn,element.toString());
			}
			else if(command.getCommand() == COMMAND.FILE)
			{
				if(command.getUpload() == null)
				{
					if(files.isDirectory(command.getPath()))
					{
						List<String> list = Arrays.asList(files.getAllFiles(command.getPath()));
						list.addAll(Arrays.asList(files.getFolders(command.getPath())));
						JsonElement element = new Gson().toJsonTree(list);
						sendText(conn,element.toString());
					}
					else
					{
						String[] parts2 = command.getPath().split("\\\\");
						String path = "\\";
						Parent p;
						String json;
						for(int x = 0; x < parts2.length-1; x++)
							path.concat("\\"+parts2[x]);
						
						final SourceFile file = files.getFile(path, parts2[parts2.length-1]);
						
						if(command.getClip() == null)
						{
							p = new Parent(file.getRelativeFile(),file.getVideoProfile().getDuration(),file.getVideoProfile().getDuration());
							json = inobject.toJson(p);
							sendText(conn,json);
							send.add(conn, file.getAbsoluteFile(), new json_objects.File("clip",file.getRelativeFile(), file.getFileName(), "0:0:0", file.getVideoProfile().getDuration()));
							//sendFile(conn,file.getAbsoluteFile(),new json_objects.File("clip",file.getRelativeFile(), file.getFileName(), "0:0:0", file.getVideoProfile().getDuration()));
						}
						else if(command.getClip().getSplit() == null)
						{
							p = new Parent(file.getRelativeFile(),file.getVideoProfile().getDuration(),command.getClip().getSplit());
							json = inobject.toJson(p);
							sendText(conn,json);
							SourceFile convertedfile = null;
							ArrayList<SourceFile> children = file.getChildren();
							for(int index = 0; index < children.size(); index++)
							{
								String[] childpart = children.get(index).getFileName().split("\\.");
								if(children.get(index).getVideoProfile().getHeight() == command.getClip().getHeight() 
										&& children.get(index).getVideoProfile().getWidth() == command.getClip().getWidth() 
										&& childpart[childpart.length-1].equals(command.getClip().getFormat()))
								{
									convertedfile = children.get(index);
								}								
							}
							if(convertedfile == null)
							{
								VideoProfile tempprofile = new VideoProfile();
								String start = command.getClip().getStart();
								tempprofile.setStart(Profile.getHours(start), Profile.getMinutes(start), Profile.getSeconds(start));
								tempprofile.setDimensions(command.getClip().getHeight(), command.getClip().getWidth());
								tempprofile.setExtension(command.getClip().getFormat());
								send.add(conn, file,tempprofile);
							}
							else
							{
								send.add(conn, convertedfile.getAbsoluteFile(), new json_objects.File("clip",file.getRelativeFile(), convertedfile.getFileName(), command.getClip().getStart(), convertedfile.getVideoProfile().getDuration()));
							}
						}
						else
						{
							p = new Parent(file.getRelativeFile(),file.getVideoProfile().getDuration(),command.getClip().getSplit());
							json = inobject.toJson(p);
							sendText(conn,json);
							
							//TODO: check for every clip or make it
							double itteration = Profile.timeToSecond(command.getClip().getSplit());
							double duration = Profile.timeToSecond(file.getVideoProfile().getDuration());
							HashMap<ArrayList<String>,ProfileItem> map = new HashMap<ArrayList<String>,ProfileItem>();
				
							for(double x = Profile.timeToSecond(command.getClip().getStart());x < duration;x+= itteration)
							{
								VideoProfile tempprofile = new VideoProfile();
								tempprofile.setStart(x);
								if(x+itteration < duration)
									tempprofile.setDuration(itteration);
								else
									tempprofile.setDuration(duration - x);
								if(command.getClip().getHeight() > 0 && command.getClip().getWidth() > 0)
									tempprofile.setDimensions(command.getClip().getHeight(), command.getClip().getWidth());
								
								if(command.getClip().getFormat() != null)
									tempprofile.setExtension(command.getClip().getFormat());
								
								ProfileItem item = new ProfileItem(tempprofile);
								//profiles.add(item);
								map.put(tempprofile.getHashValues(), item);
							}//TODO: turn into hash map check
							ArrayList<SourceFile> children = file.getChildren();
							for(int index = 0; index < children.size(); index++)
							{
								ProfileItem item = map.get(children.get(index).getVideoProfile().getHashValues());
								if(item != null)
								{
									send.add(conn, children.get(index).getAbsoluteFile(), new json_objects.File("clip", file.getRelativeFile(), children.get(index).getFileName(), item.getProfile().getStart(), item.getProfile().getDuration()));
									item.setUsed();
									//map.put(children.get(index).getVideoProfile(),item);//update value
									map.put(children.get(index).getVideoProfile().getHashValues(), item);
								}
							}
							
							ArrayList<ProfileItem> maplist = new ArrayList<ProfileItem>(map.values());
							for(int x = 0; x < maplist.size(); x++)
							{
								if(maplist.get(x).getUsed() == false)
								{
									send.add(conn, file, maplist.get(x).getProfile());
									//send make request
								}
							}
						}
					}
					thread = Executors.newFixedThreadPool(1);
					thread.execute(send);
				}
				else
				{
					upload.add(command);
				}
			}
			else if(command.getCommand() == COMMAND.STATUS)
			{
				
			}
			else if(command.getCommand() == COMMAND.ACTION)
			{
				
			}
			System.out.println(command.getDescription());
		} catch (DataFormatException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void onMessage(final WebSocket conn,ByteBuffer blob)
	{
		try {
			Message header = upload.poll();
			if(header == null)
				throw new Exception("Recieved file without header");
			
			FileOutputStream out = new FileOutputStream(files.getSettings().getProperty("ROOT") + header.getPath()+"\\"+header.getUpload());
			FileChannel chout = out.getChannel();
			chout.write(blob);
			chout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onOpen(WebSocket conn, ClientHandshake handshake)
	{
		System.out.println("Connection opened");
		
	}
	public void sendText(WebSocket conn,String message)
	{
		conn.send(message);
	}
}
