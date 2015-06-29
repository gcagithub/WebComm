package com.webComm.rest.auth;

import com.webComm.data.auth.model.User;
import com.webComm.rest.AResponse;

public class SignupUserResponse extends AResponse {
	private static final long serialVersionUID = 1L;
	User result;
	
	SignupUserResponse(User user) {
		uid = user.getSessionId();
		result = user;
	}
}
