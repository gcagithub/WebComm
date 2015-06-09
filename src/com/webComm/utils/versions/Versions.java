package com.webComm.utils.versions;

import java.io.Serializable;
import java.util.HashMap;

import com.webComm.json.Writer;

public class Versions implements Serializable{

	private static final long serialVersionUID = 4049166361699024580L;
	private HashMap<String, Version> versions;
	
	public Versions() {
		versions = new HashMap<String, Version>();
	}
	
	public String toString() {
		return Writer.write(this);
	}
	
	public Version getVersion(String versionname) {
		return versions.get(versionname);
	}
	
	public void addVersion(String versionname, int ver, String filename) {
		Version v = new Version();
		v.setVersionnumber(ver);
		v.setRemotefile(filename);
		versions.put(versionname, v);
	}
	
	/**
	 * @param versions the versions to set
	 */
	public void setVersions(HashMap<String, Version> versions) {
		this.versions = versions;
	}
	/**
	 * @return the versions
	 */
	public HashMap<String, Version> getVersions() {
		return versions;
	}
}
