package json_objects;

import java.util.ArrayList;

public class Profile
{
	private Format format;
	private ArrayList<Stream> streams;
	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public Stream getStream(String value)
	{
		Stream result = null;
		for(int x = 0; x < streams.size() && result == null; x++)
		{
			if(streams.get(x).getCodec_type().equals(value))
				result = streams.get(x);
		}
		return result;
	}
	public void setStream(ArrayList<Stream> stream) {
		streams = stream;
	}
}
