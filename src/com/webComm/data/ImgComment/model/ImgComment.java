package com.webComm.data.ImgComment.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webComm.hibernate.HibernatedEntity;

@Entity
@Table(name = "img_comment")
public class ImgComment extends HibernatedEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ImgComment.class);
	
	public ImgComment (MultivaluedMap<String, String> params) {
		setComment(params.getFirst("comment"));
		setTitle(params.getFirst("title"));
		setImgSrc(params.getFirst("imgSrc"));
		setCreatedOn(params.getFirst("createdOn"));
		
		this.createdOnDT = new Date();
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "img_src", length = 255)
	private String imgSrc;
	
	@Column(name = "comment", length = 512)
	private String comment;
	
	@Column(name = "title", length = 128)
	private String title;
	
	@Column(name = "created_on", length = 32)
	private String createdOn;
	
	@Column(name = "created_on_dt")
	private Date createdOnDT;
	
	@Column(name = "hash_id", length = 255)
	private String hashId;
	
	public ImgComment () {
		createdOnDT = new Date();
	}
	
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
		try {
			this.hashId = URLEncoder.encode(imgSrc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Error imgSrc html attribute encoding", e);
			this.hashId = imgSrc;
		}
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public Long getId() {
		return id;
	}
	public Date getCreatedOnDT() {
		return createdOnDT;
	}
	
}
