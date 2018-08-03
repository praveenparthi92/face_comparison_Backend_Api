package com.FaceTool.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class FaceIdWithFbIdModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String faceBookId;
	
	private String faceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFaceBookId() {
		return faceBookId;
	}

	public void setFaceBookId(String faceBookId) {
		this.faceBookId = faceBookId;
	}

	public String getFaceId() {
		return faceId;
	}

	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
}
