package com.webComm.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;

/**
 * Controller. Basic class for data access.
 * 
 * @author alexript
 */
public class Controller {

	private static IBusyInfo busyinfo;
	private boolean showbusy;
	protected Log log;
	private int batchcounter;
	private Session session;
	private String sessionname;

	static {
		busyinfo = null;
	}

	/**
	 * Sets the busy info object.
	 * 
	 * @param bi
	 *            the new busy info
	 */
	public static void setBusyInfo(IBusyInfo bi) {
		busyinfo = bi;
	}

	/**
	 * Send busy info to busy info object.
	 * 
	 * @param state
	 *            the state
	 */
	private static void sendBusyInfo(boolean state) {
		if (busyinfo != null) {
			busyinfo.setBusy(state);
		}
	}

	/**
	 * Instantiates a new controller.
	 */
	public Controller() {
		sessionname = SessionDescriptor.DEFAULT_SESSION_NAME;
		log = LogFactory.getLog(Controller.class);
		showbusy = true;
	}

	/**
	 * Instantiates a new controller.
	 * 
	 * @param displaybusy
	 *            the displaybusy
	 */
	public Controller(boolean displaybusy) {
		sessionname = SessionDescriptor.DEFAULT_SESSION_NAME;
		log = LogFactory.getLog(Controller.class);
		showbusy = displaybusy;
	}

	/**
	 * Instantiates a new controller.
	 * 
	 * @param clazz
	 *            controller class
	 */
	@SuppressWarnings("rawtypes")
	public Controller(Class clazz) {
		log = LogFactory.getLog(clazz);
		sessionname = SessionDescriptor.DEFAULT_SESSION_NAME;
		showbusy = true;
	}

	/**
	 * Instantiates a new controller.
	 * 
	 * @param sfname
	 *            session name
	 */
	public Controller(String sfname) {
		sessionname = sfname;
		log = LogFactory.getLog(Controller.class);
		showbusy = true;
	}

	/**
	 * Instantiates a new controller.
	 * 
	 * @param sfname
	 *            the sfname
	 * @param displaybusy
	 *            the displaybusy
	 */
	public Controller(String sfname, boolean displaybusy) {
		sessionname = sfname;
		log = LogFactory.getLog(Controller.class);
		showbusy = displaybusy;
	}

	/**
	 * Instantiates a new controller.
	 * 
	 * @param sfname
	 *            session name
	 * @param clazz
	 *            controller class
	 */
	@SuppressWarnings("rawtypes")
	public Controller(String sfname, Class clazz) {
		log = LogFactory.getLog(clazz);
		sessionname = sfname;
		showbusy = true;
	}

	/**
	 * Get session
	 * 
	 * @return Session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Begin transaction.
	 */
	public void beginTransaction() {
		if (showbusy) {
			sendBusyInfo(true);
		}
		session = HibernateUtil.getSessionFactory(sessionname)
				.getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
		session.beginTransaction();
	}

	/**
	 * Commit transaction.
	 */
	public void commitTransaction() {
		if (!session.isOpen()) {
			beginTransaction();
		}

		session.getTransaction().commit();
		if (showbusy) {
			sendBusyInfo(false);
		}
	}

	/**
	 * Save object
	 * 
	 * @param clazz
	 *            Model class
	 * @param obj
	 *            Model instance
	 * 
	 * @return id of saved record
	 */
	public Long save(Class<? extends HibernatedEntity> clazz,
			HibernatedEntity obj) {
		return save(clazz.getSimpleName(), obj);
	}

	/**
	 * Save object
	 * 
	 * @param classname
	 *            Model class name
	 * @param obj
	 *            Model instance
	 * 
	 * @return id of saved record
	 */
	public Long save(String classname, HibernatedEntity obj) {
		Long id = 0L;
		try {
			log.debug("Start transaction");
			beginTransaction();
			log.debug("Transaction started");
			session.saveOrUpdate(classname, obj);
			log.debug("saveOrUpdate");
			id = (Long) session.getIdentifier(obj);
			log.debug("Get id = " + id);
			commitTransaction();
			log.debug("Commited transaction");
		} catch (Exception e ) {
			log.error("Can't save object", e);
			id = 0L;
			if(session.isOpen()) {
				session.getTransaction().rollback();
			}
		}
		return id;
	}

	/**
	 * Find record by id.
	 * 
	 * @param classname
	 *            Model class name
	 * @param id
	 *            the id
	 * 
	 * @return found instance
	 */
	public HibernatedEntity find(String classname, long id) {

		HibernatedEntity obj = null;
		beginTransaction();
		obj = (HibernatedEntity) session
				.createQuery(
						"select obj from " + classname
								+ " obj where obj.id = :id")
				.setParameter("id", id).setMaxResults(1).uniqueResult();
		commitTransaction();
		return obj;
	}

