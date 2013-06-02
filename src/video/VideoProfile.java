package video;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoProfile extends Profile implements Serializable
{
	public enum PROFILESETTINGS{BITRATE,DURATION,START,ASPECT,PASS,CODEC,DIMENSIONS}
	public enum ENCODEPASS{SINGLE(1),DOUBLE(2);
	private final int value;
	ENCODEPASS(int value){this.value = value;}
	public int getValue(){return value;}
	}
	public enum ASPECTRATIO{WIDE(16/9),FULL(4/3);
		private final double value;
		ASPECTRATIO(double value){this.value = value;}
		public double getValue(){return value;}
	}
	private ArrayList<PROFILESETTINGS> settings;
	private Double aspect;
	private int pass,height,width;
	private AudioProfile audio;
	public VideoProfile()
	{
		super();
		settings = new ArrayList<PROFILESETTINGS>();
		audio = new AudioProfile(); 
	}
	public void setBitRate(int value)
	{
		super.setBitRate(value);
		settings.add(PROFILESETTINGS.BITRATE);		
	}
	public void setDuration(int hour,int minute,int second)
	{
		super.setDuration(hour, minute, second);
		settings.add(PROFILESETTINGS.DURATION);
	}
	public void setDuration(double time)
	{
		super.setDuration(time);
		settings.add(PROFILESETTINGS.DURATION);
	}
	public void setStart(int hour,int minute,int second)
	{
		super.setStart(hour, minute, second);
		settings.add(PROFILESETTINGS.START);
	}
	public void setStart(double time)
	{
		super.setStart(time);
		settings.add(PROFILESETTINGS.START);
	}
	public void setPass(ENCODEPASS value)
	{
		pass = value.getValue();
		settings.add(PROFILESETTINGS.PASS);
	}
	public void setAspect(Double value)
	{
		aspect = value;
		settings.add(PROFILESETTINGS.ASPECT);
	}
	public void setDimensions(int h,int w)
	{
		height = h;
		width = w;
		settings.add(PROFILESETTINGS.DIMENSIONS);
	}
	public void setCodec(String value)
	{
		super.setCodec(value);
		settings.add(PROFILESETTINGS.CODEC);
	}
	public AudioProfile getAudio()
	{
		return audio;
	}
	public ArrayList<String> getBitRateCommand()
	{
		ArrayList<String> output = super.getBitRateCommand();
		output.add(0, "-b:v");
		return output;
	}
	public ArrayList<String> getPassCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-pass:v");
		output.add(String.valueOf(pass));
		return output;
	}
	public int getPass()
	{
		return pass;
	}
	public ArrayList<String> getAspectCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-aspect:v");
		output.add(String.valueOf(aspect));
		return output;
	}
	public Double getAspect()
	{
		return aspect;
	}
	public ArrayList<String> getCodecCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-vcodec");
		output.add(codec);
		return output;
	}
	public String getCodec()
	{
		return codec;
	}
	public ArrayList<String> getDimensionCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-s:v");
		output.add(width+"x"+height);
		return output;
	}
	public int getHeight()
	{
		return height;
	}
	public int getWidth()
	{
		return width;
	}
	public void removeSetting(PROFILESETTINGS value)
	{
		settings.remove(value);
	}
	public ArrayList<String> getCommands()
	{
		ArrayList<String> output = new ArrayList<String>();
		if(settings.contains(PROFILESETTINGS.BITRATE))
			output.addAll(getBitRateCommand());
			//.add(getBitRate());
		
		if(settings.contains(PROFILESETTINGS.DURATION))
			output.addAll(getDurationCommand());
		
		if(settings.contains(PROFILESETTINGS.START))
			output.addAll(getStartCommand());
		
		if(settings.contains(PROFILESETTINGS.ASPECT))
			output.addAll(getAspectCommand());
		
		if(settings.contains(PROFILESETTINGS.PASS))
			output.addAll(getPassCommand());
		
		if(settings.contains(PROFILESETTINGS.CODEC))
			output.addAll(getCodecCommand());
		
		return output;
	}
	public ArrayList<String> getInputCommands()
	{
		ArrayList<String> output = new ArrayList<String>();
		if(settings.contains(PROFILESETTINGS.START) && Profile.timeToSecond(getStart()) > 30)
			output.addAll(getInputStartCommand());
		
		return output;
	}
	public boolean equals(Object obj)
	{
		if(super.equals(obj) &&
				(obj instanceof Profile) &&
				getHeight() == ((VideoProfile)obj).getHeight() &&
				getWidth() == ((VideoProfile)obj).getWidth() &&
				getAspect() == ((VideoProfile)obj).getAspect())
			return true;
		else
			return false;
	}
	public ArrayList<String> getHashValues()
	{
		ArrayList<String> values = new ArrayList<String>();
		values.add(getExtension());
		values.add(Integer.toString(getHeight()));
		values.add(Integer.toString(getWidth()));
		values.add(getStart());
		values.add(getDuration());
		
		return values;
	}
}
