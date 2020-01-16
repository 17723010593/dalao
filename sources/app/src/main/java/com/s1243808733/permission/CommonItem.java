package com.s1243808733.permission;

public class CommonItem extends Permission {
	private String title;
	private String subtitle;

	public CommonItem(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubtitle() {
		return subtitle;
	}
}
