package com.webComm.utils;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {

	private static Properties props;
	private static Properties globalprops;
	private static String globalpropsname;

	private static Log log = null;

	private static boolean fchanged = false;
	private static boolean fglobalchanged = false;

	static {
		props = new Properties();
		try {
			InputStream fis = VFS.openForRead(HomeDir
					.getConfigFileName());
			props.load(fis);
			fis.close();
			log = LogFactory.getLog(Config.class);
		} catch (java.io.FileNotFoundException e) {
			if (log != null) {
				log.error(e.getMessage());
			} else {
				System.err.println(e.getMessage());
				System.err.println("I will_use default settings");
			}

		} catch (java.io.IOException e) {
			if (log != null) {
				log.error(e.getMessage());
			} else {
				System.err.println(e.getMessage());
				System.err.println("This is very uncommon place");
			}

		}

		globalprops = null;
		globalpropsname = null;
	}

	public static void initGlobalConfig(String path) {
		globalpropsname = path;
		globalprops = new Properties();
		try {
			InputStream fis = new FileInputStream(path);
			globalprops.load(fis);
			fis.close();
		} catch (java.io.FileNotFoundException e) {
			if (log != null) {
				log.error(e.getMessage());
			} else {
				System.err.println(e.getMessage());
				System.err.println("I will_use default settings");
			}

		} catch (java.io.IOException e) {
			if (log != null) {
				log.error(e.getMessage());
			} else {
				System.err.println(e.getMessage());
				System.err.println("This is wery uncommon place");
			}

		}

	}

	public static Properties get() {
		return props;
	}

	public static String get(String name) {
		String val = props.getProperty(name);
		log.trace("Config get(name): " + name + " = " + val);
		return val;
	}

	public static String get(String name, String defval) {
		if (!props.containsKey(name)) {
			props.put(name, defval);
			fchanged = true;
		}
		String val = props.getProperty(name,
				getGlobal("defval." + name, defval));
		log.trace("Config get(name, defval): " + name + " = " + val);
		return val;

	}

	public static String getGlobal(String name, String defval) {
		if (globalprops == null) {
			return defval;
		}

		if (!globalprops.containsKey(name)) {
			globalprops.put(name, defval);
			fglobalchanged = true;
		}
		String val = globalprops.getProperty(name, defval);
		log.trace("Config get(name, defval): " + name + " = " + val);
		return val;

	}

	public static void set(String name, int val) {
		set(name, Integer.toString(val));
	}

	public static void set(String name, String val) {
		props.put(name, val);
		fchanged = true;
	}

	public static void setGlobal(String name, String val) {
		if (globalprops == null) {
			return;
		}
		globalprops.put(name, val);
		fglobalchanged = true;
	}

	public static void save() {
		if (fchanged || fglobalchanged) {
			try {
				OutputStream fos;
				if (fchanged) {
					fos = VFS.openForWrite(HomeDir
							.getConfigFileName(false));

					props.store(fos, "Application settings");

					fos.close();
				}

				if (fglobalchanged && globalprops != null
						&& globalpropsname != null) {
					fos = new FileOutputStream(globalpropsname);

					globalprops.store(fos, "Application settings");

					fos.close();

				}

			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}
			fchanged = false;

		}
	}

	public static void set(String name, double resizeWeight) {
		set(name, Double.toString(resizeWeight));

	}

	public static void set(String name, boolean b) {
		if (b) {
			set(name, "true");
		} else {
			set(name, "false");
		}
	}

	public static void set(String name, Color color) {
		set(name,
				color.getRed() + "," + color.getGreen() + "," + color.getBlue());
	}

	public static int get(String name, int i) {
		String s = get(name, Integer.toString(i));
		int r = i;
		r = Integer.parseInt(s);
		return r;
	}

	public static boolean get(String name, boolean b) {
		String s = get(name, Boolean.toString(b));
		boolean r = b;
		r = Boolean.parseBoolean(s);
		return r;
	}

	public static Long get(String name, Long l) {
		String s = get(name, Long.toString(l));
		Long r = l;
		r = Long.parseLong(s);
		return r;
	}

	public static Color get(String name, Color c) {
		String s = get(name,
				c.getRed() + "," + c.getGreen() + "," + c.getBlue());
		Color r = c;
		String[] ss = s.split(",");
		r = new Color(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
				Integer.parseInt(ss[2]));
		return r;
	}
}
