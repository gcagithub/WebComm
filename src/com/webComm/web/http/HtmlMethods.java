package com.webComm.web.http;

import java.util.Map;

import com.webComm.web.velocity.HtmlSupport;

/**
 * Вспомогательные методы для работы с html.
 * 
 * @author alexript
 */
public class HtmlMethods {
	public static final boolean USEJS = false;

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            экран
	 * @param action
	 *            действие
	 * @param name
	 *            название
	 * @param aslist
	 *            признак вывода ссылки в списке
	 * 
	 * @return ссылка
	 */
	public static String link(MetaRequest request, String view, String action,
			String name, boolean aslist) {
		return link(request, view, action, name, USEJS, aslist);
	}

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @param aslist
	 *            the aslist
	 * @return the string
	 */
	public static String link(MetaRequest request, String view, String action,
			String name, Map<String, String> additionalvalues, boolean aslist) {
		return link(request, view, action, name, additionalvalues, USEJS, aslist);
	}

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param name
	 *            the name
	 * @return the string
	 */
	public static String link(MetaRequest request, String view, String action,
			String name) {
		return link(request, view, action, name, USEJS, false);
	}

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public static String link(MetaRequest request, String view, String action,
			String name, Map<String, String> additionalvalues) {
		return link(request, view, action, name, additionalvalues, USEJS, false);
	}

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            экран
	 * @param action
	 *            действие
	 * @param name
	 *            название
	 * @param usejs
	 *            использовать ajax
	 * @param aslist
	 *            выводить в списке
	 * 
	 * @return the string
	 */
	public static String link(MetaRequest request, String view, String action,
			String name, boolean usejs, boolean aslist) {
		String uid = "";
		if (request != null) {
			uid = request.uid();
		}
		return link(request, uid, view, action, name, null, usejs, aslist);

	}

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @param usejs
	 *            the usejs
	 * @param aslist
	 *            the aslist
	 * @return the string
	 */
	public static String link(MetaRequest request, String view, String action,
			String name, Map<String, String> additionalvalues, boolean usejs,
			boolean aslist) {

		String uid = request.uid();
		return link(request, uid, view, action, name, additionalvalues, usejs,
				aslist);

	}

	/**
	 * Сформировать ссылку.
	 * 
	 * @param request
	 *            the request
	 * @param uid
	 *            the uid
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @param usejs
	 *            the usejs
	 * @param aslist
	 *            the aslist
	 * @return the string
	 */
	public static String link(MetaRequest request, String uid, String view,
			String action, String name, Map<String, String> additionalvalues,
			boolean usejs, boolean aslist) {
		String result = "";
//		GroupController gc = new GroupController();
//
//		if (gc.canAccess(uid, view, action)) {
//			result = HtmlSupport.link(request, view, action, name,
//					additionalvalues, usejs, aslist);
//		}
		return result;

	}

	/**
	 * Current action.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the string
	 */
	public static String currentAction(MetaRequest request) {
		String action = request.get("a");
		if (action == null || action.length() < 1) {
			action = "start";
		}
		return action;
	}

	/**
	 * Current view.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the string
	 */
	public static String currentView(MetaRequest request) {
		String view = request.get("v");
		if (view == null || view.length() < 1) {
			view = "start";
		}
		return view;
	}

	/**
	 * Start form.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @return the string
	 */
	public static String startForm(MetaRequest request, String view,
			String action) {
		return startForm(request, view, action, null);
	}

	/**
	 * Start form.
	 * 
	 * @param request
	 *            the request
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public static String startForm(MetaRequest request, String view,
			String action, Map<String, String> additionalvalues) {
		String result = "";
//		GroupController gc = new GroupController();
//		HttpSession session = request.getSession();
//		String uid = (String) session.getAttribute("uid");
//		request.setState("fdrawform", false);
//		if (gc.canAccess(uid, view, action)) {
//			request.setState("fdrawform", true);
//			result = HtmlSupport.startForm(view, action, additionalvalues);
//		}
		return result;
	}

	/**
	 * End form.
	 * 
	 * @param request
	 *            the request
	 * @param submitname
	 *            the submitname
	 * @return the string
	 */
	public static String endForm(MetaRequest request, String submitname) {
		String result = "";
		boolean fdrawform = request.getStateBool("fdrawform");
		if (fdrawform) {
			request.setState("fdrawform", false);
			result = HtmlSupport.endForm(submitname);
		}

		return result;
	}

}
