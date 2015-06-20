package com.webComm.data.ImgComment.helpers;


import com.webComm.hibernate.HibernatedEntity;

public class HashedImgComments extends HibernatedEntity{
	private String hashId;
	private int count;
	
	public HashedImgComments (String hashId, int count) {
		this.hashId = hashId;
		this.count = count;
	}

	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
