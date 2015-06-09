package com.webComm.json;

import com.google.gson.Gson;

/**
 * The Class Writer.
 * Create JSON String from Object
 */
public class Writer {
	
	/**
	 * Write.
	 * 
	 * @param obj
	 *            the obj
	 * @return the string
	 */
	public static String write(Object obj) {
		String str = new Gson().toJson(obj);
		return str;
	}
}
