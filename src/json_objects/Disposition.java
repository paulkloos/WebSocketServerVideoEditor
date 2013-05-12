package json_objects;

import com.google.gson.annotations.SerializedName;

public class Disposition
{
	@SerializedName("default")
	private int defaultnum;
	private int dub;
	private int original;
	private int comment;
	private int lyrics;
	private int karaoke;
	private int forced;
	private int hearing_impaired;
	private int visual_impaired;
	private int clean_effects;
	private int attached_pic;
	public int getDefaultnum() {
		return defaultnum;
	}
	public void setDefaultnum(int defaultnum) {
		this.defaultnum = defaultnum;
	}
	public int getDub() {
		return dub;
	}
	public void setDub(int dub) {
		this.dub = dub;
	}
	public int getOriginal() {
		return original;
	}
	public void setOriginal(int original) {
		this.original = original;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getLyrics() {
		return lyrics;
	}
	public void setLyrics(int lyrics) {
		this.lyrics = lyrics;
	}
	public int getKaraoke() {
		return karaoke;
	}
	public void setKaraoke(int karaoke) {
		this.karaoke = karaoke;
	}
	public int getForced() {
		return forced;
	}
	public void setForced(int forced) {
		this.forced = forced;
	}
	public int getHearing_impaired() {
		return hearing_impaired;
	}
	public void setHearing_impaired(int hearing_impaired) {
		this.hearing_impaired = hearing_impaired;
	}
	public int getVisual_impaired() {
		return visual_impaired;
	}
	public void setVisual_impaired(int visual_impaired) {
		this.visual_impaired = visual_impaired;
	}
	public int getClean_effects() {
		return clean_effects;
	}
	public void setClean_effects(int clean_effects) {
		this.clean_effects = clean_effects;
	}
	public int getAttached_pic() {
		return attached_pic;
	}
	public void setAttached_pic(int attached_pic) {
		this.attached_pic = attached_pic;
	}

}
