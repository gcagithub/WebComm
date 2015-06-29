package com.webComm.hibernate;

import com.webComm.hibernate.SessionDescriptor;
import com.webComm.utils.Config;

public class WebCommSession extends SessionDescriptor {

	public WebCommSession () {
		super(Config.get("project_name"));
	}
	
}
