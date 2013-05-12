package video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import video.OutputLogs.ACTION;

public class VideoManager
{
	private ArrayList<VideoClip> clips;
	private Properties settings;
	private String output;
	private String path;
	public VideoManager()
	{
		
	}
	public String getOutput()
	{
		return path+"\\"+output;
	}
	private void combineClips(ArrayList<VideoClip> value)
	{
		Process p;
		OutputLogs stdout = null, errors = null;
		List<String> commands = new ArrayList<String>();
		ProcessBuilder builder = new ProcessBuilder();
		//build command list
		commands.add(settings.getProperty("FFMPEG"));
		commands.add("-i");
		String temp = "\"concat:"; 
		for(int x = 0; x < value.size(); x++)
		{
			if(x > 0)
				temp.concat("|");
			
			temp.concat(combineList(value.get(x).getFiles()));			
		}
		temp.concat("\"");
		commands.add(temp);
		commands.add("-c");
		commands.add("copy");
		commands.add(getOutput());
		
		builder.command(commands);
		builder.redirectErrorStream(true);
		try {
			p = builder.start();
			stdout = new OutputLogs(p.getInputStream(),ACTION.PRINT);
			errors = new OutputLogs(p.getErrorStream(),ACTION.NONE);
	
	//OutputStream stdin = p.getOutputStream();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			stdout.Done();
			errors.Done();
		}
	}
	private String combineList(ArrayList<String> value)
	{
		String output = value.get(0);
		for(int x= 1; x < value.size(); x++)
		{
			output.concat("|"+value.get(x));
		}
		return output;
	}
}
