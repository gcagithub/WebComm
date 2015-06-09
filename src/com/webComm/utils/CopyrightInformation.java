package com.webComm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * 
 * @author alexript
 */
public class CopyrightInformation {

	private static final int START_YEAR = 2014;

	public static final String INTERVAL_TEMPLATE = "{0} - {1}";

	public static String getCopyrightYears(Date currentDate) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(currentDate);
		int currentyear = calendar.get(Calendar.YEAR);

		if (START_YEAR == currentyear) {
			return Integer.toString(currentyear);
		} else {
			return MessageFormat.format(INTERVAL_TEMPLATE, Integer.toString(START_YEAR),
					Integer.toString(currentyear));
		}

	}

	public static String getCopyrightYears() {
		Date currentDate = new Date();
		return getCopyrightYears(currentDate);
	}

	private static String DEFAULT_BUILD = "localhost build";
	private static String DEFAULT_DATE = new Date().toString();
	private static Properties versionProperties;

	public static String getBuildInfo() {
		String versionFile = HomeDir.getUserApplicationHome()
				+ HomeDir.getFileSeparator() + "version.properties";

		if (versionProperties == null) {
			versionProperties = new Properties();
			InputStream is = null;

			// First try loading from the current directory
			try {
				File f = new File(versionFile);
				is = new FileInputStream(f);
				versionProperties.load(is);
				is.close();
			} catch (Exception e) {
				is = null;
				versionProperties = null;
			}
		}
		String number = versionProperties == null ? DEFAULT_BUILD
				: versionProperties.getProperty("build.number", DEFAULT_BUILD); // $BUILD_NUMBER
		String date = versionProperties == null ? DEFAULT_DATE
				: versionProperties.getProperty("build.date", DEFAULT_DATE); // $BUILD_ID
		String info = "build: " + number + " build date: " + date;
		return info;
	}
}
