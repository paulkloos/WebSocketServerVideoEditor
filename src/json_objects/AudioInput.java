package json_objects;

import video.AudioProfile;
import video.Profile;

public class AudioInput
{
	private int bitrate, samplerate;
	private String duration,start, codec, channels;
	
	public int getBitrate() {
		return bitrate;
	}
	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public int getSamplerate() {
		return samplerate;
	}
	public void setSamplerate(int samplerate) {
		this.samplerate = samplerate;
	}
	public String getChannels() {
		return channels;
	}
	public void setChannels(String channels) {
		this.channels = channels;
	}
	public AudioProfile apply(AudioProfile profile)
	{
		AudioProfile temp = profile;
		if(bitrate != 0)
			temp.setBitRate(bitrate);
		
		if(samplerate != 0)
			temp.setSampleRate(samplerate);
		
		if(duration != null)
			temp.setDuration(Profile.timeToSecond(duration));
		
		if(start != null)
			temp.setStart(Profile.timeToSecond(start));
		
		if(codec != null)
			temp.setCodec(codec);
		
		if(channels != null)
			temp.setChannels(channels);
		
		return profile;
	}
}
