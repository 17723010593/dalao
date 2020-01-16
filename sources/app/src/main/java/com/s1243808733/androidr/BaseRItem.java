package com.s1243808733.androidr;

public class BaseRItem
{
	public String name;
	public int id;
	
	public Highlight hig_name= new Highlight();
	
	public BaseRItem(){}
	public BaseRItem(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public class Highlight {
		public int start=-1;
		public int end=-1;
	}

	
}
