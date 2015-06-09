package com.webComm.plugin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import com.webComm.json.Writer;

/**
 * The Class PluginDescriptions.
 */
public class PluginDescriptions implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8951385742937649500L;

	/** The dscr. */
	private HashMap<String, PluginDescription> dscr = null;

	/**
	 * Instantiates a new plugin descriptions.
	 */
	public PluginDescriptions() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return Writer.write(this);
	}

	/**
	 * Checks if is white.
	 * 
	 * @param classname
	 *            the classname
	 * @return true, if is white
	 */
	public boolean isWhite(String classname) {
		if (dscr == null) {
			dscr = new HashMap<String, PluginDescription>();
		}

		if (dscr.containsKey(classname)) {
			PluginDescription pd = dscr.get(classname);
			return !pd.isFblack();
		} else {
			PluginDescription pd = new PluginDescription();
			pd.setFblack(false);
			dscr.put(classname, pd);
		}

		return true;
	}

	/**
	 * Gets the classes.
	 * 
	 * @return the classes
	 */
	public Set<String> getClasses() {
		return dscr.keySet();
	}

	/**
	 * Sets the wite.
	 * 
	 * @param classname
	 *            the new wite
	 */
	public void setWite(String classname) {
		setFblack(classname, false);
	}

	/**
	 * Sets the black.
	 * 
	 * @param classname
	 *            the new black
	 */
	public void setBlack(String classname) {
		setFblack(classname, true);
	}

	/**
	 * Sets the fblack.
	 * 
	 * @param classname
	 *            the classname
	 * @param fblack
	 *            the fblack
	 */
	private void setFblack(String classname, boolean fblack) {
		if (dscr == null) {
			dscr = new HashMap<String, PluginDescription>();
		}

		if (dscr.containsKey(classname)) {
			dscr.get(classname).setFblack(fblack);
		} else {
			PluginDescription pd = new PluginDescription();
			pd.setFblack(fblack);
			dscr.put(classname, pd);
		}
	}

}
