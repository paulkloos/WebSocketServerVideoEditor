package json_objects.tools;

import java.util.ArrayList;
//! \details base class for filters
public abstract class Tool
{
	public enum STATE{ZERO(0),ONE(1);
	private final int value;
	STATE(int value){this.value = value;}
	public int getValue(){return value;}
	}
	protected ArrayList<String> input;
	protected ArrayList<String> output;

	public abstract void run();
	public abstract String filter();
	public ArrayList<String> getInput() {
		return input;
	}
	public void setInput(ArrayList<String> input) {
		this.input = input;
	}
	public ArrayList<String> getOutput() {
		return output;
	}
	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}
}
