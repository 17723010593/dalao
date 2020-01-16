package com.s1243808733.permission;


public class Permission implements Comparable<Permission> {
	
	private String permission;

	private String name;

	private String describe;

	public TextHighlight highlight=new TextHighlight();

	public Permission(){
		
	}
	
	public Permission(String permission) {
		this.permission = permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		if (name == null) {
			String label=PermissionUtils.getPermissionLabel(permission);
			if (label == null || label.equals(permission)) {
				name = PermissionUtils.getPermissionNameSuffix(permission);
			} else {
				name = label;
			}
		}
		return name;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getDescribe() {
		if (describe == null) {
			describe = PermissionUtils.getPermissionDescription(permission);
		}
		return describe;
	}

	public class Highlight {
		public int start=-1;
		public int end=-1;
	}
	
	public class TextHighlight {

		public Highlight title= new Highlight();
		public Highlight subtitle= new Highlight();

	}

	@Override
	public int compareTo(Permission p1) {
		return p1.getName().compareTo(getName());
	}
	

}
