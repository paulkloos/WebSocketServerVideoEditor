package video;

import json_objects.Message;

import org.java_websocket.WebSocket;

public class QueueJob
{
	private WebSocket connection;
	private String file;
	private json_objects.File fileinfo;
	private SourceFile parent;
	private Profile profile;
	private boolean ready;
	public QueueJob(WebSocket con, String path, json_objects.File info)
	{
		connection = con;
		file = path;
		fileinfo = info;
		ready = true;
	}
	public QueueJob(WebSocket con, SourceFile parent, Profile info)
	{
		connection = con;
		this.parent = parent;
		profile = info;
		ready = false;		
	}
	public boolean getReady()
	{
		return ready;
	}
	public WebSocket getConnection()
	{
		return connection;
	}
	public String getFile()
	{
		return file;
	}
	public json_objects.File getFileInfo()
	{
		return fileinfo;
	}
	public Profile getProfile()
	{
		return profile;
	}
	public SourceFile getParent()
	{
		return parent;
	}
}
