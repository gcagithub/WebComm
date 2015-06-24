package com.webComm.data.auth.model;

import java.io.Serializable;
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
@Table(name = "users")
public class User extends HibernatedEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(User.class);
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "created_on_dt")
	private Date createdOnDT;
	
	@Column(name = "email", length = 128)
	private String email;
	
	@Column(name = "password", length = 128)
	private String password;
	
	@Column(name = "username", length = 128)
	private String username;
	
	@Column(name = "session_id", length = 128)
	private String sessionId;
	
	public User (MultivaluedMap<String, String> params) {
		setUsername(params.getFirst("username"));
		setEmail(params.getFirst("email"));
		setPassword(params.getFirst("password"));
		setSessionId(params.getFirst("sessionId"));
		setCreatedOnDT(new Date());
	}

	public Date getCreatedOnDT() {
		return createdOnDT;
	}

	public void setCreatedOnDT(Date createdOnDT) {
		this.createdOnDT = createdOnDT;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
