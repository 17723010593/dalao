package com.s1243808733.util;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.io.FileFilter;

public class FindFile {
	public static List<File> find(File dir, FileFilter filter) {
		List<File> list=new ArrayList<>();
		File[] fileArray =dir.listFiles();
		for (File file:fileArray) {
			if (filter.accept(file)) {
				if (file.isDirectory()) {
					list.addAll(find(file, filter));
				} else {
					list.add(file);
				}
			}
		}
		return list;
	}
}
