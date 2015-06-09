package com.webComm.rest;

import java.io.Serializable;

public abstract class AResponse implements Serializable{
	private static final long serialVersionUID = 290709102973386263L;
	public String uid;
	public int signedin;
}
