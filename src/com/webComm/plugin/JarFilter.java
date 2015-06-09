package com.webComm.plugin;

import java.io.File;
import java.io.FilenameFilter;

/**
 * The Class JarFilter.
 */
public class JarFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		return (name.trim().toLowerCase().endsWith(".jar"));
	}

}
