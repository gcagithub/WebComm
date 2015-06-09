package com.webComm.data.files.model;

import java.io.InputStream;
import java.util.Arrays;

public class FileMeta {
	private String fileName;
	private String fileSize;
	private String fileType;
	private String serial;
	private Long fileRealSize;
	private String ownerCode;
	private String contentType;
	
	private static final String[] imageTypes = {"image/gif","image/png","image/jpeg","image/jpg"};

	private InputStream content;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public InputStream getContent() {
		return this.content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "FileMeta [fileName=" + fileName + ", fileSize=" + fileSize
				+ ", fileType=" + fileType + ", serial=" + serial + "]";
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Long getFileRealSize() {
		return fileRealSize;
	}

	public void setFileRealSize(Long fileRealSize) {
		this.fileRealSize = fileRealSize;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public boolean isImage() {
		return Arrays.asList(imageTypes).contains(getFileType());
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
