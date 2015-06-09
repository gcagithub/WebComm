package com.webComm.utils;

import java.util.Random;

public class Serial {

	public static final int DEFAULT_SERIAL_LENGTH = 24;

	/**
	 * Generate random serial-like sequence with default length.
	 * 
	 * @return the string
	 */
	public static String generate() {
		return generate(DEFAULT_SERIAL_LENGTH);
	}
	
	public static String generate(int length) {
		Random r = new Random();
		String s = "";
		int delimiterpointer = 1;

		while (s.length() < length) {
			if (0 == (delimiterpointer % 5)) {
				s += '-';
				delimiterpointer++;
			}
			int i = r.nextInt(16);
			s += Integer.toHexString(i);
			delimiterpointer++;
		}

		return s;
	}
}
