package com.FouregoStudio.Models;

public class Bookmark {
	
	private long id;
	private int iVolume, iSection, iChapter;
	private int positionInChapterList;
	private String title;
	private String comment;
	private int scrollTo;
	
	public Bookmark() {
		iVolume = 0;
		iSection = 0;
		iChapter = 0;
		title = "";
		comment = "";
		scrollTo = 0;
	}
	
	public Bookmark(long id, int iVolume, int iSection, int iChapter, int positionInChapterList, String title) {
		this.id = id;
		this.iVolume = iVolume;
		this.iSection = iSection;
		this.iChapter = iChapter;
		if (positionInChapterList < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = positionInChapterList;
		this.title = title;
		this.comment = "";
		this.scrollTo = 0;
	}
	
	public Bookmark(int iVolume, int iSection, int iChapter, int positionInChapterList, String title) {
		this.iVolume = iVolume;
		this.iSection = iSection;
		this.iChapter = iChapter;
		if (positionInChapterList < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = positionInChapterList;
		this.title = title;
		this.comment = "";
		this.scrollTo = 0;
	}
	
	public Bookmark(int iVolume, int iSection, int iChapter, int positionInChapterList, String title, String comment) {
		this.iVolume = iVolume;
		this.iSection = iSection;
		this.iChapter = iChapter;
		if (positionInChapterList < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = positionInChapterList;
		this.title = title;
		this.comment = comment;
		this.scrollTo = 0;
	}
	
	public Bookmark(int iVolume, int iSection, int iChapter, int positionInChapterList, String title, String comment, int scrollTo) {
		this.iVolume = iVolume;
		this.iSection = iSection;
		this.iChapter = iChapter;
		if (positionInChapterList < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = positionInChapterList;
		this.title = title;
		this.comment = comment;
		this.scrollTo = scrollTo;
	}
	
	public Bookmark(long id, int iVolume, int iSection, int iChapter, int positionInChapterList, String title, String comment) {
		this.id = id;
		this.iVolume = iVolume;
		this.iSection = iSection;
		this.iChapter = iChapter;
		if (positionInChapterList < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = positionInChapterList;
		this.title = title;
		this.comment = comment;
		this.scrollTo = 0;
	}
	
	public Bookmark(long id, int iVolume, int iSection, int iChapter, int positionInChapterList, String title, String comment, int scrollTo) {
		this.id = id;
		this.iVolume = iVolume;
		this.iSection = iSection;
		this.iChapter = iChapter;
		if (positionInChapterList < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = positionInChapterList;
		this.title = title;
		this.comment = comment;
		this.scrollTo = scrollTo;
	}
	
	public long getId() {
		return id;
	}
	
	public void setVolumeIndex(int value) {
		iVolume = value;
	}
	
	public int getVolumeIndex() {
		return iVolume;
	}
	
	public void setSectionIndex(int value) {
		iSection = value;
	}
	
	public int getSectionIndex() {
		return iSection;
	}
	
	public void setChapterIndex(int value) {
		iChapter = value;
	}
	
	public int getChapterIndex() {
		return iChapter;
	}
	
	public void setPosInChapterList(int value) {
		if (value < 1)
			this.positionInChapterList = 1;
		else
			this.positionInChapterList = value;
	}
	
	public int getPosInChapterList() {
		return positionInChapterList;
	}
	
	public void setTitle(String value) {
		title = value;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setComment(String value) {
		comment = value;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setScrollTo(int value) {
		scrollTo = value;
	}
	
	public int getScrollTo() {
		return scrollTo;
	}

}
