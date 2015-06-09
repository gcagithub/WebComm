package com.webComm.data.ImgComment.controller;

import java.util.Collection;
import java.util.List;

import com.webComm.data.ImgComment.ImgCommentSession;
import com.webComm.hibernate.Controller;
import com.webComm.hibernate.HibernateUtil;
import com.webComm.hibernate.HibernatedEntity;
import com.webComm.utils.Config;

public class ImgCommentController extends Controller {
	public static final String SESSION_NAME = Config.get("project_name");
	
	public ImgCommentController () {
		super(SESSION_NAME, ImgCommentController.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends HibernatedEntity> findAllByParameterList(
			Class<? extends HibernatedEntity> clazz, String paramname, List<String> param) {
		
		List<? extends HibernatedEntity> records;

		beginTransaction();
		records = (List<? extends HibernatedEntity>) getSession()
				.createQuery(
						"select obj from " + clazz.getSimpleName() + " obj where obj."
								+ paramname + " in :param")
				.setParameterList("param", param).list();

		commitTransaction();
		return records;
	}
	
}
