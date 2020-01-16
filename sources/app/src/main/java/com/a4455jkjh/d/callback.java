package com.a4455jkjh.d;
import com.aide.ui.build.android.fb;

public class callback implements Runnable {

	private c c;
	private String msg;

	public callback(c c, String msg) {
		this.c = c;
		this.msg = msg;
	}

	@Override
	public void run() {
		fb.DW(c.a(c), msg);
	}

}
