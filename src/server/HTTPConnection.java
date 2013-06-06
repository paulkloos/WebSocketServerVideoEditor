package server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;

import javax.tools.Tool;

import json_objects.Message;
import json_objects.Message.COMMAND;
import json_objects.tools.ToolAdapter;

import org.java_websocket.*;
import org.java_websocket.handshake.ClientHandshake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import video.SendQueue;
import video.VideoManager;
//! \details handles server connection
public class HTTPConnection extends WebSocketServer
{
	private SourceFiles files;
	private Gson inobject;
	private GsonBuilder builder;
	private Queue<Message> upload;
	private SendQueue send;
	private ExecutorService thread;
	private VideoManager manager;
	public HTTPConnection(int port,SourceFiles ref) throws UnknownHostException
	{
		super(new InetSocketAddress(port));
		builder = new GsonBuilder();
		builder.registerTypeAdapter(Tool.class, new ToolAdapter());
		inobject = builder.create();
		files = ref;
		upload = new LinkedList<Message>();
		send = new SendQueue();
		manager = new VideoManager(files,send);
	}
	
	public HTTPConnection(InetSocketAddress address,SourceFiles ref)
	{
		super(address);
		builder = new GsonBuilder();
		builder.registerTypeAdapter(Tool.class, new ToolAdapter());
		inobject = builder.create();
		files = ref;
		upload = new LinkedList<Message>();
		send = new SendQueue();
		manager = new VideoManager(files,send);
	}

	public void onClose(WebSocket conn, int code, String reason, boolean remote)
	{
		System.out.println("Connection closed");		
	}

	public void onError(WebSocket conn, Exception ex)
	{
		ex.printStackTrace();		
	}
	/*!
	 * Function: onMessage
	 * \param WebSocket conn
	 * \param String inmessage
	 * \details handles in coming String based message
	 * (non-Javadoc)
	 * @see org.java_websocket.WebSocketServer#onMessage(org.java_websocket.WebSocket, java.lang.String)
	 */
	public void onMessage(final WebSocket conn, String inmessage)
	{
		Message command;
		command = inobject.fromJson(inmessage, Message.class);
		
		try {
			if(command.getCommand() == COMMAND.FOLDER)
			{
				List<String> list = Arrays.asList(files.getFolders(command.getFiles()[0].getFile()));
				JsonElement element = new Gson().toJsonTree(list);
				sendText(conn,element.toString());
			}
			else if(command.getCommand() == COMMAND.FILE_REQUEST)
			{
				
				if(files.isDirectory(command.getFiles()[0].getFile()))
				{
					List<String> list = Arrays.asList(files.getAllFiles(command.getFiles()[0].getFile()));
					list.addAll(Arrays.asList(files.getFolders(command.getFiles()[0].getFile())));
					JsonElement element = new Gson().toJsonTree(list);
					sendText(conn,element.toString());
				}
				manager.processList(command,conn);
				/*TODO once all things are test to work, remove this code
				 * else
				{
					String[] parts2 = command.getPath()[0].split("\\\\");
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
						HashMap<ArrayList<String>,SourceFile> children = file.getChildren();

						VideoProfile tempprofile = new VideoProfile();
						String tempstart = command.getClip().getStart();
						String tempdur = file.getVideoProfile().getDuration();
						tempprofile.setStart(Profile.getHours(tempstart),Profile.getMinutes(tempstart),Profile.getSeconds(tempstart));
						tempprofile.setDuration(Profile.getHours(tempdur)-Profile.getHours(tempstart),Profile.getMinutes(tempdur)-Profile.getMinutes(tempstart),Profile.getSeconds(tempdur)-Profile.getSeconds(tempstart));
						
						if(command.getClip().getHeight() > 0 && command.getClip().getWidth() > 0)
							tempprofile.setDimensions(command.getClip().getHeight(), command.getClip().getWidth());
						
						if(command.getClip().getFormat() != null)
							tempprofile.setExtension(command.getClip().getFormat());
						
						convertedfile = children.get(tempprofile.getHashValues());
						if(convertedfile == null)
						{
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
						HashMap<ArrayList<String>,VideoProfile> map = new HashMap<ArrayList<String>,VideoProfile>();
			
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
							
							map.put(tempprofile.getHashValues(), tempprofile);
						}//TODO: turn into hash map check
						HashMap<ArrayList<String>,SourceFile> children = file.getChildren();
						ArrayList<VideoProfile> maplist = new ArrayList<VideoProfile>(map.values());
						for(int index = 0; index < maplist.size(); index++)
						{
							SourceFile item = children.get(maplist.get(index).getHashValues());
							if(item != null)
								send.add(conn, children.get(index).getAbsoluteFile(), new json_objects.File("clip", file.getRelativeFile(), children.get(index).getFileName(), item.getVideoProfile().getStart(), item.getVideoProfile().getDuration()));
							else
								send.add(conn, file, maplist.get(index));
						}
					}
				}*/
				thread = Executors.newFixedThreadPool(1);
				thread.execute(send);
			}
			else if(command.getCommand() == COMMAND.UPLOAD)
			{
				upload.add(command);
			}			
			else if(command.getCommand() == COMMAND.STATUS)
			{
				
			}
			else if(command.getCommand() == COMMAND.ACTION)
			{
				
			}
		} catch (DataFormatException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*!
	 * Function: onMessage
	 * \param WebSocket conn
	 * \param ByteBuffer blob
	 * \details handles data coming in for file upload
	 * (non-Javadoc)
	 * @see org.java_websocket.WebSocketServer#onMessage(org.java_websocket.WebSocket, java.nio.ByteBuffer)
	 */
	public void onMessage(final WebSocket conn,ByteBuffer blob)
	{
		try {
			Message header = upload.poll();
			if(header == null)
				throw new Exception("Recieved file without header");// a header is expected for all file uploads
			
			FileOutputStream out = new FileOutputStream(files.getSettings().getProperty("ROOT") + header.getFiles()[0].getFile());
			FileChannel chout = out.getChannel();
			chout.write(blob);
			out.close();
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
	static public void sendText(WebSocket conn,String message)
	{
		conn.send(message);
	}
}
