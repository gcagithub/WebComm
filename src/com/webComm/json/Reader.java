package com.webComm.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

/**
 * The Class Reader. Read JSON into Object
 */
public class Reader {

	public static Object readUrl(String source, Class<?> cls)
			throws IOException {
		URL url = new URL(source);
		return read(url, cls);
	}

	public static Object read(URL url, Class<?> cls) throws IOException {

		StringBuffer buffer = null;

		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String inputLine;
		buffer = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			buffer.append(inputLine);
		}
		in.close();
		return read(buffer.toString(), cls);
	}

	public static Object read(String serializedString, Class<?> cls) {
		Object obj = new Gson().fromJson(serializedString, cls);
		return obj;
	}
}