package server;

import video.Profile;

public class ProfileItem
{
	private Profile profile;
	private boolean used;
	
	public ProfileItem(Profile value)
	{
		profile = value;
		used = false;
	}
	public void setUsed()
	{
		used = true;
	}
	public Profile getProfile()
	{
		return profile;
	}
	public boolean getUsed()
	{
		return used;
	}
}
