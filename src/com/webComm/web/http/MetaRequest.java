package com.webComm.web.http;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The Class MetaRequest. Workarounds for http request object (utf8, get/post
 * etc.)
 */
public class MetaRequest {

	/** The is multipart. */
	private boolean isMultipart = false;

	/** The original request. */
	private HttpServletRequest req = null;

	/** The file items. */
	private List<FileItem> items = null;

	/** The log. */
	private static Log log = null;

	/** The state. */
	private HashMap<String, String> state = null;

	static {
		log = LogFactory.getLog(MetaRequest.class);
	}

	/**
	 * Instantiates a new meta request.
	 * 
	 * @param request
	 *            the request
	 */
	public MetaRequest(HttpServletRequest request) {
		req = request;
		init();
	}

	/**
	 * Instantiates a new meta request.
	 * 
	 * @param request
	 *            the request
	 * @param noparse
	 *            the noparse
	 */
	public MetaRequest(HttpServletRequest request, boolean noparse) {
		req = request;
		if (!noparse) {
			init();
		}
	}

	/**
	 * Inits the object.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				items = upload.parseRequest(req);
			} catch (FileUploadException e) {
				log.error(e.getMessage());
			}
		}

		HttpSession session = req.getSession();
		state = (HashMap<String, String>) session.getAttribute("sessionstate");
		if (state == null) {
			state = new HashMap<String, String>();
		}
	}

	/**
	 * Gets the state.
	 * 
	 * @param name
	 *            the name
	 * @return the state
	 */
	public String getState(String name) {
		String result = "";
		if (state.containsKey(name)) {
			result = state.get(name);
		}

		return result;
	}

	/**
	 * Sets the state.
	 * 
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 */
	public void setState(String name, String val) {
		state.put(name, val);
		HttpSession session = req.getSession();
		session.setAttribute("sessionstate", state);
	}

	/**
	 * Sets the state.
	 * 
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 */
	public void setState(String name, boolean val) {
		if (val) {
			setState(name, "true");
		} else {
			setState(name, "false");
		}
	}

	/**
	 * Gets the state bool.
	 * 
	 * @param name
	 *            the name
	 * @return the state bool
	 */
	public boolean getStateBool(String name) {
		String v = getState(name);

		boolean result = false;
		if (v != null && !v.isEmpty()) {
			if (v.equals("1") || v.equals("t") || v.equals("true")
					|| v.equals("yes")) {
				result = true;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (isMultipart) {
			sb.append("Multipart request<br/><hr/>");
			if (items != null) {
				Iterator<FileItem> i = items.iterator();
				while (i.hasNext()) {
					FileItem item = i.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String val = item.getString();
						sb.append(name);
						sb.append(" = ");
						sb.append(val);
						sb.append("<br/>");
					}

				}
			} else {
				sb.append("No items<br/>");
			}
		} else {
			sb.append("Regular request<br/><hr/>");

			Enumeration<?> e = req.getParameterNames();
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String val = req.getParameter(name);
				sb.append(name);
				sb.append(" = ");
				sb.append(val);
				sb.append("<br/>");
			}
		}

		return sb.toString();
	}

	/**
	 * Gets the.
	 * 
	 * @param param
	 *            the param
	 * @return the string
	 */
	public String get(String param) {
		String text = "";
		if (isMultipart) {
			if (items != null) {
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (item.isFormField()) {
						String name = item.getFieldName();
						String val = item.getString();
						if (name.equals(param)) {
							text = val;
						}
					}
				}
			}
		} else {
			text = req.getParameter(param);
		}
		return text;
	}

	/**
	 * Gets the int.
	 * 
	 * @param param
	 *            the param
	 * @return the int
	 */
	public int getInt(String param) {
		String v = get(param);
		int result = 0;
		if (v != null && !v.isEmpty()) {
			result = Integer.parseInt(v);
		}
		return result;
	}

	/**
	 * Gets the long.
	 * 
	 * @param param
	 *            the param
	 * @return the long
	 */
	public Long getLong(String param) {
		String v = get(param);
		Long result = 0L;
		if (v != null && !v.isEmpty()) {
			result = Long.parseLong(v);
		}
		return result;
	}

	/**
	 * Gets the float.
	 * 
	 * @param param
	 *            the param
	 * @return the float
	 */
	public float getFloat(String param) {
		String v = get(param).replace(',', '.');
		float result = 0.0F;
		if (v != null && !v.isEmpty()) {
			result = Float.parseFloat(v);
		}
		return result;
	}

	/**
	 * Gets the boolean.
	 * 
	 * @param param
	 *            the param
	 * @return the boolean
	 */
	public boolean getBoolean(String param) {
		String v = get(param);
		boolean result = false;
		if (v != null && !v.isEmpty()) {
			if (v.equals("1") || v.equals("t") || v.equals("true")
					|| v.equals("yes")) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Write file.
	 * 
	 * @param item
	 *            the item
	 * @return the string
	 */
	private String writeFile(FileItem item) {
		String tmpfile = "";
		boolean fcomplete = true;
		log.error("Found. Try to get file body");
		File file = null;
		try {
			file = File.createTempFile("upload", ".bin");
			file.deleteOnExit();
			item.write(file);
			log.error("File created");

		} catch (IOException e1) {
			log.error("Can't create temporary file");
			log.error(e1.getMessage());
			fcomplete = false;
		} catch (Exception e) {
			log.error("Can't write uploaded file into "
					+ file.getAbsolutePath());
			log.error(e.getMessage());
			fcomplete = false;
		} finally {
			if (fcomplete) {
				tmpfile = file.getAbsolutePath();
				log.error("Created temp file " + tmpfile);
			}
		}

		return tmpfile;

	}

	/**
	 * Gets the uploaded file.
	 * 
	 * @param param
	 *            the param
	 * @return the uploaded file
	 */
	public String getUploadedFile(String param) {
		String tmpfile = "";
		if (isMultipart) {
			log.error("Begin file uploading");
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();

				log.error("Check field " + item.getFieldName());

				String name = item.getFieldName();
				if (name.equals(param)) {
					tmpfile = writeFile(item);
				}
			}
		} else {
			log.error("This is not Multipart request");
		}
		return tmpfile;
	}

	public FileItem getUploadedFileItem(String param) {
		if (isMultipart) {
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				String name = item.getFieldName();
				if (name.equals(param)) {
					return item;
				}
			}
		}
		return null;

	}

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	public HttpSession getSession() {
		return req.getSession();
	}

	/**
	 * Gets the request.
	 * 
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return req;
	}

	/**
	 * Gets the Uid.
	 * 
	 * @return the string
	 */
	public String uid() {
		HttpSession session = req.getSession();
		String uid = (String) session.getAttribute("uid");
		return uid;
	}

	/**
	 * Sets the uid.
	 * 
	 * @param newuid
	 *            the new uid
	 */
	public void setUid(String newuid) {
		HttpSession session = req.getSession();
		session.setAttribute("uid", newuid);
	}

}