	/**
	 * Find record by id.
	 * 
	 * @param clazz
	 *            Model class
	 * @param id
	 *            the id
	 * 
	 * @return found instance
	 */
	public HibernatedEntity find(Class<? extends HibernatedEntity> clazz,
			long id) {
		HibernatedEntity obj = null;
		beginTransaction();
		obj = (HibernatedEntity) session.get(clazz, id);
		commitTransaction();
		return obj;
	}

	/**
	 * Get all records
	 * 
	 * @param classname
	 *            Model class name
	 * 
	 * @return list of Model instances
	 */
	@SuppressWarnings("unchecked")
	public List<? extends HibernatedEntity> findAll(String classname) {
		List<HibernatedEntity> records;
		beginTransaction();

		records = session.createQuery("select obj from " + classname + " obj")
				.list();
		commitTransaction();
		return records;

	}

	/**
	 * Get all records
	 * 
	 * @param clazz
	 *            Model class
	 * 
	 * @return List of Model instances
	 */
	public List<? extends HibernatedEntity> findAll(
			Class<? extends HibernatedEntity> clazz) {
		return findAll(clazz.getSimpleName());
	}

	/**
	 * Get records with criteria
	 * 
	 * @param clazz
	 *            Model class
	 * @param paramname
	 *            param name
	 * @param param
	 *            param value
	 * 
	 * @return list of Model instances
	 */
	public List<? extends HibernatedEntity> findAllByParameter(
			Class<? extends HibernatedEntity> clazz, String paramname,
			Object param) {
		return findAllByParameter(clazz.getSimpleName(), paramname, param);
	}

	/**
	 * Get records with criteria
	 * 
	 * @param tablename
	 *            Model class name
	 * @param paramname
	 *            param name
	 * @param param
	 *            param value
	 * 
	 * @return list of Model instances
	 */
	@SuppressWarnings("unchecked")
	public List<? extends HibernatedEntity> findAllByParameter(
			String tablename, String paramname, Object param) {
		List<? extends HibernatedEntity> records;

		beginTransaction();
		records = session
				.createQuery(
						"select obj from " + tablename + " obj where obj."
								+ paramname + " = :param")
				.setParameter("param", param).list();

		commitTransaction();
		return records;
	}

	/**
	 * Get ONE record by param
	 * 
	 * @param clazz
	 *            Model class
	 * @param paramname
	 *            param name
	 * @param param
	 *            param value
	 * 
	 * @return Model instance
	 */
	public HibernatedEntity findByParameter(
			Class<? extends HibernatedEntity> clazz, String paramname,
			Object param) {
		return findByParameter(clazz.getSimpleName(), paramname, param);
	}

	/**
	 * Get ONE record by param
	 * 
	 * @param tablename
	 *            Model class name
	 * @param paramname
	 *            param name
	 * @param param
	 *            param value
	 * 
	 * @return Model instance
	 */
	public HibernatedEntity findByParameter(String tablename, String paramname,
			Object param) {
		HibernatedEntity obj = null;

		beginTransaction();
		obj = (HibernatedEntity) session
				.createQuery(
						"select obj from " + tablename + " obj where obj."
								+ paramname + " = :param")
				.setParameter("param", param).setMaxResults(1).uniqueResult();

		commitTransaction();
		return obj;
	}

	/**
	 * Begin batch insert.
	 */
	public void beginBatchInsert() {
		batchcounter = 0;
		beginTransaction();

	}

	/**
	 * Flush.
	 */
	public void flush() {
		batchcounter++;
		if (batchcounter % 50 == 0) {
			session.flush();
			session.clear();
		}
	}

	/**
	 * Commit batch insert.
	 */
	public void commitBatchInsert() {
		commitTransaction();

	}

	/**
	 * Delete.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param obj
	 *            the obj
	 */
	public void delete(Class<? extends HibernatedEntity> clazz, Object obj) {
		beginTransaction();
		session.delete(clazz.getSimpleName(), obj);
		commitTransaction();
	}

	/**
	 * Delete.
	 * 
	 * @param classname
	 *            the classname
	 * @param obj
	 *            the obj
	 */
	public void delete(String classname, Object obj) {
		beginTransaction();
		session.delete(classname, obj);
		commitTransaction();
	}

	public String getSessionName() {
		return sessionname;
	}
}
