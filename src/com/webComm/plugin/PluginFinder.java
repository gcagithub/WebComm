package com.webComm.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.google.gson.Gson;
import com.webComm.utils.Config;

/**
 * The Class PluginFinder.
 */
public class PluginFinder {
	private static final String PLUGIN_INTERFACE = "com.webComm.plugin.IPlugin";

	private static final String PLUGINS_DESCRIPTIONS = "plugins.descriptions";

	// Parameters
	/** The Constant parameters. */
	@SuppressWarnings({ "rawtypes" })
	private static final Class[] parameters = new Class[] { URL.class };

	/** The plugin collection. */
	private List<IPlugin> pluginCollection;

	/** The pds. */
	private PluginDescriptions pds;
	
	private HashMap<String, String> jarnames;

	/**
	 * Instantiates a new plugin finder.
	 */
	public PluginFinder() {
		pluginCollection = new ArrayList<IPlugin>();
		jarnames = new HashMap<String, String>();
		String s = Config.get(PLUGINS_DESCRIPTIONS, "");
		if (s.isEmpty()) {
			pds = new PluginDescriptions();
		} else {
			pds = new Gson().fromJson(s.trim(), PluginDescriptions.class);
		}
	}

	/**
	 * Search.
	 * 
	 * @param directory
	 *            the directory
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void search(String directory) throws Exception {
		File dir = new File(directory);
		if (dir.isFile()) {
			return;
		}

		File[] files = dir.listFiles(new JarFilter());
		for (File f : files) {
			List<String> classNames = getClassNames(f.getAbsolutePath());
			for (String className : classNames) {
				// Remove the “.class” at the back
				String name = className.substring(0, className.length() - 6);
				Class clazz = getClass(f, name);
				Class[] interfaces = clazz.getInterfaces();
				for (Class c : interfaces) {
					// Implement the IPlugin interface
					if (c.getName().equals(PLUGIN_INTERFACE)) {
						if (pds.isWhite(name)) {
							pluginCollection.add((IPlugin) clazz.newInstance());
						}
						//System.err.println("store plugin filename: " + name + ", " + f.getAbsolutePath());
						jarnames.put(name, f.getAbsolutePath());
					}
				}
			}
		}
		saveDescriptions();
	}
	
	public String getPluginFile(String pluginname) {
		if(jarnames.containsKey(pluginname)) {
			return jarnames.get(pluginname);
		}
		
		return null;
	}
	
	/**
	 * Sets the wite.
	 * 
	 * @param classname
	 *            the new wite
	 */
	public void setWite(String classname) {
		pds.setWite(classname);
	}
	
	/**
	 * Sets the black.
	 * 
	 * @param classname
	 *            the new black
	 */
	public void setBlack(String classname) {
		pds.setBlack(classname);
	}
	
	/**
	 * Checks if is white.
	 * 
	 * @param classname
	 *            the classname
	 * @return true, if is white
	 */
	public boolean isWhite(String classname) {
		return pds.isWhite(classname);
	}
	
	/**
	 * Gets the classes.
	 * 
	 * @return the classes
	 */
	public Set<String> getClasses() {
		return pds.getClasses();
	}
	
	/**
	 * Save descriptions.
	 */
	public void saveDescriptions() {
		Config.set(PLUGINS_DESCRIPTIONS, pds.toString());
		Config.save();
	}

	/**
	 * Gets the class names.
	 * 
	 * @param jarName
	 *            the jar name
	 * @return the class names
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("resource")
	protected List<String> getClassNames(String jarName) throws IOException {
		ArrayList<String> classes = new ArrayList<String>();
		JarInputStream jarFile = new JarInputStream(
				new FileInputStream(jarName));
		JarEntry jarEntry;
		while (true) {
			jarEntry = jarFile.getNextJarEntry();
			if (jarEntry == null) {
				break;
			}
			if (jarEntry.getName().trim().toLowerCase().endsWith(".class")) {
				classes.add(jarEntry.getName().replaceAll("/", "\\."));
			}
		}

		return classes;
	}

	/**
	 * Gets the class.
	 * 
	 * @param file
	 *            the file
	 * @param name
	 *            the name
	 * @return the class
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	public Class getClass(File file, String name) throws Exception {
		addURL(file.toURI().toURL());

		URLClassLoader clazzLoader;
		Class clazz;
		String filePath = file.getAbsolutePath();
		filePath = "jar:file://" + filePath + "!/";
		URL url = new File(filePath).toURI().toURL();
		clazzLoader = new URLClassLoader(new URL[] { url });
		clazz = clazzLoader.loadClass(name);
		return clazz;

	}

	/**
	 * Adds the url.
	 * 
	 * @param u
	 *            the u
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addURL(URL u) throws IOException {
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL urls[] = sysLoader.getURLs();
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].toString().equalsIgnoreCase(u.toString())) {
				return;
			}
		}
		Class sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException(
					"Error, could not add URL to system classloader");
		}
	}

	/**
	 * Gets the plugin collection.
	 * 
	 * @return the plugin collection
	 */
	public List<IPlugin> getPluginCollection() {
		return pluginCollection;
	}

	/**
	 * Sets the plugin collection.
	 * 
	 * @param pluginCollection
	 *            the new plugin collection
	 */
	public void setPluginCollection(List<IPlugin> pluginCollection) {
		this.pluginCollection = pluginCollection;
	}
}
