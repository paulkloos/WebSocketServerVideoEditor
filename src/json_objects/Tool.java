package json_objects;

import java.util.ArrayList;

import video.SourceFile;

public abstract class Tool
{
	private ArrayList<Tool> tools;
	private Tool next;
	protected SourceFile obj;
	public Tool()
	{
		tools = new ArrayList<Tool>();
		next = null;
	}
	public  void input(SourceFile value)
	{
		obj = value;
	}
	public SourceFile output()
	{
		return obj;
	}
	public void addTool(Tool next)
	{
		this.next = next;
	}
	public void extendTool(Tool ext)
	{
		tools.add(ext);
	}
	public void callNext()
	{
		next.input(obj);
	}
	public boolean nextExist()
	{
		if(next == null)
			return false;
		else
			return true;
	}
	public abstract void run();
}
