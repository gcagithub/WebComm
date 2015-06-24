package com.webComm.rest.auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.webComm.rest.AResponse;
import com.webComm.rest.AService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthService extends AService {

	
	@GET
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public AResponse loginUser () {
		return null;
	}
	
	@GET
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public AResponse logoutUser () {
		return null;
	}
	
	@GET
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public AResponse signupUser () {
		return null;
	}
}
