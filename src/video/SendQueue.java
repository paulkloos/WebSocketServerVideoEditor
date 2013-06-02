package video;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.NotYetConnectedException;
import java.util.LinkedList;
import java.util.Queue;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

public class SendQueue implements Runnable
{
	Queue<QueueJob> jobs;
	private Gson inobject;
	
	public SendQueue()
	{
		jobs = new LinkedList<QueueJob>();
		inobject = new Gson();
	}
	public void add(WebSocket conn,String path,json_objects.File info)
	{
		QueueJob temp = new QueueJob(conn,path,info);
		jobs.add(temp);
	}
	public void add(WebSocket conn, SourceFile source,Profile info)
	{
		QueueJob temp = new QueueJob(conn,source,info);
		jobs.add(temp);
	}
	private void sendFile(WebSocket conn,String path,json_objects.File info)
	{
		try
		{
			File file = new File(path);
			String json = inobject.toJson(info);		
			conn.send(json);//send header
			
			byte[] data = new byte[(int)file.length()];
			DataInputStream stream = new DataInputStream(new FileInputStream(file));
			stream.readFully(data);
			stream.close();
			
			conn.send(data);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (NotYetConnectedException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(jobs.size() > 0)
		{
			if(jobs.peek().getReady() == true)
			{
				QueueJob temp = jobs.poll();
				sendFile(temp.getConnection(),temp.getFile(),temp.getFileInfo());
			}
			else
			{
				QueueJob temp = jobs.poll();
				try {
					SourceFile source = temp.getSource().createChild((VideoProfile) temp.getProfile());
					sendFile(temp.getConnection(),source.getAbsoluteFile(),new json_objects.File("clip",temp.getSource().getRelativeFile(), source.getFileName(), source.getVideoProfile().getStart(), source.getVideoProfile().getDuration()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
}
