package com.webComm.data.auth.controller;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.webComm.hibernate.Controller;
import com.webComm.hibernate.HibernatedEntity;
import com.webComm.utils.Config;

public class AuthController extends Controller {
	public static final String SESSION_NAME = Config.get("project_name");
	
	public AuthController () {
		super(SESSION_NAME, AuthController.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends HibernatedEntity> T getUserByParams(Class<? extends HibernatedEntity> clazz,
			Map<String, String> propertyNameValues){
		
		beginTransaction();
		
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.add(Restrictions.allEq(propertyNameValues));
		T user = (T) criteria.uniqueResult();
		
		commitTransaction();
		
		return user;
	}
}
