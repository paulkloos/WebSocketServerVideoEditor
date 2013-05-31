package json_objects;

import video.Profile;
import video.VideoProfile;
import video.VideoProfile.ENCODEPASS;

public class VideoInput
{
	private int height,width,bitrate;
	private double aspect;
	private String duration,start, codec;
	private ENCODEPASS pass;
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getPass() {
		return pass.getValue();
	}
	public void setPass(int pass) {
		if(pass != 2)
			this.pass = ENCODEPASS.SINGLE;
		else
			this.pass = ENCODEPASS.DOUBLE;
	}
	public int getBitrate() {
		return bitrate;
	}
	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}
	public double getAspect() {
		return aspect;
	}
	public void setAspect(double aspect) {
		this.aspect = aspect;
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
	public VideoProfile apply(VideoProfile profile)
	{
		VideoProfile temp = profile;
		if(height > 0 && width > 0)
			temp.setDimensions(height, width);
		
		if(this.getPass() != temp.getPass())
			temp.setPass(pass);
		
		if(bitrate != 0)
			temp.setBitRate(bitrate);
		
		if(aspect != 0)
			temp.setAspect(aspect);
		
		if(duration != null)
			temp.setDuration(Profile.timeToSecond(duration));
		
		if(start != null)
			temp.setStart(Profile.timeToSecond(start));
		
		if(codec != null)
			temp.setCodec(codec);
		
		return temp;
	}
}
