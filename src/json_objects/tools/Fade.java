package json_objects.tools;


public class Fade extends Tool
{
	private enum TYPE{in,out};
	private TYPE fade_type;
	private int start_frame;
	private int nb_frames;
	private int alpha;
	private int start_time;
	private int duration;
	private String command;
	public void setType(String value)
	{
		if(value.equals("in"))
			fade_type = TYPE.in;
		else if(value.equals("out"))
			fade_type = TYPE.out;
		else
			fade_type = null;
	}
	public void setStartFrame(int value)
	{
		start_frame = value;
	}
	public void setFrames(int value)
	{
		nb_frames = value;
	}
	public void setAlpha(int value)
	{
		alpha = value;
	}
	public void setStartTime(int value)
	{
		start_time = value;
	}
	public void setDuration(int value)
	{
		duration = value;
	}
	public String filter()
	{
		return command;
	}
	public void run()
	{
		command = "fade=";
		int flag_count = 0;
		if(fade_type != null)
		{
			if(fade_type == TYPE.in)
				command += "t=in";
			else
				command += "t=out";
			
			flag_count++;
		}
		if(start_frame != 0)
		{
			if(flag_count > 0)
				command += ":";
			
			command += "s="+start_frame;
			flag_count++;
		}
		if(nb_frames != 0 && duration == 0)
		{
			if(flag_count > 0)
				command += ":";
			
			command += "n="+nb_frames;
			flag_count++;
		}
		else if(duration != 0)
		{
			if(flag_count > 0)
				command += ":";
			
			command += "d="+duration;
			flag_count++;
		}
		if(alpha != 0)
		{
			if(flag_count > 0)
				command += ":";
			
			command += "alpha="+alpha;
			flag_count++;
		}
		if(start_time != 0)
		{
			if(flag_count > 0)
				command += ":";
			
			command += "st="+start_time;
			flag_count++;
		}
		if(input != null && input.size() > 0)
		{
			String temp = "";
			for(int x = 0; x < input.size(); x++)
				temp += "["+input.get(x)+"]";
			
			command = temp + " " + command;
		}
		if(output != null && output.size() > 0)
		{
			String temp = "";
			for(int x = 0; x < output.size(); x++)
				temp += "["+output.get(x)+"]";
			
			command += " " + temp;
		}
	}
}
