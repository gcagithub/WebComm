package com.webComm.rest.comment;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.webComm.data.Domain.controller.CommentController;
import com.webComm.data.Domain.model.Comment;
import com.webComm.rest.AService;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ObjectService extends AService {
	@POST
	@Path("/create")
	public CreateResponse create(CreateRequest r) {
		CreateResponse response = new CreateResponse();
		CommentController cc = new CommentController();
		Comment comment = new Comment();
		comment.setUrl(r.url);
		comment.setText(r.text);
		cc.create(comment);
		response.createResult = true;
		return (CreateResponse) prepareResponse(response);
	}

	@POST
	@Path("/list")
	public ListResponse list(ListRequest r) {
		ListResponse response = new ListResponse();
		CommentController cc = new CommentController();
		List<Comment> comments = cc.findByUrl(r.url);
		response.comments = new String[comments.size()];
		int i = 0;
		for (Comment comment : comments) {
			response.comments[i] = comment.getText();
			i++;
		}
		return (ListResponse) prepareResponse(response);
	}
}
