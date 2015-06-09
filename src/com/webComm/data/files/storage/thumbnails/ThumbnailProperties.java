package com.webComm.data.files.storage.thumbnails;

import javax.servlet.http.HttpServletRequest;

public class ThumbnailProperties {
	public static final int WIDTH_DEFAULT=200;
	public static final int HEIGHT_DEFAULT = 200;
	
	private int width;
	private int height;
	private boolean apply;
	
	public ThumbnailProperties() {
		setWidth(WIDTH_DEFAULT);
		setHeight(HEIGHT_DEFAULT);
		setApply(true);
	}
	
	public ThumbnailProperties(int width, int height) {
		this.width = width;
		this.height = height;
		setApply(true);
	}

	public ThumbnailProperties(String width, String height) {
		initFromStrings(width, height);
	}

	private void initFromStrings(String width, String height) {
		if(width==null) {
			setWidth(WIDTH_DEFAULT);
		} else {
			try {
				int w = Integer.parseInt(width);
				setWidth(w);
			} catch (Exception e) {
				setWidth(WIDTH_DEFAULT);
			}
		}

		if(height==null) {
			setHeight(HEIGHT_DEFAULT);
		} else {
			try {
				int h = Integer.parseInt(height);
				setHeight(h);
			} catch (Exception e) {
				setHeight(HEIGHT_DEFAULT);
			}
		}
		setApply(true);
	}
	
	public ThumbnailProperties(HttpServletRequest request) {
		this();
		String w = request.getParameter("w");
		String h = request.getParameter("h");
		if(w==null && h==null) {
			setApply(false);
		} else {
			initFromStrings(w, h);
		}
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isApply() {
		return apply;
	}

	public void setApply(boolean apply) {
		this.apply = apply;
	}
}
