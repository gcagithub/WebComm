package com.webComm.data.ImgComment.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.webComm.hibernate.Controller;
import com.webComm.hibernate.HibernatedEntity;
import com.webComm.utils.Config;

public class ImgCommentController extends Controller {
	public static final String SESSION_NAME = Config.get("project_name");
	
	public ImgCommentController () {
		super(SESSION_NAME, ImgCommentController.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends HibernatedEntity> findAllByTolerance(
				Class<? extends HibernatedEntity> clazz,
				String hashId,
				int perCentMatch
			) {
		
		List<? extends HibernatedEntity> records = new ArrayList<>();
		
		if(hashId == null || hashId.isEmpty()) return records;
		
		beginTransaction();
		
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.add(Restrictions
				.sqlRestriction(
						"comparehash(?, {alias}.hash_id, 4) > ?",
						new Object[]{hashId,perCentMatch},
						new org.hibernate.type.Type[]{
								org.hibernate.type.StandardBasicTypes.STRING,
								org.hibernate.type.StandardBasicTypes.INTEGER
								}
						));
		records = criteria.list();
		
		commitTransaction();
		
		return records;
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends HibernatedEntity> findAllByParameterList(
			Class<? extends HibernatedEntity> clazz, String paramname, List<String> param) {
		List<? extends HibernatedEntity> records = new ArrayList<>();
		
		if(param == null || param.isEmpty()) return records;
		
		beginTransaction();

		records = (List<? extends HibernatedEntity>) getSession()
				.createQuery(
						"select obj from " + clazz.getSimpleName() + " obj where obj."
								+ paramname + " in :param")
				.setParameterList("param", param).list();
	
		commitTransaction();
		
		return records;
	}
	
	public Criteria createCriteria(Class<? extends HibernatedEntity> clazz) {
		return getSession().createCriteria(clazz);
	}
	
	public void addOrder(Class<? extends HibernatedEntity> clazz, Order order) {
		beginTransaction();
		createCriteria(clazz).addOrder(order);
		commitTransaction();
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends HibernatedEntity> findAllByToleranceOrder(
			Class<? extends HibernatedEntity> clazz,
			Object param,
			Order order,
			int perCentMatch) {
		List<? extends HibernatedEntity> records;

		beginTransaction();
//		records = getSession()
//				.createQuery(
//						"select obj from " + clazz.getSimpleName() + " obj where obj."
//								+ paramname + " = :param")
//				.setParameter("param", param).list();
		
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.add(Restrictions
				.sqlRestriction(
						"comparehash(?, {alias}.hash_id, 4) > ?",
						new Object[]{param,perCentMatch},
						new org.hibernate.type.Type[]{
								org.hibernate.type.StandardBasicTypes.STRING,
								org.hibernate.type.StandardBasicTypes.INTEGER
								}
						));
		criteria.addOrder(order);
		records = criteria.list();
		
		commitTransaction();
		
		return records;

	}
	
}
