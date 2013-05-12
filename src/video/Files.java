package video;

import java.util.ArrayList;

public abstract class Files
{
	protected ArrayList<String> files;
	public Files()
	{
		files = new ArrayList<String>();
	}
	public void addFile(String value)
	{
		files.add(value);
	}
	public boolean removeFile(int index)
	{
		if(files.size() > index && index >= 0)
		{
			files.remove(index);
			return true;
		}
		else
			return false;
	}
	public boolean removeFile(String value)
	{
		boolean removed = false;
		for(int index = 0; index < files.size(); index++)
		{
			if(files.get(index).equals(value))
			{
				removeFile(index);
				removed = true;
				break;
			}
		}
		return removed;
	}
	public int count()
	{
		return files.size();
	}
	public ArrayList<String> getFileList()
	{
		return files;
	}
	public abstract ArrayList<String> getCommand();
}
