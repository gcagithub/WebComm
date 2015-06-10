package com.webComm.rest.imgcomment;

import java.util.ArrayList;
import java.util.List;

import com.webComm.hibernate.HibernatedEntity;
import com.webComm.json.Writer;
import com.webComm.rest.AResponse;

public class ListImgCommentResponse extends AResponse {
	private static final long serialVersionUID = 1L;
	
	public List<String> result;
	
	public ListImgCommentResponse (List<? extends HibernatedEntity> comments) {
		this.result = convertToPrimitive(comments);
	}

	private List<String> convertToPrimitive(List<? extends HibernatedEntity> comments) {
		List<String> result = new ArrayList<String>(comments.size());
		comments.forEach((c) -> result.add(c.toString()));
		return result; 
	}
	
	public List<String> getContent(){
		return result;
	}
	
	
}
