package com.webComm.data.files.model;

import java.io.Serializable;

public class FileDescriptor implements Serializable {
	private static final long serialVersionUID = 7535561656455650213L;
	public String serial;
	public String name;
	public Long size;
	public String type;
	public String contentType;
	
	public FileDescriptor() {
		
	}
	
	public FileDescriptor(FileMeta meta) {
		serial=meta.getSerial();
		name = meta.getFileName();
		size = meta.getFileRealSize();
		type = meta.getFileType();
		contentType = meta.getContentType();
	}
}
