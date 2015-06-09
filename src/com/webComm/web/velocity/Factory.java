package com.webComm.web.velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

import com.webComm.utils.ApplicationEnvironment;
import com.webComm.utils.HomeDir;
import com.webComm.web.http.MetaRequest;

/**
 * The Class Factory.
 * 
 * @author alexript
 */
public class Factory {

	/** The log. */
	private static Log log;

	/** The engine. */
	private static VelocityEngine engine;

	/** The loaderpath. */
	private static String loaderpath;

	/** Объекты View. */
	private static ArrayList<IView> views;

	/** The globalclasses. */
	private static HashMap<String, Class<?>> globalclasses;

	static {
		log = LogFactory.getLog(Factory.class);
		engine = null;
		loaderpath = HomeDir.getViewPath();
		views = new ArrayList<IView>();
		globalclasses = new HashMap<String, Class<?>>();
	}

	/**
	 * Inits the engine.
	 */
	private static void initEngine() {
		if (engine == null) {
			engine = new VelocityEngine();
			Properties p = new Properties();
			p.setProperty("file.resource.loader.path", loaderpath);
			p.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
					NullLogChute.class.getName());

			try {
				engine.init(p);
			} catch (Exception ex) {
				log.error(ex);
			}
		}
	}

	/**
	 * Добавить View в спул экранов.
	 * 
	 * @param view
	 *            the view
	 */
	public static void addViewObject(IView view) {
		views.add(view);
	}

	/**
	 * Adds the global class.
	 * 
	 * @param globalname
	 *            the globalname
	 * @param clazz
	 *            the clazz
	 */
	public static void addGlobalClass(String globalname, Class<?> clazz) {
		getGlobalclasses().put(globalname, clazz);
	}

	/**
	 * Остановить спул экранов.
	 */
	public static void stop() {
		getGlobalclasses().clear();
		views.clear();
	}

	/**
	 * Найти экран в спуле экранов и передать ему вызов.
	 * 
	 * @param view
	 *            экран
	 * @param action
	 *            действие
	 * @param request
	 *            запрос
	 * @return результат
	 */
	public static String dispatch(String view, String action,
			MetaRequest request) {
		StringBuilder sb = new StringBuilder();

		boolean isfound = false;
		Iterator<IView> i = views.iterator();
		while (i.hasNext()) {
			View v = (View) i.next();
			if (v.isMyView(view)) {
				String html = v.dispatch(action, request);
				if (html != null && !html.isEmpty()) {
					sb.append(html);
					isfound = true;
				}
			}
		}

		if (!isfound) {
			sb.append("Can't dispatch " + view + " view and " + action
					+ " action. <br/>");
			sb.append(getPath());
			sb.append(views.toString());

			sb.append(request.toString());
		}

		String ret = sb.toString();
		return ret;
	}

	/**
	 * Установить путь к шаблонам.
	 * 
	 * @param path
	 *            the new path
	 */
	public static void setPath(String path) {
		loaderpath = path;
	}

	/**
	 * Получить путь к шаблонам.
	 * 
	 * @return the path
	 */
	public static String getPath() {
		return loaderpath;
	}

	/**
	 * Получить контекст движка шаблонов.
	 * 
	 * @return контекст
	 */
	public static VelocityContext getContext() {
		initEngine();
		VelocityContext context = new VelocityContext();
		return context;
	}

	/**
	 * Заполнить поток шаблоном с подставленными данными.
	 * 
	 * @param output
	 *            поток
	 * @param templatefilename
	 *            имя шаблона
	 * @param context
	 *            данные
	 */
	public static void write(StringWriter output, String templatefilename,
			VelocityContext context) {
		Template template = null;
		try {
			template = engine.getTemplate(templatefilename, "UTF-8");
		} catch (ResourceNotFoundException ex) {
			log.error("Resource " + templatefilename + " not found.");
			log.trace(ex);
		} catch (ParseErrorException ex) {
			log.error("Parsing error for " + templatefilename);
			log.trace(ex);
		} catch (Exception ex) {
			log.error("Exception for " + templatefilename + ": "
					+ ex.getMessage());
			log.trace(ex);
		} finally {
			if (template != null) {
				try {
					template.merge(context, output);
				} catch (ResourceNotFoundException ex) {
					log.error("Resource not found.");
					log.trace(ex);
				} catch (ParseErrorException ex) {
					log.error("Parse error.");
					log.trace(ex);
				} catch (MethodInvocationException ex) {
					log.error("Method invocation exception.");
					log.trace(ex);
				}
			}
		}
	}

	/**
	 * Отладка.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		ApplicationEnvironment.start(".dfe", "velocity", true);

		String template = "template.vm";
		if (args.length > 0) {
			template = args[0];
		}

		Date now = new Date();
		VelocityContext vc = getContext();
		vc.put("date", now);
		StringWriter sw = new StringWriter();
		write(sw, template, vc);
		System.out.println(sw.toString());
		ApplicationEnvironment.stop(true);
	}

	/**
	 * Gets the globalclasses.
	 * 
	 * @return the globalclasses
	 */
	public static HashMap<String, Class<?>> getGlobalclasses() {
		return globalclasses;
	}
}
