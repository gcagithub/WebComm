package com.webComm.data.ImgComment;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.webComm.data.ImgComment.model.ImgComment;
import com.webComm.hibernate.SessionDescriptor;
import com.webComm.utils.Config;
import com.webComm.utils.HomeDir;

public class ImgCommentSession extends SessionDescriptor {
	private Configuration _configuration;
	
	
	public ImgCommentSession (String name) {
		super(name);
		_configuration = configuration(name);
	}
	
	private Configuration configuration(String name) {
        File file = new File(HomeDir.getUserApplicationHome()
        		+ HomeDir.getFileSeparator()
        		+ Config.get("hib." + name + ".config_file"));
		return  new Configuration().configure(file);

	}

	@Override
	public SessionFactory getSessionFactory() {
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().
				applySettings(_configuration.getProperties());
		return _configuration.buildSessionFactory(serviceRegistryBuilder.build());
		
	}
}
