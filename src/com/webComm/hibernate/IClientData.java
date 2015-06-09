package com.webComm.hibernate;

/**
 * The Interface IClientData. Converts client-side data into Model object
 * 
 * @author alexript
 */
public interface IClientData {
    
    /**
	 * to object
	 * 
	 * @param obj
	 *            client-side representation
	 */
    public void fromData(Object obj);
}
