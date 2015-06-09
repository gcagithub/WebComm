package com.webComm.hibernate;

/**
 * The Interface IServerData. Converts Model object into client-side representation
 * 
 * @author alexript
 */
public interface IServerData {
    
    /**
	 * To data.
	 * 
	 * @return client-side
	 */
    public Object toData();
}
