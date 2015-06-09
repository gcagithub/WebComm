package com.webComm.web.velocity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import com.webComm.utils.HomeDir;
import com.webComm.web.http.MetaRequest;


/**
 * Родительский класс для View.
 * 
 * @author alexript
 */
public abstract class View implements IView {

	/** The context. */
	private VelocityContext context;

	/** The templatefilename. */
	private String templatefilename;

	/** The viewname. */
	private String viewname;

	/** The log. */
	protected static Log log;

	static {
		log = LogFactory.getLog(View.class);
	}

	/**
	 * Instantiates a new view.
	 * 
	 * @param viewname
	 *            the viewname
	 */
	public View(String viewname) {
		context = null;
		templatefilename = "";
		this.viewname = viewname;
	}

	/**
	 * Checks if is my view.
	 * 
	 * @param checkedview
	 *            Проверяемый экран
	 * 
	 * @return true, if проверяемый экран -- это наш экран
	 */
	public boolean isMyView(String checkedview) {
		return viewname.equals(checkedview);
	}

	/**
	 * Start view.
	 * 
	 * @param actionname
	 *            the actionname
	 */
	public void startView(String actionname) {
		String templatepath = Factory.getPath() + HomeDir.getFileSeparator()
				+ viewname;

		File templatePath = new File(templatepath);
		if (!templatePath.exists()) {
			templatePath.mkdirs();
		}

		templatefilename = templatepath + HomeDir.getFileSeparator()
				+ actionname;
		if (!templatefilename.endsWith(".vm")) {
			templatefilename += ".vm";
		}

		File templatefile = new File(templatefilename);
		if (!templatefile.exists()) {
			FileOutputStream fos = null;
			try {
				log.trace("Create empty template " + templatefilename);
				fos = new FileOutputStream(templatefilename);
			} catch (FileNotFoundException ex) {
				log.error("Can't create default template in file "
						+ templatefilename);
			} finally {
				try {
					fos
							.write(("# default template for '" + actionname + "' action\n")
									.getBytes());
					fos.close();
				} catch (IOException ex) {
					log.error("Can't write default template as "
							+ templatefilename);
				}
			}
		}

		templatefilename = viewname + HomeDir.getFileSeparator() + actionname;
		if (!templatefilename.endsWith(".vm")) {
			templatefilename += ".vm";
		}
		context = Factory.getContext();

		HashMap<String, Class<?>> globalclasses = Factory.getGlobalclasses();
		Set<String> names = globalclasses.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = (String) i.next();
			putToView(name, globalclasses.get(name));
		}

	}

	/**
	 * Put to view.
	 * 
	 * @param propname
	 *            the propname
	 * @param propval
	 *            the propval
	 */
	public void putToView(String propname, Object propval) {
		context.put(propname, propval);
	}

	/**
	 * Draw view.
	 * 
	 * @return the string
	 */
	public String drawView() {
		StringWriter sw = new StringWriter();
		Factory.write(sw, templatefilename, context);
		context = null;
		return sw.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.spb.dfe.velocity.IView#dispatch(java.lang.String,
	 * javax.servlet.http.HttpServletRequest)
	 */

	public String dispatch(String action, MetaRequest request) {
		String ret = "";
		if (action.equals("list") || action.equals("start")
				|| action.equals("index")) {
			ret = list(request);
		} else if (action.equals("new")) {
			ret = newrec(request);
		} else if (action.equals("create")) {
			ret = create(request);
		} else if (action.equals("edit")) {
			ret = edit(request);
		} else if (action.equals("update")) {
			ret = update(request);
		} else if (action.equals("delete")) {
			ret = delete(request);
		}
		return ret;
	}

	/**
	 * Сгенерировать форму для создания новой записи.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String newrec(MetaRequest request) {
		startView("new");
		putToView("request", request.getRequest());
		return drawView();

	}


}
