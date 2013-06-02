package video;

import java.io.Serializable;
import java.util.ArrayList;

public class AudioProfile extends Profile implements Serializable
{
	public enum PROFILESETTINGS{BITRATE,DURATION,START,SAMPLERATE,CODEC,CHANNELS}
	private int samplerate;
	private String channels;
	private ArrayList<PROFILESETTINGS> settings;
	public AudioProfile()
	{
		super();
		settings = new ArrayList<PROFILESETTINGS>();
	}
	public void setSampleRate(int value)
	{
		samplerate = value;
		settings.add(PROFILESETTINGS.SAMPLERATE);
	}
	public void setChannels(String value)
	{
		channels = value;
		settings.add(PROFILESETTINGS.CHANNELS);
	}
	public ArrayList<String> getSampleRateCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-ar:a");
		output.add(String.valueOf(samplerate));
		return output;
	}
	public int getSampleRate()
	{
		return samplerate;
	}
	public ArrayList<String> getCodecCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add("-acodec");
		output.add(codec);
		return output;
	}
	public String getCodec()
	{
		return codec;
	}
	public ArrayList<String> getChannelsCommand()
	{
		ArrayList<String> output = new ArrayList<String>();
		output.add(channels);
		return output;
	}
	public String getChannels()
	{
		return channels;
	}
	public void setCodec(String value)
	{
		super.setCodec(value);
		settings.add(PROFILESETTINGS.CODEC);
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
		
		if(settings.contains(PROFILESETTINGS.SAMPLERATE))
			output.addAll(getSampleRateCommand());
		
		if(settings.contains(PROFILESETTINGS.CODEC))
			output.addAll(getCodecCommand());
		
		return output;
	}
	public ArrayList<String> getHashValues()
	{
		ArrayList<String> values = new ArrayList<String>();
		values.add(getExtension());
		values.add(getChannels());
		values.add(Integer.toString(getSampleRate()));
		values.add(getStart());
		values.add(getDuration());
		
		return values;
	}
}
