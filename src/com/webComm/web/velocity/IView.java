package com.webComm.web.velocity;

import com.webComm.web.http.MetaRequest;

/**
 * The Interface IView.
 * 
 * @author alexript
 */
public interface IView {

	/**
	 * Обработать вызов отрисовки главной части страницы.
	 * 
	 * @param action
	 *            действие
	 * @param request
	 *            запрос
	 * @return html-код
	 */
	public String dispatch(String action, MetaRequest request);

	/**
	 * Creates the.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String create(MetaRequest request);

	/**
	 * Edits the.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String edit(MetaRequest request);

	/**
	 * Update.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String update(MetaRequest request);

	/**
	 * Delete.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String delete(MetaRequest request);

	/**
	 * Newrec.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String newrec(MetaRequest request);

	/**
	 * List.
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	public String list(MetaRequest request);
}
