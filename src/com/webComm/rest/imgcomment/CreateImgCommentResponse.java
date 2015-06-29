package com.webComm.rest.imgcomment;

import com.webComm.rest.AResponse;

public class CreateImgCommentResponse extends AResponse {
	private static final long serialVersionUID = 1L;
	Long id;
	
	CreateImgCommentResponse (Long id) {
		this.id = id;
	}
	
}
