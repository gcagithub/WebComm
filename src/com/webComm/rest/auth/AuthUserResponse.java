package com.webComm.rest.auth;

import com.webComm.data.auth.model.User;
import com.webComm.rest.AResponse;

public class AuthUserResponse extends AResponse {
	private static final long serialVersionUID = 1L;
	User result;
	
	AuthUserResponse(User user) {
		uid = user.getSessionId();
		result = user;
	}
}
