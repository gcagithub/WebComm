package com.webComm.data.Domain;

import com.webComm.data.Domain.model.Comment;
import com.webComm.hibernate.SessionDescriptor;

public class DomainSession extends SessionDescriptor {

	public DomainSession() {
		super("domain");
		registerMap(Comment.class);
	}
}
