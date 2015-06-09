package com.webComm.hibernate;

import com.webComm.json.Writer;

/**
 * The Class DFEEntity. Encode entity to JSON string
 */
public class HibernatedEntity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return Writer.write(this);
	}
}
