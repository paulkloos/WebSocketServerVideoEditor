package video;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Profile implements Serializable
{
	protected int bitrate;
	private String duration;
	private String start;
	protected String codec;
	private String extension;
	public Profile()
	{
	}
	public void setBitRate(int value)
	{
		bitrate = value;
	}
	public void setDuration(int hour,int minute,double second)
	{
		duration = hour+":"+minute+":"+Math.round(second);
	}
	public void setDuration(double time)
	{
		double temp1 = time/60;
		if(temp1 >= 60)
		{
			double temp2 = temp1/60;
			int hr = (int) Math.floor(temp2);
			double inter = (temp2 - hr)*60;
			int min = (int) Math.floor(inter);
			setDuration(hr, min, (inter-min)*60);
		}
		else
		{
			int min = (int) Math.floor(temp1);
			setDuration(0, min, (temp1-min)*60);
		}
	}
	public void setStart(int hour,int minute,double second)
	{
		start = hour+":"+minute+":"+Math.round(second);
	}
	public void setStart(double time)
	{
		double temp1 = time/60;
		if(temp1 >= 60)
		{
			double temp2 = temp1/60;
			int hr = (int) Math.floor(temp2);
			double inter = (temp2 - hr)*60;
			int min = (int) Math.floor(inter);
			setStart(hr, min, (inter-min)*60);
		}
		else
		{
			int min = (int) Math.floor(temp1);
			setStart(0, min, (temp1-min)*60);
		}
	}
	public void setCodec(String value)
	{
		codec = value;
	}
	public void setExtension(String value)
	{
		extension = value;
	}
	public ArrayList<String> getBitRateCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add(bitrate+"k");
		return output;
	}
	public int getBitRate()
	{
		return bitrate;
	}
	public ArrayList<String> getDurationCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-t");
		output.add(duration);
		return output;
	}
	public String getDuration()
	{
		return duration;
	}
	public String getExtension()
	{
		return extension;
	}
	public ArrayList<String> getStartCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-ss");
		output.add(start);
		return output;
	}
	public ArrayList<String> getInputStartCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-ss");
		double time = timeToSecond(start)-30;
		double temp1 = time/60;
		if(temp1 >= 60)
		{
			double temp2 = temp1/60;
			int hr = (int) Math.floor(temp2);
			double inter = (temp2 - hr)*60;
			int min = (int) Math.floor(inter);
			output.add(hr+":"+min+":"+ ((inter-min)*60));
		}
		else
		{
			int min = (int) Math.floor(temp1);
			output.add("0:"+min+":"+ ((temp1-min)*60));			
		}
		return output;
	}
	public String getStart()
	{
		return start;
	}
	public static int getHours(String time)
	{
		String[] temp = time.split(":");
		return Integer.parseInt(temp[0]);
	}
	public static int getMinutes(String time)
	{
		String[] temp = time.split(":");
		return Integer.parseInt(temp[1]);
	}
	public static double getSeconds(String time)
	{
		String[] temp = time.split(":");
		return Double.parseDouble(temp[2]);
	}
	public static double timeToSecond(String time)
	{
		return getHours(time)*120 + getMinutes(time)*60 + getSeconds(time);
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Profile &&
				getBitRate() == ((Profile) obj).getBitRate() &&
				Profile.timeToSecond(getDuration()) == Profile.timeToSecond(((Profile) obj).getDuration()) &&
				getExtension().equals(((Profile) obj).getExtension()) &&
				Profile.timeToSecond(getStart()) == Profile.timeToSecond(((Profile) obj).getStart()))
			return true;
		else
			return false;
	}
	public abstract ArrayList<String> getHashValues();
}
