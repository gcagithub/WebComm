package com.webComm.web.velocity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.webComm.web.http.MetaRequest;

/**
 * The Class HtmlSupport.
 */
public class HtmlSupport {

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

		StringBuilder sb = new StringBuilder();
		StringBuilder rb = new StringBuilder();

		if (aslist) {
			sb.append("<li>");
		}
		sb.append("<a id=\"href");
		sb.append(view);
		sb.append(action);
		Class<MetaRequest> metaClass = MetaRequest.class;
		if (request != null && metaClass.isInstance(request)) {
			String reqv = request.get("v");
			if (reqv != null && reqv.equals(view)) {
				sb.append("\" class=\"active");
			}
		}
		sb.append("\" href=\"");
		sb.append("index.jsp?");
		rb.append("v=");
		rb.append(view);
		rb.append("&a=");
		rb.append(action);

		if (additionalvalues != null && additionalvalues.size() > 0) {
			Set<String> keys = additionalvalues.keySet();
			Iterator<String> i = keys.iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				rb.append("&");
				rb.append(key);
				rb.append("=");
				rb.append(additionalvalues.get(key));
			}
		}
		String srequest = rb.toString();
		sb.append(srequest);
		sb.append("\"");
		if (usejs) {
			sb.append(" onclick=\"this.href='#top';drawDispetcher('");
			sb.append(srequest);
			sb.append("', '");
			sb.append(view);
			sb.append("');setCurrentMainInCurrency('");
			sb.append(view);
			sb.append("', '");
			sb.append(action);
			sb.append("', '");
			if (additionalvalues != null && additionalvalues.size() > 0
					&& additionalvalues.containsKey("id")) {
				sb.append(additionalvalues.get("id"));
			}
			sb.append("', '");
			if (additionalvalues != null && additionalvalues.size() > 0
					&& additionalvalues.containsKey("code")) {
				sb.append(additionalvalues.get("code"));
			}
			sb.append("', '");
			if (additionalvalues != null && additionalvalues.size() > 0
					&& additionalvalues.containsKey("page")) {
				sb.append(additionalvalues.get("page"));
			}
			sb.append("');\"");
		}
		sb.append(" alt=\"");
		sb.append(name);
		sb.append("\" title=\"");
		sb.append(name);
		sb.append("\">");
		sb.append(name);
		sb.append("</a>");
		if (aslist) {
			sb.append("</li>");
		}
		String result = sb.toString();
		return result;
	}

	/**
	 * Start form.
	 * 
	 * @param view
	 *            the view
	 * @param action
	 *            the action
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public static String startForm(String view, String action,
			Map<String, String> additionalvalues) {
		StringBuilder sb = new StringBuilder();
		sb.append("<form action=\"index.jsp\" method=\"post\" enctype=\"multipart/form-data\">");
		sb.append("<input type=\"hidden\" name=\"v\" value=\"");
		sb.append(view);
		sb.append("\"/>");
		sb.append("<input type=\"hidden\" name=\"a\" value=\"");
		sb.append(action);
		sb.append("\"/>");

		if (additionalvalues != null && additionalvalues.size() > 0) {
			Set<String> keys = additionalvalues.keySet();
			Iterator<String> i = keys.iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				sb.append("<input type=\"hidden\" name=\"");
				sb.append(key);
				sb.append("\" value=\"");
				sb.append(additionalvalues.get(key));
				sb.append("\"/>");

			}
		}

		String result = sb.toString();
		return result;
	}

	/**
	 * End form.
	 * 
	 * @param submitname
	 *            the submitname
	 * @return the string
	 */
	public static String endForm(String submitname) {
		StringBuffer sb = new StringBuffer();
		sb.append("<input class=\"vhod\" type=\"submit\" value=\"");
		sb.append(submitname);
		sb.append("\" /></form>");
		String result = sb.toString();
		return result;
	}
}
