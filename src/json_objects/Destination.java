package json_objects;

public class Destination extends Tool
{
	public enum location{LOCAL,CLIENT}
	private location dest;
	private String name;
	public void setLocation(location value)
	{
		dest = value;
	}
	public void setName(String value)
	{
		name = value;
	}
	public location getLocation()
	{
		return dest;
	}
	public String getName()
	{
		return name;
	}
	@Override
	public void run()
	{
		
		
	}
}
