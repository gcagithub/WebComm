package com.webComm.utils.versions;

import java.io.Serializable;

import com.webComm.json.Writer;

public class Version implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4232180978446835416L;
	private int versionnumber;
	private String remotefile;

	public Version() {
		versionnumber = 0;
		remotefile = "";
	}

	public Version(int num) {
		versionnumber = num;
	}

	public String toString() {
		return Writer.write(this);
	}

	/**
	 * @param versionnumber
	 *            the versionnumber to set
	 */
	public void setVersionnumber(int versionnumber) {
		this.versionnumber = versionnumber;
	}

	/**
	 * @return the versionnumber
	 */
	public int getVersionnumber() {
		return versionnumber;
	}

	/**
	 * @param remotefile
	 *            the remotefile to set
	 */
	public void setRemotefile(String remotefile) {
		this.remotefile = remotefile;
	}

	/**
	 * @return the remotefile
	 */
	public String getRemotefile() {
		return remotefile;
	}
}
