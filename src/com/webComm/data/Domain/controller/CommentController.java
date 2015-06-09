package com.webComm.data.Domain.controller;

import java.util.List;

import com.webComm.data.Domain.model.Comment;
import com.webComm.hibernate.Controller;
import com.webComm.hibernate.HibernatedEntity;

public class CommentController extends Controller {

	public CommentController() {
		super("domain", Comment.class);
	}

	public CommentController(String sessionname) {
		super(sessionname);
	}

	public List<? extends HibernatedEntity> findAll(
			Class<? extends HibernatedEntity> clazz) {
		return findAll(clazz.getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public List<Comment> findByUrl(String url) {
		List<Comment> objects = (List<Comment>) findAllByParameter(
				Comment.class, "url", url);
		return objects;
	}

	public Comment create(Comment comment) {
		save(Comment.class, comment);
		return comment;
	}

}
