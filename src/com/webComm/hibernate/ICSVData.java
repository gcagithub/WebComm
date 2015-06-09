package com.webComm.hibernate;

/**
 * The Interface ICSVData. translate data to csv and back
 * 
 * @author alexript
 */
public interface ICSVData {
    
    /**
	 * To csv string.
	 * 
	 * @return the string
	 */
    public String toCSVString();
    
    /**
	 * From csv string.
	 * 
	 * @param str
	 *            the string
	 */
    public void fromCSVString(String str);
}
