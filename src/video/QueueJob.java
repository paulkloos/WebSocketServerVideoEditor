package video;


import json_objects.Parent;

import org.java_websocket.WebSocket;

public class QueueJob
{
	private WebSocket connection;
	private String file;
	private json_objects.File fileinfo;
	private SourceFile source;
	private Profile profile;
	private boolean ready;
	private Parent header;
	public QueueJob(WebSocket con, String path, json_objects.File info,Parent header)
	{
		connection = con;
		file = path;
		fileinfo = info;
		ready = true;
		this.header = header;
	}
	public QueueJob(WebSocket con, SourceFile source, Profile info, Parent header)
	{
		connection = con;
		this.source = source;
		profile = info;
		ready = false;	
		this.header = header;
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
	public SourceFile getSource()
	{
		return source;
	}
	public Parent getHeader()
	{
		return header;
	}
}
