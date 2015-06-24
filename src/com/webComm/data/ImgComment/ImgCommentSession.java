package com.webComm.data.ImgComment;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.webComm.data.ImgComment.model.ImgComment;
import com.webComm.hibernate.SessionDescriptor;
import com.webComm.utils.Config;
import com.webComm.utils.HomeDir;

public class ImgCommentSession extends SessionDescriptor {
//	private Configuration _configuration;
//    private final SessionFactory SESSION_FACTORY;
	
	public ImgCommentSession (String name) {
		super(name);
//		_configuration = configuration(name);
//		SESSION_FACTORY = createSessionFactory();
	}
	
//	private Configuration configuration(String name) {
//        File file = new File(HomeDir.getUserApplicationHome()
//        		+ HomeDir.getFileSeparator()
//        		+ Config.get("hib." + name + ".config_file"));
//		return  new Configuration().configure(file);
//
//	}
//
//	private SessionFactory createSessionFactory() {
//		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().
//				applySettings(_configuration.getProperties());
//		return _configuration.buildSessionFactory(serviceRegistryBuilder.build());
//		
//	}
//	
//	@Override
//	public SessionFactory getSessionFactory() {
//        return SESSION_FACTORY;
//    }
}
