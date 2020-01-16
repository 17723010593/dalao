package com.s1243808733.icons;

import com.caverock.androidsvg.SVG;
import java.util.Locale;

public class Icon {
	private String filePath;

	private String file;

	private SVG svg;

	public TextHighlight hig=new TextHighlight();

	public void setSvg(SVG svg) {
		this.svg = svg;
	}

	public SVG getSvg() {
		return svg;
	}

	public String getFileName(String suffix) {
		String name=getFile();
		int i=name.lastIndexOf(".");
		if (i >= 0) {
			name = getFile().substring(0, i);
		}
		return ("ic_" + name.replace("-", "_") + suffix).toLowerCase(Locale.ENGLISH);
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}

    public String getFileName() {
        int i=getFile().lastIndexOf(".");
        if (i >= 0) {
            return getFile().substring(0, i);
        }
        return file;
	}

	public class Highlight {
		public int start=-1;
		public int end=-1;
	}

	public class TextHighlight {

		public Highlight title= new Highlight();

	}



}
