package com.webComm.rest.pingpong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.webComm.rest.AService;

@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PongService extends AService{

	@GET
	@Path("/pong")
	public Boolean getPong() {
		return true;
	}
}
