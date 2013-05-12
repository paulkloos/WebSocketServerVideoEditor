package video;

import java.util.ArrayList;

public class InputFiles extends Files
{
	public InputFiles()
	{
		super();
	}

	public ArrayList<String> getCommand()
	{
		ArrayList<String> output = files;
		output.add(0, "-i");
		
		return output;
	}
}
