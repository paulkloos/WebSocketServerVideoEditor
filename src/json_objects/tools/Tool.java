package json_objects.tools;

import java.util.ArrayList;

public abstract class Tool
{
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
