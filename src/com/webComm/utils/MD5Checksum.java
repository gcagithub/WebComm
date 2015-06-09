package com.webComm.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

public class MD5Checksum {
	private static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);
		return createChecksum(fis);
	}

	private static byte[] createChecksum(InputStream fis) throws Exception {
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}

	private static String createMD5(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public static String getMD5Checksum(URL fileUrl) throws Exception {
		byte[] b = createChecksum(fileUrl.openStream());
		return createMD5(b);
	}

	public static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		return createMD5(b);
	}
}
