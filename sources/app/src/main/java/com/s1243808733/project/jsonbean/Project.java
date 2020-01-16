package com.s1243808733.project.jsonbean;

import java.util.List;

public class Project {
	public static final String ANDROID="android";

	private String type;
	
	private String main;
	
	private String template;
	
	private boolean noPkg;

	private List<String> openFile;

	public void setNoPkg(boolean noPkg) {
		this.noPkg = noPkg;
	}

	public boolean isNoPkg() {
		return noPkg;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public void setMain(String main) {
		this.main = main;
	}

	public String getMain() {
		return main;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void setOpenFile(List<String> openFile) {
		this.openFile = openFile;
	}

	public List<String> getOpenFile() {
		return openFile;
	}


}
