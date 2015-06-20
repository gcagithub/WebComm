package com.webComm.rest.imgcomment;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.hibernate.criterion.Order;

import com.webComm.data.ImgComment.controller.ImgCommentController;
import com.webComm.data.ImgComment.helpers.HashedImgComments;
import com.webComm.data.ImgComment.model.ImgComment;
import com.webComm.hibernate.HibernatedEntity;
import com.webComm.rest.AService;

@Path("/imgcomment")
@Produces(MediaType.APPLICATION_JSON)
public class ImgCommentService extends AService{
	private int PERCENT_MATCHED = 75;

	@GET
	@Path("/getAllImgStatus")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ListImgCommentResponse getAllImgStatusByHash(@QueryParam("imgHashIdList[]") List<String> hashIdList){
		ImgCommentController controller = new ImgCommentController();
		List<HashedImgComments> result = new ArrayList<HashedImgComments>();
		List<? extends HibernatedEntity> comments;
		for(String hashId : hashIdList){
			comments = controller.findAllByTolerance(ImgComment.class, hashId, PERCENT_MATCHED);
			if(!comments.isEmpty()){
				result.add(new HashedImgComments(hashId, comments.size()));
			}
		}
		return new ListImgCommentResponse(result);
	}
	
	public ListImgCommentResponse getAllImgStatus(@QueryParam("imgHashIdList[]") List<String> hashIdList){
		ImgCommentController controller = new ImgCommentController();
		List<? extends HibernatedEntity> comments = controller.findAllByParameterList(ImgComment.class, "hashId", hashIdList);
		return new ListImgCommentResponse(comments);
	}
	
	@POST
	@Path("/postImgComm")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public CreateImgCommentResponse createImgComment(MultivaluedMap<String, String> params) {
		ImgCommentController controller = new ImgCommentController();
		ImgComment comment = new ImgComment(params);
		Long id = controller.save(ImgComment.class, comment);
		return new CreateImgCommentResponse(id);
	}
	
	@GET
	@Path("/getImgComm")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ListImgCommentResponse getAllImgComments(@QueryParam("hashId") String hashId){
		ImgCommentController controller = new ImgCommentController();
		List<? extends HibernatedEntity> comments = controller
				.findAllByToleranceOrder(ImgComment.class, hashId, Order.asc("createdOnDT"), PERCENT_MATCHED);
		return new ListImgCommentResponse(comments);
	}
}
