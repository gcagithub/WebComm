package com.webComm.rest.imgcomment;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.webComm.data.ImgComment.controller.ImgCommentController;
import com.webComm.data.ImgComment.model.ImgComment;
import com.webComm.hibernate.HibernatedEntity;
import com.webComm.rest.AService;

@Path("/imgcomment")
public class ImgCommentService extends AService{

	@GET
	@Path("/getAllImgStatus")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ListImgCommentResponse getAllImgStatus(@QueryParam("imgSrcs[]") List<String> imgSrcs){
		ImgCommentController controller = new ImgCommentController();
		List<? extends HibernatedEntity> comments = controller.findAllByParameterList(ImgComment.class, "imgSrc", imgSrcs);
		return new ListImgCommentResponse(comments);
	}  
}
