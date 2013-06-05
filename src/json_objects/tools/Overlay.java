package json_objects.tools;

public class Overlay extends Tool
{
	public enum FORMAT{YUV420("yuv420"),YUV444("yuv444"),RGB("rgb");
	private final String value;
	FORMAT(String value){this.value = value;}
	public String getValue(){return value;}
	}
	public enum EVAL{INIT("init"),FRAME("frame");
	private final String value;
	EVAL(String value){this.value = value;}
	public String getValue(){return value;}
	}
	private int x,y;
	private STATE shortest,repeatlast;
	private FORMAT format;
	private EVAL eval;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String filter() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public STATE getShortest() {
		return shortest;
	}

	public void setShortest(STATE shortest) {
		this.shortest = shortest;
	}

	public STATE getRepeatlast() {
		return repeatlast;
	}

	public void setRepeatlast(STATE repeatlast) {
		this.repeatlast = repeatlast;
	}

	public FORMAT getFormat() {
		return format;
	}

	public void setFormat(FORMAT format) {
		this.format = format;
	}

	public EVAL getEval() {
		return eval;
	}

	public void setEval(EVAL eval) {
		this.eval = eval;
	}

}
