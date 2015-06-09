package com.webComm.data.files.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.webComm.data.files.model.ContentType;
import com.webComm.data.files.model.FileMeta;
import com.webComm.data.files.storage.Storage;
import com.webComm.data.files.storage.thumbnails.ThumbnailProperties;
import com.webComm.rest.AService;
import com.webComm.utils.Serial;

public class FilesController {

	private static Storage storage;

	public static void setStorage(Storage newstorage) {
		storage = newstorage;
	}

	public FilesController() {
		if (storage == null) {
			throw new NullPointerException("Storage did not defined");
		}
	}

	public FileMeta find(String requestedName) {
		return find(requestedName, true);
	}

	public FileMeta find(String requestedName, boolean openfile) {
		return storage.read(requestedName, openfile);
	}

	public FileMeta find(String requestedName,
			ThumbnailProperties thumbnailProperties) {
		if (!thumbnailProperties.isApply()) {
			return find(requestedName);
		}
		return storage.readThumbnail(requestedName, thumbnailProperties);
	}

	private FileMeta store(FileMeta fileMeta) throws IOException {
		fileMeta.setSerial(generateSerialNumber());
		if (!storage.store(fileMeta)) {
			fileMeta.setSerial("");
			fileMeta.setOwnerCode("");
		}
		return fileMeta;
	}

	private String generateSerialNumber() {

		String s = "";
		do {
			s = Serial.generate();
		} while (!isValid(s));
		return s;
	}

	private boolean isValid(String s) {
		return storage.isSerialValid(s);
	}

	public List<FileMeta> save(HttpServletRequest request) throws IOException,
			ServletException {

		List<FileMeta> files = new LinkedList<FileMeta>();

		// 1. Check request has multipart content
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		FileMeta temp = null;

		// 2. If yes (it has multipart "files")
		if (isMultipart) {

			// 2.1 instantiate Apache FileUpload classes
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			// 2.2 Parse the request
			try {

				// 2.3 Get all uploaded FileItem
				List<FileItem> items = upload.parseRequest(request);

				String uid = getUid(items, request);
				if (!uid.equals(AService.SESSION_UID_NOUID)) {

					// 2.4 Go over each FileItem
					for (FileItem item : items) {
						// 2.5 if FileItem is not of type "file"
						if (item.isFormField()) {

							// 2.6 Search for "twitter" parameter
							// if(item.getFieldName().equals("twitter"))
							// twitter = item.getString();

						} else {

							// 2.7 Create FileMeta object
							temp = new FileMeta();
							temp.setFileName(item.getName());
							temp.setContent(item.getInputStream());
							temp.setFileType(item.getContentType());
							temp.setFileSize(item.getSize() / 1024 + "Kb");
							temp.setFileRealSize(item.getSize());
							temp.setOwnerCode(uid);
							temp.setContentType(ContentType.DEFAULT_TYPE);

							// 2.7 Add created FileMeta object to List<FileMeta>
							// files
							files.add(store(temp));

						}
					}
				}

			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
		return files;
	}

	private String getUid(List<FileItem> items, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String uid = (String) session.getAttribute(AService.SESSION_UID);
		String clientUID = AService.SESSION_UID_NOUID;
		for (FileItem item : items) {
			if (item.getFieldName().equals(AService.CLIENT_UID_FIELD_NAME)) {
				clientUID = item.getString();
				break;
			}
		}

		if (clientUID == null || !clientUID.equals(uid)) {
			session.setAttribute(AService.SESSION_UID,
					AService.SESSION_UID_NOUID);
			uid = AService.SESSION_UID_NOUID;
		}
		if (uid == null) {
			uid = AService.SESSION_UID_NOUID;
		}

		return uid;
	}

	/**
	 * Select serials for files with image content type
	 * 
	 * @param serials
	 *            List of serials
	 * @return list of serials
	 */
	public List<String> getImages(List<String> serials) {
		List<String> imageSerials = new ArrayList<String>();
		for (String serial : serials) {
			FileMeta meta = storage.read(serial, false);
			if (meta != null && meta.isImage()) {
				imageSerials.add(serial);
			}
		}
		return imageSerials;
	}

	/**
	 * Select serials for files with document content type
	 * 
	 * @param serials
	 *            List of serials
	 * @return list of serials
	 */
	public List<String> getDocuments(List<String> serials) {
		List<String> documentsSerials = new ArrayList<String>();
		List<String> imageSerials = getImages(serials);
		for (String serial : serials) {
			if (!imageSerials.contains(serial)) {
				documentsSerials.add(serial);
			}
		}

		return documentsSerials;
	}

	public List<FileMeta> getMeta(List<String> serials, boolean imagesOnly) {
		List<FileMeta> metas = new ArrayList<FileMeta>();
		for (String serial : serials) {
			FileMeta meta = storage.read(serial, false);
			if (meta != null) {
				if (!imagesOnly || (imagesOnly && meta.isImage())) {
					metas.add(meta);
				}
			}
		}
		return metas;
	}

	public boolean delete(String fileCode, HttpServletRequest request) {
		boolean result = true;
		HttpSession session = request.getSession();
		String uid = (String) session.getAttribute(AService.SESSION_UID);
		String clientUID = request.getParameter(AService.CLIENT_UID_FIELD_NAME);
		if (clientUID == null || !clientUID.equals(uid)) {
			session.setAttribute(AService.SESSION_UID,
					AService.SESSION_UID_NOUID);
			result = false;
		}
		if (uid == null || uid.equals(AService.SESSION_UID_NOUID)) {
			result = false;
		}

		if (result) {
			if (isFileOwner(fileCode, uid)) {
				result = storage.delete(fileCode);
			} else {
				result = false;
			}
		}

		return result;
	}

	public String setContentType(String fileCode, String contentType,
			String requestUID) {
		if (isFileOwner(fileCode, requestUID)) {
			return storage.updateContentType(fileCode, contentType);
		} else {
			return getContentType(fileCode);
		}
	}

	private boolean isFileOwner(String fileCode, String requestUID) {
		// TODO: all company's users are file owners
		Properties properties = storage.getProperties(fileCode);
		return requestUID!=null && requestUID.equals(properties.get(Storage.PROPERTY_OWNER));
	}

	public String getContentType(String fileCode) {
		return storage.getContentType(fileCode);
	}

	/**
	 * Find the first file with selected content type in the list of files. Select the very first if not found.
	 * @param contentType selected type
	 * @param fileCodes list of file codes
	 * @return file code
	 */
	public String selectOneFileByType(String contentType, List<String> fileCodes) {
		List<FileMeta> metas = storage.loadProperties(fileCodes);
		for(FileMeta meta : metas) {
			if(meta.getContentType().equals(contentType)) {
				return meta.getSerial();
			}
		}
		return fileCodes.get(0);
	}

}
