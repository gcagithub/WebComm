package com.webComm.rest.auth;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.catalina.session.Constants;
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
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		SignupUserResponse respLogin = new SignupUserResponse(user);
		ResponseBuilder response = Response.ok(respLogin, MediaType.APPLICATION_JSON);
		
//		user.applayPasswordBCrypt();
		
		Map<String, String> params = new HashMap<String, String>(2);
		params.put("email", user.getEmail());
//		params.put("password", user.getPassword());
		User storedUser = getUserByParams(params);
				
		if(storedUser == null) {
			//not exist
			respLogin.signedin = Status.NOT_FOUND.getStatusCode();
		} else if (BCrypt.checkpw(user.getPassword(), storedUser.getPassword())){
			response.cookie(new NewCookie("username", storedUser.getUsername()));
			request.getSession().setAttribute("email", storedUser.getEmail());
			request.getSession().setMaxInactiveInterval(60*60); // 1 hour
			
			respLogin.signedin = Status.ACCEPTED.getStatusCode();
		} else {
			respLogin.signedin = Status.UNAUTHORIZED.getStatusCode();
		}
		
		return response.build();
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
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		SignupUserResponse respSignUp = new SignupUserResponse(user);
		ResponseBuilder response = Response.ok(respSignUp, MediaType.APPLICATION_JSON);
		
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("email", user.getEmail());
		
		if(getUserByParams(params) != null){
			//already exist
			respSignUp.signedin = Status.CONFLICT.getStatusCode();
			return response.build();
		}
		
		user.applayPasswordBCrypt();
		
		Long id = getAuthController().save(User.class, user);
		
		if(id == 0) {
			respSignUp.signedin = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		} else {
			respSignUp.signedin = Status.CREATED.getStatusCode();
			
			response.cookie(new NewCookie("username", user.getUsername()));
			request.getSession().setAttribute("email", user.getEmail());
			request.getSession().setMaxInactiveInterval(60*60); // 1 hour
		}
		
		return response.build();
	}
	
	@POST
	@Path("/dummy")
//	@Consumes(MediaType.APPLICATION_JSON)
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
