package com.webComm.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

public abstract class AService {
	@Context
	private HttpServletRequest request;

	public static final String SESSION_UID = "uid";
	public static final String SESSION_UID_NOUID = "";
	public static final int CODE_OK = 1;
	public static final int CODE_ERROR = 0;
	private static final String RESULT_LOGOUT = "logout";
	public static final String CLIENT_UID_FIELD_NAME = "clientUID";

	/**
	 * Check request for clientUID, compare with session uid. Drop session UID
	 * if UIDs is not equals.
	 * 
	 * @param req
	 *            client request
	 * @return true if client and session have the same uid
	 */
	public boolean checkClientUid(ARequest req) {
		boolean result = true;
		HttpSession session = request.getSession();
		String uid = (String) session.getAttribute(SESSION_UID);
		String clientUID = req.clientUID;
		if (!clientUID.equals(uid)) {
			session.setAttribute(SESSION_UID, SESSION_UID_NOUID);
			result = false;
		}
		if (uid == null || uid.equals(SESSION_UID_NOUID)) {
			result = false;
		}

		return result;
	}

	/**
	 * Prepare response. Check session's UID and, if empty, mark response as
	 * signedin=false
	 * 
	 * @param resp
	 *            response
	 * @return prepared resp
	 */
	public AResponse prepareResponse(AResponse resp) {
		HttpSession session = request.getSession();
		String uid = (String) session.getAttribute(SESSION_UID);
		resp.uid = uid;
		if (SESSION_UID_NOUID.equals(uid)) {
			resp.signedin = CODE_ERROR;
		} else {
			resp.signedin = CODE_OK;
		}
		return resp;
	}

}
