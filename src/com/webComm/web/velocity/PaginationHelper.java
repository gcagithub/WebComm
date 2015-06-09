package com.webComm.web.velocity;

import java.util.HashMap;
import java.util.List;

import com.webComm.web.http.HtmlMethods;
import com.webComm.web.http.MetaRequest;

/**
 * Pagination
 */
@SuppressWarnings({ "unchecked" })
public class PaginationHelper<E> {
	/** The view. */
	private String view;

	/** The action. */
	private String action;

	/** Записей на странице. */
	private int numperpage;

	/** Всего записей в списке. */
	private long total;

	/** Текущая страница. */
	private int currentpage;

	/** Номер п/п первой записи на страницу. */
	private long firstitem;

	/** Номер по порядку последней записи на странице. */
	private long lastitem;

	/** The cache. */
	private List<E> cache;

	/**
	 * The Constructor.
	 * 
	 * @param request
	 *            the request
	 * @param list
	 *            the list
	 * @param recordsperpage
	 *            the recordsperpage
	 */
	public PaginationHelper(MetaRequest request, List<?> list,
			int recordsperpage) {
		numperpage = recordsperpage;
		if (list == null) {
			total = 0L;
		} else {
			total = list.size();
		}
		view = request.get("v");
		if (view == null || view.length() < 1) {
			view = "start";
		}

		action = request.get("a");
		if (action == null || action.length() < 1) {
			action = "start";
		}

		String page = request.get("page");
		if (page == null || page.length() < 1) {
			setCurrentpage(1);
		} else {
			setCurrentpage(Integer.parseInt(page));
		}

		calcFirstItem();
		calcLastItem();
		if (list != null) {
			cache = (List<E>) list.subList((int) firstitem - 1,
					(int) Math.max(lastitem, firstitem - 1));
		}
	}
	
	public List<E> getList() {
		return cache;
	}

	/**
	 * Calc first item.
	 */
	private void calcFirstItem() {
		firstitem = (numperpage * (getCurrentpage() - 1)) + 1;
	}

	/**
	 * Calc last item.
	 */
	private void calcLastItem() {
		lastitem = Math.min(firstitem + numperpage - 1, total);
	}

	/**
	 * Gets the first item.
	 * 
	 * @return the first item
	 */
	public long getFirstItem() {
		return firstitem;
	}

	/**
	 * Gets the last item.
	 * 
	 * @return the last item
	 */
	public long getLastItem() {
		return lastitem;
	}

	/**
	 * Gets the total.
	 * 
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * Checks for first.
	 * 
	 * @return true, if checks for first
	 */
	public boolean hasFirst() {
		return (getCurrentpage() > 1);
	}

	/**
	 * Checks for prev.
	 * 
	 * @return true, if checks for prev
	 */
	public boolean hasPrev() {
		return hasFirst();
	}

	/**
	 * Checks for next.
	 * 
	 * @return true, if checks for next
	 */
	public boolean hasNext() {
		double k = total / (numperpage+1);
		return (getCurrentpage() < Math.ceil(k) + 1);
	}

	/**
	 * Checks for last.
	 * 
	 * @return true, if checks for last
	 */
	public boolean hasLast() {
		return hasNext();
	}

	/**
	 * Link to first.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the string
	 */
	public String linkToFirst(String name) {
		HashMap<String, String> additionalvalues = new HashMap<String, String>();

		return linkToFirst(name, additionalvalues);
	}

	/**
	 * Link to first.
	 * 
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public String linkToFirst(String name,
			HashMap<String, String> additionalvalues) {
		String ret = name;
		if (hasFirst()) {
			additionalvalues.put("page", "1");
			ret = HtmlSupport.link(null, view, action, name, additionalvalues,
					HtmlMethods.USEJS, false);
		}

		return ret;
	}

	/**
	 * Link to prev.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the string
	 */
	public String linkToPrev(String name) {
		HashMap<String, String> additionalvalues = new HashMap<String, String>();
		return linkToPrev(name, additionalvalues);
	}

	/**
	 * Link to prev.
	 * 
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public String linkToPrev(String name,
			HashMap<String, String> additionalvalues) {
		String ret = name;
		if (hasPrev()) {
			additionalvalues
					.put("page", Integer.toString(getCurrentpage() - 1));
			ret = HtmlSupport.link(null, view, action, name, additionalvalues,
					HtmlMethods.USEJS, false);
		}

		return ret;
	}

	/**
	 * Link to next.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the string
	 */
	public String linkToNext(String name) {
		HashMap<String, String> additionalvalues = new HashMap<String, String>();
		return linkToNext(name, additionalvalues);
	}

	/**
	 * Link to next.
	 * 
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public String linkToNext(String name,
			HashMap<String, String> additionalvalues) {
		String ret = name;
		if (hasNext()) {
			additionalvalues
					.put("page", Integer.toString(getCurrentpage() + 1));

			ret = HtmlSupport.link(null, view, action, name, additionalvalues,
					HtmlMethods.USEJS, false);
		}

		return ret;
	}

	/**
	 * Link to last.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the string
	 */
	public String linkToLast(String name) {
		HashMap<String, String> additionalvalues = new HashMap<String, String>();
		return linkToLast(name, additionalvalues);
	}

	/**
	 * Link to last.
	 * 
	 * @param name
	 *            the name
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public String linkToLast(String name,
			HashMap<String, String> additionalvalues) {
		String ret = name;
		if (hasLast()) {
			additionalvalues.put("page",
					Long.toString((total / numperpage) + 1));
			ret = HtmlSupport.link(null, view, action, name, additionalvalues,
					HtmlMethods.USEJS, false);
		}

		return ret;
	}

	/**
	 * Page links.
	 * 
	 * @param delim
	 *            the delimiter
	 * @param firstname
	 *            the firstname
	 * @param prevname
	 *            the prevname
	 * @param nextname
	 *            the nextname
	 * @param lastname
	 *            the lastname
	 * 
	 * @return the string
	 */
	public String pageLinks(String delim, String firstname, String prevname,
			String nextname, String lastname) {
		StringBuilder sb = new StringBuilder();
		sb.append(linkToFirst(firstname));
		sb.append(delim);
		sb.append(linkToPrev(prevname));
		sb.append(delim);
		sb.append(linkToNext(nextname));
		sb.append(delim);
		sb.append(linkToLast(lastname));
		return sb.toString();
	}

	/**
	 * Page links.
	 * 
	 * @param delim
	 *            the delim
	 * @param firstname
	 *            the firstname
	 * @param prevname
	 *            the prevname
	 * @param nextname
	 *            the nextname
	 * @param lastname
	 *            the lastname
	 * @param additionalvalues
	 *            the additionalvalues
	 * @return the string
	 */
	public String pageLinks(String delim, String firstname, String prevname,
			String nextname, String lastname,
			HashMap<String, String> additionalvalues) {
		StringBuilder sb = new StringBuilder();
		sb.append(linkToFirst(firstname, additionalvalues));
		sb.append(delim);
		sb.append(linkToPrev(prevname, additionalvalues));
		sb.append(delim);
		sb.append(linkToNext(nextname, additionalvalues));
		sb.append(delim);
		sb.append(linkToLast(lastname, additionalvalues));
		return sb.toString();
	}


	/**
	 * Sets the currentpage.
	 * 
	 * @param currentpage
	 *            the currentpage to set
	 */
	private void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	/**
	 * Gets the currentpage.
	 * 
	 * @return the currentpage
	 */
	public int getCurrentpage() {
		return currentpage;
	}

}
