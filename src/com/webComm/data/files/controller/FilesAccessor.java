package com.webComm.data.files.controller;

import java.util.List;

public interface FilesAccessor {
	public List<String> getImages(String unitcode);
	public List<String> getDocuments(String unitcode);
	public List<String> getAllFiles(String unitcode);
}
