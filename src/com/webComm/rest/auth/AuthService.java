package com.webComm.rest.auth;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.mindrot.jbcrypt.BCrypt;

import com.webComm.data.auth.controller.AuthController;
import com.webComm.data.auth.model.User;
import com.webComm.hibernate.HibernatedEntity;
import com.webComm.rest.AService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthService extends AService {

	private AuthController controller;
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUser (User user, @Context HttpServletRequest request) {
		if (user != null) {
			user.setSessionId(request.getSession(true).getId());
		} else {
			return Response.status(Status.BAD_REQUEST).entity(new AuthUserResponse(user)).type(MediaType.APPLICATION_JSON).build();
		}
		
		AuthUserResponse respLogin = new AuthUserResponse(user);
		ResponseBuilder response;
		
//		user.applayPasswordBCrypt();
		
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("email", user.getEmail());

		User storedUser = getUserByParams(params);
				
		if(storedUser == null) {
			//not exist
			response = Response.status(Status.NOT_FOUND);
		} else if (BCrypt.checkpw(user.getPassword(), storedUser.getPassword())){
			request.getSession().setAttribute("email", storedUser.getEmail());
			request.getSession().setMaxInactiveInterval(60*60); // 1 hour
			
			response = Response.status(Status.ACCEPTED);
			response.cookie(new NewCookie("username", storedUser.getUsername()));
			respLogin.signedin = 1;
		} else {
			response = Response.status(Status.UNAUTHORIZED);
		}
		
		return response.entity(respLogin).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/logout")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response logoutUser (@Context HttpServletRequest request, @CookieParam("JSESSIONID") Cookie cookId) {
		ResponseBuilder response = Response.ok();
		
		if (cookId != null)
			response.cookie(new NewCookie(cookId, "logout", 0, false));

		if (request.getSession(false) != null)
			request.getSession(false).invalidate();
		
		return response.build();
	}
	
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupUser (User user, @Context HttpServletRequest request) {
//	MultivaluedMap<String, String> params
		if (user != null) {
			user.setSessionId(request.getSession(true).getId());
		} else {
			return Response.status(Status.BAD_REQUEST).entity(new AuthUserResponse(user)).type(MediaType.APPLICATION_JSON).build();
		}
		
		AuthUserResponse respSignUp = new AuthUserResponse(user);
		ResponseBuilder response;
		
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("email", user.getEmail());
		
		if(getUserByParams(params) != null){
			//already exist
			return Response.status(Status.CONFLICT).entity(respSignUp).type(MediaType.APPLICATION_JSON).build();
		}
		
		user.applayPasswordBCrypt();
		
		Long id = getAuthController().save(User.class, user);
		
		if(id == 0) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR);
		} else {
			respSignUp.signedin = 1;
			
			response = Response.status(Status.CREATED);
			response.cookie(new NewCookie("username", user.getUsername()));
			
			request.getSession().setAttribute("email", user.getEmail());
			request.getSession().setMaxInactiveInterval(60*60); // 1 hour
		}
		
		return response.entity(respSignUp).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/dummy")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dummy (InputStream input) {
		return null;
	}
	
	private <T extends HibernatedEntity> T getUserByParams(Map<String, String> values) {
		return getAuthController().getUserByParams(User.class, values);

	}
	
	private AuthController getAuthController() {
		return controller == null ? new AuthController() : controller; 
		
	}

}
