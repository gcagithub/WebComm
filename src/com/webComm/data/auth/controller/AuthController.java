package com.webComm.data.auth.controller;

import com.webComm.hibernate.Controller;
import com.webComm.utils.Config;

public class AuthController extends Controller {
	public static final String SESSION_NAME = Config.get("project_name");
	
	public AuthController () {
		super(SESSION_NAME, AuthController.class);
	}
}
