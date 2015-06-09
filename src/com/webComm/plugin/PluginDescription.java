package com.webComm.plugin;

import java.io.Serializable;

import com.webComm.json.Writer;

/**
 * The Class PluginDescription.
 */
public class PluginDescription implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3961527704133820974L;

	/** The fblack. */
	private boolean fblack;

	/**
	 * Instantiates a new plugin description.
	 */
	public PluginDescription() {
		setFblack(false);
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
	 * Sets the fblack.
	 * 
	 * @param fblack
	 *            the fblack to set
	 */
	public void setFblack(boolean fblack) {
		this.fblack = fblack;
	}

	/**
	 * Checks if is fblack.
	 * 
	 * @return the fblack
	 */
	public boolean isFblack() {
		return fblack;
	}

}
