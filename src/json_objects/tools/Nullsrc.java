package json_objects.tools;

import java.awt.Color;

public class Nullsrc extends Tool
{
	private Color color;
	private int level,rate,sar,duration;
	private String size;
	private double decimals;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String filter() {
		// TODO Auto-generated method stub
		return null;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = Color.decode(color);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getSar() {
		return sar;
	}

	public void setSar(int sar) {
		this.sar = sar;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getDecimals() {
		return decimals;
	}

	public void setDecimals(double decimals) {
		this.decimals = decimals;
	}

}
