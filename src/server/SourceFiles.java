package server;

import java.util.Properties;

import video.SourceFile;

public interface SourceFiles
{
	public SourceFile getFile(int index);
	public SourceFile getFile(String path,String name);
	public String[] getFolders(String path);
	public int countAll();
	public int count(String path);
	public String[] getAllFiles(String path);
	public boolean isDirectory(String path);
	public Properties getSettings();
}
